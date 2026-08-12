package cn.iocoder.yudao.module.temu.framework.client;

import cn.iocoder.yudao.module.temu.framework.client.api.AuthApi;
import cn.iocoder.yudao.module.temu.framework.client.api.OrderApi;
import cn.iocoder.yudao.module.temu.framework.client.api.ProductApi;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Temu OpenAPI 传输客户端。仅负责签名、HTTP 调用和 JSON 转换，日志及持久化由业务中间件处理。
 */
public class TemuClient {
    private static final String ROUTER_PATH = "/openapi/router";
    private static final String DATA_TYPE = "JSON";
    private final String appKey;
    private final String appSecret;
    private final String accessToken;
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AuthApi authApi;
    private final OrderApi orderApi;
    private final ProductApi productApi;

    /** 使用默认 HTTP 客户端创建 Temu 客户端。 */
    public TemuClient(String appKey, String appSecret, String accessToken, String site) {
        this(appKey, appSecret, accessToken, site,
                new RestTemplate(requestFactory()), new ObjectMapper());
    }

    /** 使用指定 HTTP 客户端创建 Temu 客户端，便于测试和统一网络配置。 */
    public TemuClient(String appKey, String appSecret, String accessToken, String site,
                      RestTemplate restTemplate, ObjectMapper objectMapper) {
        if (blank(appKey) || blank(appSecret) || blank(site)) {
            throw new IllegalArgumentException("Temu appKey、appSecret、site 不能为空");
        }
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.accessToken = accessToken;
        this.baseUrl = resolveEndpoint(site);
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.authApi = new AuthApi(this);
        this.orderApi = new OrderApi(this);
        this.productApi = new ProductApi(this);
    }

    /** @return 认证 API。 */
    public AuthApi getAuth() {
        return authApi;
    }

    /** @return 商品 API。 */
    public ProductApi getProduct() {
        return productApi;
    }

    /** @return 订单 API。 */
    public OrderApi getOrder() {
        return orderApi;
    }

    /** 发起 Temu Router 请求并返回原始 JSON，供各 API 做强类型转换。 */
    public JsonNode request(String apiType, HttpMethod method, Map<String, ?> payload) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("type", apiType);
        params.put("app_key", appKey);
        params.put("access_token", accessToken);
        params.put("timestamp", System.currentTimeMillis() / 1000);
        params.put("data_type", DATA_TYPE);
        params.putAll(filterNulls(payload));
        params.put("sign", sign(params));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = HttpMethod.GET.equals(method) ? UriComponentsBuilder.fromUriString(baseUrl + ROUTER_PATH)
                .queryParams(new org.springframework.util.LinkedMultiValueMap<>()).build().toUri()
                : URI.create(baseUrl + ROUTER_PATH);
        try {
            ResponseEntity<String> response = HttpMethod.GET.equals(method)
                    ? restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class)
                    : restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(params, headers), String.class);
            JsonNode body = objectMapper.readTree(response.getBody());
            if (body == null || body.isNull()) {
                throw new TemuClientException("Temu 响应为空: " + apiType);
            }
            // 所有 Router 接口统一在客户端拦截业务失败，调用方只处理成功结果和领域校验。
            if (!body.path("success").asBoolean(false)) {
                String error = body.path("errorMsg").asText(null);
                if (error == null || error.isBlank()) error = body.path("error_message").asText(null);
                throw new TemuClientException("Temu 业务请求失败: " + apiType
                        + (error == null || error.isBlank() ? "" : ": " + error));
            }
            return body;
        } catch (RestClientResponseException ex) {
            throw new TemuClientException("Temu HTTP 请求失败: " + apiType, ex);
        } catch (RestClientException | JacksonException ex) {
            throw new TemuClientException("Temu 请求处理失败: " + apiType, ex);
        }
    }

    /** 将结果节点转换为指定 DTO。 */
    public <T> T convert(JsonNode node, Class<T> type) {
        try {
            return node == null || node.isNull() ? null : objectMapper.treeToValue(node, type);
        } catch (JacksonException ex) {
            throw new TemuClientException("Temu 响应转换失败", ex);
        }
    }

    String sign(Map<String, ?> params) {
        StringBuilder value = new StringBuilder(appSecret);
        new TreeMap<>(params).forEach((key, item) -> value.append(key).append(signValue(item)));
        value.append(appSecret);
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(value.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(32);
            for (byte item : digest) result.append(String.format("%02X", item));
            return result.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("JDK 未提供 MD5 算法", ex);
        }
    }

    private String signValue(Object value) {
        if (value == null) return "";
        if (value instanceof Map || value instanceof List) {
            try { return objectMapper.writeValueAsString(value); }
            catch (JacksonException ex) { throw new TemuClientException("Temu 参数序列化失败", ex); }
        }
        return String.valueOf(value).replace(" ", "");
    }
    private Map<String, Object> filterNulls(Map<String, ?> source) {
        if (source == null) return Collections.emptyMap();
        Map<String, Object> result = new LinkedHashMap<>();
        source.forEach((key, value) -> { if (value != null) result.put(key, filterValue(value)); });
        return result;
    }
    private Object filterValue(Object value) {
        if (value instanceof Map) {
            Map<String, Object> result = new LinkedHashMap<>();
            ((Map<?, ?>) value).forEach((key, item) -> { if (item != null) result.put(String.valueOf(key), filterValue(item)); });
            return result;
        }
        if (value instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object item : (List<?>) value) if (item != null) result.add(filterValue(item));
            return result;
        }
        return value;
    }
    private static String resolveEndpoint(String site) {
        return normalize(TemuSite.from(site).getEndpoint());
    }
    private static String normalize(String value) {
        String result = value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
        return result.endsWith(ROUTER_PATH) ? result.substring(0, result.length() - ROUTER_PATH.length()) : result;
    }
    private static boolean blank(String value) { return value == null || value.trim().isEmpty(); }
    private static org.springframework.http.client.SimpleClientHttpRequestFactory requestFactory() {
        org.springframework.http.client.SimpleClientHttpRequestFactory factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); factory.setReadTimeout(30000); return factory;
    }
}
