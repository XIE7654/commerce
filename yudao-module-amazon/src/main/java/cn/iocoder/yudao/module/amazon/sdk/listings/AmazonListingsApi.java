package cn.iocoder.yudao.module.amazon.sdk.listings;

import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getList;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getPayload;

/** Listings Items 与 Listings Restrictions API SDK。 */
@Component
public class AmazonListingsApi {

    private static final String LISTINGS_PATH = "/listings/2021-08-01";

    @Resource
    private AmazonSellingPartnerClient client;

    /**
     * 查询卖家在指定 Marketplace 的 Listings Items。
     *
     * @param request Listings 查询参数与店铺授权上下文
     * @return Listings Items 响应
     */
    public AmazonApiResponse<Map<String, Object>> searchItems(AmazonListingsRequest request) {
        validateSearchRequest(request);
        Map<String, String> query = baseQuery(request);
        query.put("includedData", joinOrDefault(request.getIncludedData(), "summaries"));
        put(query, "identifiers", join(request.getIdentifiers()));
        put(query, "identifiersType", request.getIdentifiersType());
        put(query, "variationParentSku", request.getVariationParentSku());
        put(query, "packageHierarchySku", request.getPackageHierarchySku());
        put(query, "createdAfter", request.getCreatedAfter());
        put(query, "createdBefore", request.getCreatedBefore());
        put(query, "lastUpdatedAfter", request.getLastUpdatedAfter());
        put(query, "lastUpdatedBefore", request.getLastUpdatedBefore());
        put(query, "withIssueSeverity", join(request.getWithIssueSeverity()));
        put(query, "withStatus", join(request.getWithStatus()));
        put(query, "withoutStatus", join(request.getWithoutStatus()));
        query.put("sortBy", isBlank(request.getSortBy()) ? "lastUpdatedDate" : request.getSortBy());
        query.put("sortOrder", isBlank(request.getSortOrder()) ? "DESC" : request.getSortOrder());
        query.put("pageSize", String.valueOf(request.getPageSize() == null ? 10 : request.getPageSize()));
        put(query, "pageToken", request.getPageToken());
        put(query, "issueLocale", request.getIssueLocale());
        return get(request, LISTINGS_PATH + "/items/" + encodePathSegment(request.getSellerId()), query,
                "getListingsItems", "listings-items");
    }

    /**
     * 查询一个卖家 SKU 的 Listings Item。
     *
     * @param request 商品定位条件与店铺授权上下文
     * @return Listings Item 响应
     */
    public AmazonApiResponse<Map<String, Object>> getItem(AmazonListingsRequest request) {
        validateContext(request, true);
        Map<String, String> query = itemQuery(request);
        query.put("includedData", joinOrDefault(request.getIncludedData(), "summaries"));
        return get(request, itemPath(request), query, "getListingsItem", "listings-item");
    }

    /**
     * 创建或全量更新一个卖家 SKU 的 Listings Item。
     *
     * @param request 商品属性、提交模式与店铺授权上下文
     * @return Amazon 提交响应
     */
    public AmazonApiResponse<Map<String, Object>> putItem(AmazonListingsRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("productType", request.getProductType());
        body.put("attributes", request.getAttributes());
        if (!isBlank(request.getRequirements())) {
            body.put("requirements", request.getRequirements());
        }
        return mutateItem(request, HttpMethod.PUT, body, "putListingsItem");
    }

    /**
     * 按 JSON Patch 局部更新一个卖家 SKU 的 Listings Item。
     *
     * @param request Patch 操作、提交模式与店铺授权上下文
     * @return Amazon 提交响应
     */
    public AmazonApiResponse<Map<String, Object>> patchItem(AmazonListingsRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("productType", request.getProductType());
        body.put("patches", request.getPatches());
        return mutateItem(request, HttpMethod.PATCH, body, "patchListingsItem");
    }

    /**
     * 删除一个卖家 SKU 的 Listings Item。
     *
     * @param request 商品定位条件与店铺授权上下文
     * @return Amazon 删除响应
     */
    public AmazonApiResponse<Map<String, Object>> deleteItem(AmazonListingsRequest request) {
        validateContext(request, true);
        return mutate(request, HttpMethod.DELETE, itemPath(request), itemQuery(request), null, "deleteListingsItem");
    }

    /**
     * 查询指定 ASIN 在 Marketplace 的上架限制。
     *
     * @param request 限制查询条件与店铺授权上下文
     * @return Amazon 限制响应
     */
    public AmazonApiResponse<Map<String, Object>> getRestrictions(AmazonListingsRequest request) {
        validateContext(request, false);
        if (isBlank(request.getAsin())) {
            throw new IllegalArgumentException("ASIN 不能为空");
        }
        Map<String, String> query = baseQuery(request);
        query.put("asin", request.getAsin());
        query.put("sellerId", request.getSellerId());
        put(query, "conditionType", request.getConditionType());
        put(query, "reasonLocale", request.getReasonLocale());
        put(query, "productType", request.getProductType());
        return get(request, LISTINGS_PATH + "/restrictions", query, "getListingsRestrictions", "listings-restrictions");
    }

    /** 调用 Listings Item 写接口并保留统一的响应归档与审计行为。 */
    private AmazonApiResponse<Map<String, Object>> mutateItem(AmazonListingsRequest request, HttpMethod method,
                                                                     Object body, String operation) {
        Map<String, String> query = itemQuery(request);
        put(query, "includedData", join(request.getIncludedData()));
        put(query, "mode", request.getMode());
        return mutate(request, method, itemPath(request), query, body, operation);
    }

    /** 调用统一 HTTP 客户端，并将 Amazon 的 payload/errors 结构转换为 SDK 响应。 */
    private AmazonApiResponse<Map<String, Object>> get(AmazonListingsRequest request, String path,
                                                              Map<String, String> query, String operation, String storage) {
        validateContext(request, false);
        Map<String, Object> raw = client.getByCategory(buildUri(request, path, query), request.getAccessToken(),
                AmazonApiCategory.LISTINGS, operation, storage, request.getShopId(), request.getCountryCode(), request.getMarketplaceId());
        return response(raw);
    }

    /** 调用统一 HTTP 客户端执行写操作；Amazon 的提交结果与查询结果使用相同响应模型。 */
    private AmazonApiResponse<Map<String, Object>> mutate(AmazonListingsRequest request, HttpMethod method, String path,
                                                                 Map<String, String> query, Object body, String operation) {
        validateContext(request, true);
        Map<String, Object> raw = client.mutateByCategory(buildUri(request, path, query), request.getAccessToken(), method, body,
                AmazonApiCategory.LISTINGS, operation, "listings-item-submission", request.getShopId(), request.getCountryCode(),
                request.getMarketplaceId());
        return response(raw);
    }

    /** 将 Amazon 的 payload/errors 结构转换为调用方稳定的响应格式。 */
    private AmazonApiResponse<Map<String, Object>> response(Map<String, Object> raw) {
        List<?> errors = getList(raw, "errors");
        String msg = errors.isEmpty() ? null : String.valueOf(errors.get(0));
        return new AmazonApiResponse<>(errors.isEmpty() ? 200 : 400, getPayload(raw), msg);
    }

    /** 构造包含站点和问题语言的单商品查询参数。 */
    private Map<String, String> itemQuery(AmazonListingsRequest request) {
        Map<String, String> query = baseQuery(request);
        put(query, "issueLocale", request.getIssueLocale());
        return query;
    }

    /** 构造 Listings Item 资源路径，防止 Seller ID 或 SKU 中的特殊字符改变路由。 */
    private String itemPath(AmazonListingsRequest request) {
        validateContext(request, true);
        return LISTINGS_PATH + "/items/" + encodePathSegment(request.getSellerId()) + "/" + encodePathSegment(request.getSku());
    }

    /** 创建所有 Listings 请求共有的 Marketplace 参数。 */
    private Map<String, String> baseQuery(AmazonListingsRequest request) {
        Map<String, String> query = new TreeMap<>();
        query.put("marketplaceIds", request.getMarketplaceId());
        return query;
    }

    /** 按 RFC 3986 编码查询参数，确保分页 Token、SKU 等特殊字符不改变请求语义。 */
    private URI buildUri(AmazonListingsRequest request, String path, Map<String, String> query) {
        return URI.create(request.getEndpoint() + path + "?" + buildQuery(query));
    }

    /** 验证 SDK 所需上下文，避免将缺少授权或定位字段的请求发送至 Amazon。 */
    private void validateContext(AmazonListingsRequest request, boolean requireSku) {
        if (request == null || request.getShopId() == null || isBlank(request.getEndpoint()) || isBlank(request.getAccessToken())
                || isBlank(request.getCountryCode()) || isBlank(request.getMarketplaceId()) || isBlank(request.getSellerId())) {
            throw new IllegalArgumentException("Listings SDK 请求缺少店铺授权或 Marketplace 上下文");
        }
        if (requireSku && isBlank(request.getSku())) {
            throw new IllegalArgumentException("SKU 不能为空");
        }
    }

    /** 验证 Listings 搜索接口的互斥筛选条件。 */
    private void validateSearchRequest(AmazonListingsRequest request) {
        validateContext(request, false);
        boolean hasIdentifiers = !isEmpty(request.getIdentifiers());
        if (hasIdentifiers != !isBlank(request.getIdentifiersType())) {
            throw new IllegalArgumentException("identifiers 和 identifiersType 必须同时传入");
        }
        if (hasIdentifiers && (!isBlank(request.getVariationParentSku()) || !isBlank(request.getPackageHierarchySku()))) {
            throw new IllegalArgumentException("identifiers 不能与 variationParentSku 或 packageHierarchySku 同时使用");
        }
        if (!isBlank(request.getVariationParentSku()) && !isBlank(request.getPackageHierarchySku())) {
            throw new IllegalArgumentException("variationParentSku 与 packageHierarchySku 不能同时使用");
        }
    }

    /** 对路径段执行 UTF-8 编码，保持 Amazon 路由边界。 */
    private String encodePathSegment(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    /** 按键排序并编码查询参数，以生成稳定的 Amazon 请求 URI。 */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /** 使用 UTF-8 对查询参数进行 RFC 3986 百分号编码。 */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 仅在值非空时增加可选查询参数。 */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /** 将多值筛选参数转换为 Amazon 要求的逗号分隔格式。 */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /** 在未指定返回数据集时使用 Amazon 推荐的 summaries 默认值。 */
    private String joinOrDefault(List<String> values, String defaultValue) {
        String value = join(values);
        return isBlank(value) ? defaultValue : value;
    }

    /** 判断字符串是否为空白。 */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** 判断集合是否为空。 */
    private boolean isEmpty(List<?> values) {
        return values == null || values.isEmpty();
    }
}
