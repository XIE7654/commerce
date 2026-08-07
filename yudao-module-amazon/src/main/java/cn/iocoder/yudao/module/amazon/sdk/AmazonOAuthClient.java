package cn.iocoder.yudao.module.amazon.sdk;

import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import jakarta.annotation.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** Amazon OAuth HTTP 客户端，统一归档 JSON 响应。 */
@Component
public class AmazonOAuthClient {

    @Resource
    private AwsProperties properties;
    @Resource
    private AmazonJsonStorageService amazonJsonStorageService;

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
        Map<?, ?> result = restTemplate.postForObject(url, new HttpEntity<>(form, headers), Map.class);
        if (result == null) {
            throw new IllegalStateException("Amazon OAuth token 响应为空");
        }
        Map<String, Object> response = (Map<String, Object>) result;
        amazonJsonStorageService.persist(AmazonApiCategory.TOKENS, "oauth-token", response);
        if (response.get("access_token") == null) {
            throw new IllegalStateException("Amazon OAuth token 响应缺少 access_token");
        }
        return response;
    }

}
