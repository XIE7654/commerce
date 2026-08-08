package cn.iocoder.yudao.module.amazon.sdk;

import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import cn.iocoder.yudao.module.amazon.service.apilog.AmazonApiRequestLogContext;
import cn.iocoder.yudao.module.amazon.service.apilog.AmazonApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

/** Amazon OAuth HTTP 客户端，统一归档 JSON 响应。 */
@Component
public class AmazonOAuthClient {

    @Resource
    private AwsProperties properties;
    @Resource
    private AmazonJsonStorageService amazonJsonStorageService;
    @Resource
    private AmazonApiRequestLogService amazonApiRequestLogService;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 向 Login with Amazon 或 Ads OAuth 端点申请 Token。
     *
     * @param url OAuth Token 端点
     * @param code 授权码；刷新 Token 时传 {@code null}
     * @param refreshToken refresh token；授权码换取时传 {@code null}
     * @param type 授权类型，{@code ads} 使用广告应用凭据
     * @return OAuth JSON 响应
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> requestToken(String url, String code, String refreshToken, String type) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Amazon token endpoint 未配置");
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", refreshToken == null ? "authorization_code" : "refresh_token");
        form.add(refreshToken == null ? "code" : "refresh_token", refreshToken == null ? code : refreshToken);
        boolean ads = "ads".equalsIgnoreCase(type);
        form.add("client_id", ads ? properties.getAdClientId() : properties.getClientId());
        form.add("client_secret", ads ? properties.getAdClientSecret() : properties.getClientSecret());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        URI uri = URI.create(url);
        AmazonApiRequestLogContext context = new AmazonApiRequestLogContext("requestToken", "tokens", HttpMethod.POST.name(),
                uri, null, null, null, form, headers, LocalDateTime.now());
        ResponseEntity<Map> httpResponse;
        try {
            httpResponse = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(form, headers), Map.class);
        } catch (RestClientResponseException exception) {
            amazonApiRequestLogService.log(context, exception.getStatusCode().value(), exception.getResponseHeaders(), exception);
            throw exception;
        } catch (RuntimeException exception) {
            amazonApiRequestLogService.log(context, null, null, exception);
            throw exception;
        }
        Map<?, ?> result = httpResponse.getBody();
        if (result == null) {
            IllegalStateException exception = new IllegalStateException("Amazon OAuth token 响应为空");
            amazonApiRequestLogService.log(context, httpResponse.getStatusCode().value(), httpResponse.getHeaders(), exception);
            throw exception;
        }
        Map<String, Object> response = (Map<String, Object>) result;
        if (response.get("access_token") == null) {
            IllegalStateException exception = new IllegalStateException("Amazon OAuth token 响应缺少 access_token");
            amazonApiRequestLogService.log(context, httpResponse.getStatusCode().value(), httpResponse.getHeaders(), exception);
            throw exception;
        }
        amazonApiRequestLogService.log(context, httpResponse.getStatusCode().value(), httpResponse.getHeaders(), null);
        amazonJsonStorageService.persist(AmazonApiCategory.TOKENS, "oauth-token", response);
        return response;
    }

}
