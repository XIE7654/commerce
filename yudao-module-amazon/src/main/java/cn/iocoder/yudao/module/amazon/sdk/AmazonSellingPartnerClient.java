package cn.iocoder.yudao.module.amazon.sdk;

import jakarta.annotation.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

/** Amazon Selling Partner API HTTP 客户端，统一归档 JSON 响应。 */
@Component
public class AmazonSellingPartnerClient {

    @Resource
    private AmazonJsonStorageService amazonJsonStorageService;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 查询 Listings Items 并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri Listings Items 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @return Amazon Listings Items JSON 响应；空响应返回空 Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getListingsItems(URI uri, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("x-amz-access-token", accessToken);
        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        if (response.getBody() == null) {
            return Map.of();
        }
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        amazonJsonStorageService.persist(AmazonApiCategory.LISTINGS, "listings-items", body);
        return body;
    }

}
