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
     * 调用 Listings Items 的写入或删除接口并归档 Amazon 响应。
     *
     * @param uri Listings Items 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方法
     * @param body 请求体；删除时为 {@code null}
     * @param operationName Amazon 操作名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon Listings JSON 响应
     */
    public Map<String, Object> mutateListingsItem(URI uri, String accessToken, HttpMethod method, Object body,
                                                  String operationName, Long shopId, String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, method, body, AmazonApiCategory.LISTINGS, operationName,
                "listings-item-submission", shopId, countryCode, marketplaceId);
    }

    /**
     * 查询商品限制并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri Listings Restrictions 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon Listings Restrictions JSON 响应
     */
    public Map<String, Object> getListingsRestrictions(URI uri, String accessToken, Long shopId, String countryCode,
                                                        String marketplaceId) {
        return getListings(uri, accessToken, "getListingsRestrictions", "listings-restrictions", shopId, countryCode,
                marketplaceId);
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
     * 查询报表任务或报表文件元数据并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri Reports API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Reports JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getReports(URI uri, String accessToken, String operationName, String storageName,
                                          Long shopId, String countryCode, String marketplaceId) {
        return get(uri, accessToken, AmazonApiCategory.REPORTS, operationName, storageName, shopId, countryCode, marketplaceId);
    }

    /** 调用 Feeds API 查询或创建资源并归档 JSON 响应。 */
    public Map<String, Object> getFeeds(URI uri, String accessToken, String operationName, String storageName,
                                        Long shopId, String countryCode, String marketplaceId) {
        return get(uri, accessToken, AmazonApiCategory.FEEDS, operationName, storageName, shopId, countryCode, marketplaceId);
    }

    /** 创建 Feeds API 资源并归档 Amazon 返回的 JSON 响应。 */
    public Map<String, Object> createFeed(URI uri, String accessToken, Object body, String operationName,
                                          String storageName, Long shopId, String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, HttpMethod.POST, body, AmazonApiCategory.FEEDS, operationName,
                storageName, shopId, countryCode, marketplaceId);
    }

    /** 取消尚未开始处理的 Feed；Amazon 成功时返回 204。 */
    public void cancelFeed(URI uri, String accessToken, Long shopId, String countryCode, String marketplaceId) {
        HttpHeaders headers = buildHeaders(accessToken, false);
        AmazonApiRequestLogContext context = new AmazonApiRequestLogContext("cancelFeed", AmazonApiCategory.FEEDS.getDirectoryName(),
                HttpMethod.DELETE.name(), uri, shopId, countryCode, List.of(marketplaceId), queryParams(uri), headers, LocalDateTime.now(), null);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
            amazonApiRequestLogService.log(context, response.getStatusCode().value(), response.getHeaders(), null);
        } catch (RestClientResponseException exception) {
            amazonApiRequestLogService.log(context, exception.getStatusCode().value(), exception.getResponseHeaders(), exception);
            throw exception;
        } catch (RuntimeException exception) {
            amazonApiRequestLogService.log(context, null, null, exception);
            throw exception;
        }
    }

    /**
     * 创建报表任务并保存 Amazon 返回的 JSON 数据。
     *
     * @param uri 创建报表任务的请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param body Amazon Reports API 请求体
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return 创建任务后的 Amazon JSON 响应
     */
    public Map<String, Object> createReport(URI uri, String accessToken, Map<String, Object> body, Long shopId,
                                            String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, HttpMethod.POST, body, AmazonApiCategory.REPORTS, "createReport",
                "report", shopId, countryCode, marketplaceId);
    }

    /**
     * 取消尚未开始处理的报表任务。
     *
     * @param uri 取消报表任务的请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     */
    public void cancelReport(URI uri, String accessToken, Long shopId, String countryCode, String marketplaceId) {
        HttpHeaders headers = buildHeaders(accessToken, false);
        AmazonApiRequestLogContext context = new AmazonApiRequestLogContext("cancelReport", AmazonApiCategory.REPORTS.getDirectoryName(),
                HttpMethod.DELETE.name(), uri, shopId, countryCode, List.of(marketplaceId), queryParams(uri), headers, LocalDateTime.now(), null);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
            amazonApiRequestLogService.log(context, response.getStatusCode().value(), response.getHeaders(), null);
        } catch (RestClientResponseException exception) {
            amazonApiRequestLogService.log(context, exception.getStatusCode().value(), exception.getResponseHeaders(), exception);
            throw exception;
        } catch (RuntimeException exception) {
            amazonApiRequestLogService.log(context, null, null, exception);
            throw exception;
        }
    }

    /**
     * 调用 Amazon Orders API 并保存返回的 JSON 数据。
     *
     * @param uri Orders 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param operationName Amazon Orders 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon Orders JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getOrders(URI uri, String accessToken, String operationName, String storageName,
                                         Long shopId, String countryCode, String marketplaceId) {
        return get(uri, accessToken, AmazonApiCategory.ORDERS, operationName, storageName, shopId, countryCode, marketplaceId);
    }

    /**
     * 调用指定分类的只读接口并归档 JSON 响应。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param category 请求所属 API 分类
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求关联的 Marketplace ID
     * @return Amazon JSON 响应；空响应返回空 Map
     */
    public Map<String, Object> getByCategory(URI uri, String accessToken, AmazonApiCategory category, String operationName,
                                             String storageName, Long shopId, String countryCode, String marketplaceId) {
        return get(uri, accessToken, category, operationName, storageName, shopId, countryCode, marketplaceId);
    }

    /**
     * 调用指定分类的写接口并归档 JSON 响应。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方式
     * @param body SP-API 请求体
     * @param category 请求所属 API 分类
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求关联的 Marketplace ID
     * @return Amazon JSON 响应
     */
    public Map<String, Object> mutateByCategory(URI uri, String accessToken, HttpMethod method, Object body,
                                                AmazonApiCategory category, String operationName, String storageName,
                                                Long shopId, String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, method, body, category, operationName, storageName, shopId,
                countryCode, marketplaceId);
    }

    /**
     * 调用 Seller Wallet 写接口并透传 Amazon 要求的数字签名请求头。
     *
     * @param uri Seller Wallet 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方式
     * @param body 请求体
     * @param requestHeaders 业务接口要求的附加请求头
     * @param operationName Amazon API 操作名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求关联的 Marketplace ID
     * @return Amazon JSON 响应；允许空响应时返回空 Map
     */
    public Map<String, Object> mutateSellerWallet(URI uri, String accessToken, HttpMethod method, Object body,
                                                  Map<String, String> requestHeaders, String operationName, Long shopId,
                                                  String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, method, body, AmazonApiCategory.SELLER_WALLET, operationName,
                "seller-wallet-mutation", shopId, countryCode, marketplaceId, true, requestHeaders);
    }

    /**
     * 调用 Orders 的写入接口并归档 Amazon 响应。
     *
     * @param uri Orders 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方法
     * @param body Amazon 请求体
     * @param operationName Amazon 操作名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon Orders JSON 响应
     */
    public Map<String, Object> mutateOrders(URI uri, String accessToken, HttpMethod method, Object body,
                                            String operationName, Long shopId, String countryCode, String marketplaceId) {
        return exchangeForOptionalBody(uri, accessToken, method, body, AmazonApiCategory.ORDERS, operationName,
                "orders-mutation", shopId, countryCode, marketplaceId);
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
        return exchangeForBody(uri, accessToken, HttpMethod.GET, null, category, operationName, storageName, shopId,
                countryCode, marketplaceId);
    }

    /**
     * 执行需要 JSON 响应的 SP-API 请求并将响应持久化，供查询和创建类接口统一复用。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方式
     * @param body 请求体；GET 请求可为 {@code null}
     * @param category 响应归档的 API 分类
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon JSON 响应
     */
    private Map<String, Object> exchangeForBody(URI uri, String accessToken, HttpMethod method, Object body,
                                                AmazonApiCategory category, String operationName, String storageName,
                                                Long shopId, String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, method, body, category, operationName, storageName, shopId, countryCode,
                marketplaceId, false);
    }

    /**
     * 执行可能返回空响应体的 SP-API 请求；Orders 写接口成功时按规范返回 HTTP 204。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方式
     * @param body Amazon 请求体
     * @param category 响应归档的 API 分类
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @return Amazon JSON 响应；204 响应返回空 Map
     */
    private Map<String, Object> exchangeForOptionalBody(URI uri, String accessToken, HttpMethod method, Object body,
                                                        AmazonApiCategory category, String operationName, String storageName,
                                                        Long shopId, String countryCode, String marketplaceId) {
        return exchangeForBody(uri, accessToken, method, body, category, operationName, storageName, shopId, countryCode,
                marketplaceId, true);
    }

    /**
     * 执行 SP-API 请求并在响应存在时归档 JSON；仅允许指定接口以空响应表示成功。
     *
     * @param uri SP-API 请求地址
     * @param accessToken 店铺的 Seller LWA access token
     * @param method HTTP 请求方式
     * @param body Amazon 请求体；GET 请求可为 {@code null}
     * @param category 响应归档的 API 分类
     * @param operationName Amazon API 操作名称
     * @param storageName JSON 存储名称
     * @param shopId 店铺编号
     * @param countryCode 站点国家代码
     * @param marketplaceId 请求的 Marketplace ID
     * @param allowEmptyResponse 是否允许 HTTP 204 空响应
     * @return Amazon JSON 响应；允许空响应时返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> exchangeForBody(URI uri, String accessToken, HttpMethod method, Object body,
                                                AmazonApiCategory category, String operationName, String storageName,
                                                Long shopId, String countryCode, String marketplaceId, boolean allowEmptyResponse) {
        return exchangeForBody(uri, accessToken, method, body, category, operationName, storageName, shopId, countryCode,
                marketplaceId, allowEmptyResponse, Map.of());
    }

    /**
     * 执行 SP-API 请求并合并业务接口要求的附加请求头。
     *
     * @param requestHeaders 业务接口要求的附加请求头；用于 Seller Wallet 数字签名等受限字段
     * @return Amazon JSON 响应；允许空响应时返回空 Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> exchangeForBody(URI uri, String accessToken, HttpMethod method, Object body,
                                                AmazonApiCategory category, String operationName, String storageName,
                                                Long shopId, String countryCode, String marketplaceId, boolean allowEmptyResponse,
                                                Map<String, String> requestHeaders) {
        HttpHeaders headers = buildHeaders(accessToken, body != null);
        requestHeaders.forEach(headers::set);
        AmazonApiRequestLogContext context = new AmazonApiRequestLogContext(operationName, category.getDirectoryName(),
                method.name(), uri, shopId, countryCode, List.of(marketplaceId), body == null ? queryParams(uri) : body,
                headers, LocalDateTime.now(), null);
        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(uri, method, new HttpEntity<>(body, headers), Map.class);
        } catch (RestClientResponseException exception) {
            amazonApiRequestLogService.log(context, exception.getStatusCode().value(), exception.getResponseHeaders(), exception);
            throw exception;
        } catch (RuntimeException exception) {
            amazonApiRequestLogService.log(context, null, null, exception);
            throw exception;
        }
        if (response.getBody() == null) {
            if (allowEmptyResponse) {
                amazonApiRequestLogService.log(context, response.getStatusCode().value(), response.getHeaders(), null);
                return Map.of();
            }
            IllegalStateException exception = new IllegalStateException("Amazon SP-API 响应为空");
            amazonApiRequestLogService.log(context, response.getStatusCode().value(), response.getHeaders(), exception);
            throw exception;
        }
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        Long fileId = amazonJsonStorageService.persist(category, storageName, responseBody);
        amazonApiRequestLogService.log(context.withFileId(fileId), response.getStatusCode().value(), response.getHeaders(), null);
        return responseBody;
    }

    /**
     * 构建调用 SP-API 所需的通用请求头。
     *
     * @param accessToken 店铺的 Seller LWA access token
     * @param hasJsonBody 是否携带 JSON 请求体
     * @return 包含调用鉴权与媒体类型的请求头
     */
    private HttpHeaders buildHeaders(String accessToken, boolean hasJsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (hasJsonBody) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.set("x-amz-access-token", accessToken);
        return headers;
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
