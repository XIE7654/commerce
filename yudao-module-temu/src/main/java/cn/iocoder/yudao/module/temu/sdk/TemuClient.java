package cn.iocoder.yudao.module.temu.sdk;

import cn.iocoder.yudao.module.temu.sdk.api.AdsApi;
import cn.iocoder.yudao.module.temu.sdk.api.AfterSalesApi;
import cn.iocoder.yudao.module.temu.sdk.api.AuthApi;
import cn.iocoder.yudao.module.temu.sdk.api.FulfillmentApi;
import cn.iocoder.yudao.module.temu.sdk.api.LogisticsApi;
import cn.iocoder.yudao.module.temu.sdk.api.OrderApi;
import cn.iocoder.yudao.module.temu.sdk.api.PriceApi;
import cn.iocoder.yudao.module.temu.sdk.api.ProductApi;
import cn.iocoder.yudao.module.temu.sdk.api.PromotionApi;
import cn.iocoder.yudao.module.temu.sdk.api.WebhookApi;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
 * Temu OpenAPI Java SDK 客户端。
 *
 * <p>请求协议与 {@code temu_api} Python SDK 保持一致，接口请求统一发送到
 * {@code /openapi/router}，服务类负责暴露各业务接口。</p>
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
    private final TemuJsonStorageService jsonStorageService;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    private final AuthApi auth = new AuthApi(this);
    private final AdsApi ads = new AdsApi(this);
    private final AfterSalesApi afterSales = new AfterSalesApi(this);
    private final OrderApi order = new OrderApi(this);
    private final LogisticsApi logistics = new LogisticsApi(this);
    private final PromotionApi promotion = new PromotionApi(this);
    private final PriceApi price = new PriceApi(this);
    private final ProductApi product = new ProductApi(this);
    private final FulfillmentApi fulfillment = new FulfillmentApi(this);
    private final WebhookApi webhook = new WebhookApi(this);

    /**
     * 使用默认 HTTP 客户端创建 SDK。
     *
     * @param appKey 应用 Key
     * @param appSecret 应用 Secret
     * @param accessToken 店铺授权 Token
     * @param baseUrl Temu OpenAPI 区域域名
     */
    public TemuClient(String appKey, String appSecret, String accessToken, String baseUrl) {
        this(appKey, appSecret, accessToken, baseUrl,
                new RestTemplate(createRequestFactory(10000, 30000)), new ObjectMapper(), 10000, 30000, null);
    }

    /**
     * 使用默认 HTTP 客户端创建 SDK，并归档每次成功调用的响应。
     *
     * @param appKey 应用 Key
     * @param appSecret 应用 Secret
     * @param accessToken 店铺授权 Token
     * @param baseUrl Temu OpenAPI 区域域名
     * @param jsonStorageService 响应归档服务
     */
    public TemuClient(String appKey, String appSecret, String accessToken, String baseUrl,
                      TemuJsonStorageService jsonStorageService) {
        this(appKey, appSecret, accessToken, baseUrl,
                new RestTemplate(createRequestFactory(10000, 30000)), new ObjectMapper(), 10000, 30000, jsonStorageService);
    }

    /**
     * 使用调用方提供的 HTTP 客户端创建 SDK，便于接入统一代理、链路追踪和测试 Mock。
     *
     * @param appKey 应用 Key
     * @param appSecret 应用 Secret
     * @param accessToken 店铺授权 Token
     * @param baseUrl Temu OpenAPI 区域域名
     * @param restTemplate HTTP 客户端
     * @param objectMapper JSON 转换器
     * @param connectTimeoutMillis 连接超时时间，仅作为客户端配置说明
     * @param readTimeoutMillis 读取超时时间，仅作为客户端配置说明
     */
    public TemuClient(String appKey, String appSecret, String accessToken, String baseUrl,
                      RestTemplate restTemplate, ObjectMapper objectMapper,
                      int connectTimeoutMillis, int readTimeoutMillis) {
        this(appKey, appSecret, accessToken, baseUrl, restTemplate, objectMapper,
                connectTimeoutMillis, readTimeoutMillis, null);
    }

    /**
     * 使用调用方提供的 HTTP 客户端和响应归档服务创建 SDK。
     *
     * @param appKey 应用 Key
     * @param appSecret 应用 Secret
     * @param accessToken 店铺授权 Token
     * @param baseUrl Temu OpenAPI 区域域名
     * @param restTemplate HTTP 客户端
     * @param objectMapper JSON 转换器
     * @param connectTimeoutMillis 连接超时时间，仅作为客户端配置说明
     * @param readTimeoutMillis 读取超时时间，仅作为客户端配置说明
     * @param jsonStorageService 响应归档服务；为 {@code null} 时不归档，兼容 SDK 独立使用
     */
    public TemuClient(String appKey, String appSecret, String accessToken, String baseUrl,
                      RestTemplate restTemplate, ObjectMapper objectMapper,
                      int connectTimeoutMillis, int readTimeoutMillis, TemuJsonStorageService jsonStorageService) {
        if (isBlank(appKey) || isBlank(appSecret) || isBlank(baseUrl)) {
            throw new IllegalArgumentException("Temu appKey、appSecret、baseUrl 不能为空");
        }
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.accessToken = accessToken;
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.jsonStorageService = jsonStorageService;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    /** @return 认证接口服务 */
    public AuthApi getAuth() { return auth; }
    /** @return 广告接口服务 */
    public AdsApi getAds() { return ads; }
    /** @return 售后接口服务 */
    public AfterSalesApi getAfterSales() { return afterSales; }
    /** @return 售后接口服务；兼容 Python SDK 的 aftersales 命名 */
    public AfterSalesApi getAftersales() { return afterSales; }
    /** @return 订单接口服务 */
    public OrderApi getOrder() { return order; }
    /** @return 物流接口服务 */
    public LogisticsApi getLogistics() { return logistics; }
    /** @return 活动接口服务 */
    public PromotionApi getPromotion() { return promotion; }
    /** @return 价格接口服务 */
    public PriceApi getPrice() { return price; }
    /** @return 商品接口服务 */
    public ProductApi getProduct() { return product; }
    /** @return 履约接口服务 */
    public FulfillmentApi getFulfillment() { return fulfillment; }
    /** @return Webhook 事件订阅接口服务 */
    public WebhookApi getWebhook() { return webhook; }

    /**
     * 调用 Temu Router 接口。
     *
     * @param apiType Temu 接口 type
     * @param method HTTP 方法；当前 SDK 支持 GET 和 POST
     * @param payload 业务参数，null 值会递归过滤
     * @return Temu 返回的 JSON 内容
     */
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
        try {
            ResponseEntity<String> response;
            if (HttpMethod.GET.equals(method)) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + ROUTER_PATH);
                params.forEach(builder::queryParam);
                response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,
                        new HttpEntity<>(headers), String.class);
            } else {
                response = restTemplate.exchange(baseUrl + ROUTER_PATH, HttpMethod.POST,
                        new HttpEntity<>(params, headers), String.class);
            }
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            persistResponse(apiType, responseBody);
            return responseBody;
        } catch (RestClientException | JacksonException ex) {
            throw new TemuApiException("调用 Temu OpenAPI 失败: " + apiType, ex);
        }
    }

    /**
     * 归档成功调用的 Temu 原始响应。
     *
     * @param apiType Temu OpenAPI 接口 type
     * @param responseBody 已解析的响应 JSON
     */
    private void persistResponse(String apiType, JsonNode responseBody) {
        if (jsonStorageService != null) {
            jsonStorageService.persist(apiType, responseBody);
        }
    }

    /**
     * 计算 Temu 签名：按参数名排序后拼接 key/value，并在首尾包裹 appSecret 后取大写 MD5。
     */
    String sign(Map<String, ?> params) {
        StringBuilder content = new StringBuilder(appSecret);
        new TreeMap<>(params).forEach((key, value) -> content.append(key).append(signValue(value)));
        content.append(appSecret);
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(content.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(32);
            for (byte item : digest) {
                result.append(String.format("%02X", item));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("JDK 未提供 MD5 算法", ex);
        }
    }

    private String signValue(Object value) {
        if (value == null) return "";
        if (value instanceof Map || value instanceof List) {
            try { return objectMapper.writeValueAsString(value); }
            catch (JacksonException ex) { throw new TemuApiException("Temu 参数序列化失败", ex); }
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

    private static boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
    private static String normalizeBaseUrl(String value) {
        String normalized = value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
        String routerSuffix = ROUTER_PATH;
        if (normalized.endsWith(routerSuffix)) {
            normalized = normalized.substring(0, normalized.length() - routerSuffix.length());
        }
        return normalized;
    }

    private static org.springframework.http.client.SimpleClientHttpRequestFactory createRequestFactory(
            int connectTimeoutMillis, int readTimeoutMillis) {
        org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutMillis);
        factory.setReadTimeout(readTimeoutMillis);
        return factory;
    }
}
