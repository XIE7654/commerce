package cn.iocoder.yudao.module.amazon.service.listings;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Amazon Listings Items 服务实现。
 *
 * <p>Listings Items API 使用 Seller LWA access token 进行鉴权，不在应用侧配置 IAM 凭据。</p>
 */
@Service
public class AmazonListingsServiceImpl implements AmazonListingsService {

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> searchListingsItems(AmazonListingsSearchReqVO request) {
        validateRequest(request);
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        URI uri = buildRequestUri(marketplace, shop.getSellerId(), request);
        return amazonSellingPartnerClient.getListingsItems(uri, accessToken);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getListingsItem(AmazonListingsItemGetReqVO request) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        URI uri = buildItemRequestUri(marketplace, shop.getSellerId(), request);
        return amazonSellingPartnerClient.getListingsItem(uri, accessToken);
    }

    /**
     * 验证 Listings API 的互斥和依赖参数，避免将 Amazon 可预见的 400 请求发送到上游。
     *
     * @param request Listings 查询请求
     */
    private void validateRequest(AmazonListingsSearchReqVO request) {
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

    /**
     * 构造 Amazon Listings 搜索 URI；查询参数采用 RFC 3986 编码以避免 SKU、分页 Token 等特殊字符改变请求语义。
     *
     * @param marketplace 目标站点配置
     * @param sellerId 店铺 Seller ID
     * @param request 查询筛选条件
     * @return 可直接发起请求的 URI
     */
    private URI buildRequestUri(AmazonMarketplaceEnum marketplace, String sellerId, AmazonListingsSearchReqVO request) {
        if (isBlank(sellerId)) {
            throw new IllegalArgumentException("店铺未配置 Amazon sellerId");
        }
        Map<String, String> query = new TreeMap<>();
        query.put("marketplaceIds", marketplace.getMarketplaceId());
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

        String path = "/listings/2021-08-01/items/" + UriUtils.encodePathSegment(sellerId, StandardCharsets.UTF_8);
        return URI.create(marketplace.getEndpoint() + path + "?" + buildQuery(query));
    }

    /**
     * 构造单个 Listings Item 查询 URI；SKU 必须作为路径段编码，避免其中的斜杠等字符改变 Amazon 路由。
     *
     * @param marketplace 目标站点配置
     * @param sellerId 店铺 Seller ID
     * @param request 单商品查询参数
     * @return 可直接发起请求的 URI
     */
    private URI buildItemRequestUri(AmazonMarketplaceEnum marketplace, String sellerId, AmazonListingsItemGetReqVO request) {
        if (isBlank(sellerId)) {
            throw new IllegalArgumentException("店铺未配置 Amazon sellerId");
        }
        Map<String, String> query = new TreeMap<>();
        query.put("marketplaceIds", marketplace.getMarketplaceId());
        query.put("includedData", joinOrDefault(request.getIncludedData(), "summaries"));
        put(query, "issueLocale", request.getIssueLocale());

        String path = "/listings/2021-08-01/items/"
                + UriUtils.encodePathSegment(sellerId, StandardCharsets.UTF_8) + "/"
                + UriUtils.encodePathSegment(request.getSku(), StandardCharsets.UTF_8);
        return URI.create(marketplace.getEndpoint() + path + "?" + buildQuery(query));
    }

    /** 按 RFC 3986 编码并排序查询参数。 */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /** 使用 UTF-8 对查询参数进行 RFC 3986 百分号编码。 */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 仅在值非空时写入可选查询参数。 */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /** 将请求数组转换为 Amazon 要求的逗号分隔参数。 */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /** 将空数组替换为 Amazon API 的默认数据集。 */
    private String joinOrDefault(List<String> values, String defaultValue) {
        String value = join(values);
        return isBlank(value) ? defaultValue : value;
    }

    /** 查询当前租户下的 Amazon 店铺。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** 解析请求国家代码对应的 Amazon Marketplace。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
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
