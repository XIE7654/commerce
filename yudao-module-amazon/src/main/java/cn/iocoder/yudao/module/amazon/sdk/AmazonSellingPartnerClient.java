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
    public Map<String, Object> getListingsItems(URI uri, String accessToken) {
        return getListings(uri, accessToken, "listings-items");
    }

    /**
     * 查询单个 Listings Item 并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri 单个 Listings Item 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @return Amazon Listings Item JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getListingsItem(URI uri, String accessToken) {
        return getListings(uri, accessToken, "listings-item");
    }

    /**
     * 查询 FBA 库存摘要并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri FBA Inventory summaries 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @return FBA 库存摘要 JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getInventorySummaries(URI uri, String accessToken) {
        return get(uri, accessToken, AmazonApiCategory.FBA_INVENTORY, "inventory-summaries");
    }

    /**
     * 执行 Listings API 的 GET 请求并持久化响应，以保留 Amazon 返回字段供后续排查。
     *
     * @param uri Listings 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param storageName JSON 存储名称
     * @return Amazon Listings JSON 响应；空响应返回空 Map
     */
    private Map<String, Object> getListings(URI uri, String accessToken, String storageName) {
        return get(uri, accessToken, AmazonApiCategory.LISTINGS, storageName);
    }

    /**
     * 执行 SP-API 的 GET 请求并持久化 JSON 响应，供不同只读 API 统一复用。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param category 响应归档的 API 分类
     * @param storageName JSON 存储名称
     * @return Amazon JSON 响应；空响应返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> get(URI uri, String accessToken, AmazonApiCategory category, String storageName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("x-amz-access-token", accessToken);
        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        if (response.getBody() == null) {
            return Map.of();
        }
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        amazonJsonStorageService.persist(category, storageName, body);
        return body;
    }

}
