package cn.iocoder.yudao.module.amazon.sdk;

import cn.iocoder.yudao.module.amazon.service.apilog.AmazonApiRequestLogContext;
import cn.iocoder.yudao.module.amazon.service.apilog.AmazonApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Amazon Selling Partner API HTTP 客户端，统一归档 JSON 响应。 */
@Component
public class AmazonSellingPartnerClient {

    @Resource
    private AmazonJsonStorageService amazonJsonStorageService;
    @Resource
    private AmazonApiRequestLogService amazonApiRequestLogService;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 查询 Listings Items 并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri Listings Items 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon Listings Items JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getListingsItems(URI uri, String accessToken, Long shopId, String countryCode,
                                                String marketplaceId) {
        return getListings(uri, accessToken, "getListingsItems", "listings-items", shopId, countryCode, marketplaceId);
    }

    /**
     * 查询单个 Listings Item 并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri 单个 Listings Item 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon Listings Item JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getListingsItem(URI uri, String accessToken, Long shopId, String countryCode,
                                               String marketplaceId) {
        return getListings(uri, accessToken, "getListingsItem", "listings-item", shopId, countryCode, marketplaceId);
    }

    /**
     * 查询 FBA 库存摘要并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri FBA Inventory summaries 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return FBA 库存摘要 JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getInventorySummaries(URI uri, String accessToken, Long shopId, String countryCode,
                                                      String marketplaceId) {
        return get(uri, accessToken, AmazonApiCategory.FBA_INVENTORY, "getInventorySummaries", "inventory-summaries",
                shopId, countryCode, marketplaceId);
    }

    /**
     * 执行 Listings API 的 GET 请求并持久化响应，以保留 Amazon 返回字段供后续排查。
     *
     * @param uri Listings 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param storageName JSON 存储名称
     * @return Amazon Listings JSON 响应；空响应返回空 Map
     */
    private Map<String, Object> getListings(URI uri, String accessToken, String operationName, String storageName,
                                            Long shopId, String countryCode, String marketplaceId) {
        return get(uri, accessToken, AmazonApiCategory.LISTINGS, operationName, storageName, shopId, countryCode, marketplaceId);
    }

    /**
     * 执行 SP-API 的 GET 请求并持久化 JSON 响应，供不同只读 API 统一复用。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param category 响应归档的 API 分类
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon JSON 响应；空响应返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> get(URI uri, String accessToken, AmazonApiCategory category, String operationName,
                                    String storageName, Long shopId, String countryCode, String marketplaceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("x-amz-access-token", accessToken);
        AmazonApiRequestLogContext context = new AmazonApiRequestLogContext(operationName, category.getDirectoryName(),
                HttpMethod.GET.name(), uri, shopId, countryCode, List.of(marketplaceId), queryParams(uri), headers, LocalDateTime.now());
        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        } catch (RestClientResponseException exception) {
            amazonApiRequestLogService.log(context, exception.getStatusCode().value(), exception.getResponseHeaders(), exception);
            throw exception;
        } catch (RuntimeException exception) {
            amazonApiRequestLogService.log(context, null, null, exception);
            throw exception;
        }
        if (response.getBody() == null) {
            IllegalStateException exception = new IllegalStateException("Amazon SP-API 响应为空");
            amazonApiRequestLogService.log(context, response.getStatusCode().value(), response.getHeaders(), exception);
            throw exception;
        }
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        amazonApiRequestLogService.log(context, response.getStatusCode().value(), response.getHeaders(), null);
        amazonJsonStorageService.persist(category, storageName, body);
        return body;
    }

    /**
     * 将 URI 查询串解析为键值对，供审计服务按字段名脱敏后记录。
     *
     * @param uri 请求 URI
     * @return 查询参数键值对
     */
    private Map<String, String> queryParams(URI uri) {
        Map<String, String> params = new LinkedHashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.isBlank()) {
            return params;
        }
        for (String item : query.split("&")) {
            int separatorIndex = item.indexOf('=');
            params.put(separatorIndex < 0 ? item : item.substring(0, separatorIndex),
                    separatorIndex < 0 ? "" : item.substring(separatorIndex + 1));
        }
        return params;
    }

}
