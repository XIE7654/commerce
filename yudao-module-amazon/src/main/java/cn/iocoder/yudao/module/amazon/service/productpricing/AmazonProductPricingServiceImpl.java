package cn.iocoder.yudao.module.amazon.service.productpricing;

import cn.iocoder.yudao.module.amazon.controller.admin.productpricing.vo.AmazonProductPricingReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Product Pricing API 服务实现。 */
@Service
public class AmazonProductPricingServiceImpl implements AmazonProductPricingService {

    private static final String PRICING_V0_PATH = "/products/pricing/v0";

    @Resource
    private AmazonProductsService productsService;
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> featured(AmazonProductsReqVO request) {
        return productsService.getFeaturedOfferExpectedPriceBatch(request);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> competitive(AmazonProductsReqVO request) {
        return productsService.getCompetitiveSummary(request);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getPricing(AmazonProductPricingReqVO request) {
        return getPrice(request, "/price", "getPricing", "pricing", true);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getCompetitivePricing(AmazonProductPricingReqVO request) {
        return getPrice(request, "/competitivePrice", "getCompetitivePricing", "competitive-pricing", false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getListingOffers(AmazonProductPricingReqVO request) {
        String sellerSku = requireText(request.getSellerSku(), "sellerSku");
        return getOffers(request, "/listings/" + encodePath(sellerSku) + "/offers", "getListingOffers", "listing-offers");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getItemOffers(AmazonProductPricingReqVO request) {
        String asin = requireText(request.getAsin(), "asin");
        return getOffers(request, "/items/" + encodePath(asin) + "/offers", "getItemOffers", "item-offers");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getItemOffersBatch(AmazonProductPricingReqVO request) {
        return postBatch(request, "/batches/products/pricing/v0/itemOffers", "getItemOffersBatch", "item-offers-batch");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getListingOffersBatch(AmazonProductPricingReqVO request) {
        return postBatch(request, "/batches/products/pricing/v0/listingOffers", "getListingOffersBatch", "listing-offers-batch");
    }

    /**
     * 查询价格或竞争价格，并验证标识类型与所传标识符相匹配。
     *
     * @param request V0 价格查询参数
     * @param path API 相对路径
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档名称
     * @param includeOfferFields 是否追加价格接口专属参数
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> getPrice(AmazonProductPricingReqVO request, String path, String operationName,
                                         String storageName, boolean includeOfferFields) {
        String itemType = requireText(request.getItemType(), "itemType");
        boolean useAsin = "Asin".equals(itemType);
        boolean useSku = "Sku".equals(itemType);
        if (!useAsin && !useSku) {
            throw new IllegalArgumentException("itemType 仅支持 Asin 或 Sku");
        }
        if ((useAsin && (isEmpty(request.getAsins()) || !isEmpty(request.getSkus())))
                || (useSku && (isEmpty(request.getSkus()) || !isEmpty(request.getAsins())))) {
            throw new IllegalArgumentException("itemType 必须与唯一的 asins 或 skus 参数匹配");
        }
        Map<String, String> query = marketplaceQuery(request);
        query.put("ItemType", itemType);
        put(query, "Asins", join(request.getAsins()));
        put(query, "Skus", join(request.getSkus()));
        if (includeOfferFields) {
            put(query, "ItemCondition", request.getItemCondition());
            put(query, "OfferType", request.getOfferType());
        } else {
            put(query, "CustomerType", request.getCustomerType());
        }
        return get(request, path, query, operationName, storageName);
    }

    /**
     * 查询单个 SKU 或 ASIN 的最低报价；商品成色为 Amazon V0 的必填条件。
     *
     * @param request V0 报价查询参数
     * @param path API 相对路径
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> getOffers(AmazonProductPricingReqVO request, String path, String operationName,
                                          String storageName) {
        Map<String, String> query = marketplaceQuery(request);
        query.put("ItemCondition", requireText(request.getItemCondition(), "itemCondition"));
        put(query, "CustomerType", request.getCustomerType());
        return get(request, path, query, operationName, storageName);
    }

    /**
     * 提交官方批量报价请求体。
     *
     * @param request 店铺、站点和批量请求体
     * @param path API 绝对路径
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> postBatch(AmazonProductPricingReqVO request, String path, String operationName,
                                          String storageName) {
        if (isEmptyBody(request.getBody())) {
            throw new IllegalArgumentException("body 不能为空");
        }
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + path);
        return amazonSellingPartnerClient.mutateByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), HttpMethod.POST,
                request.getBody(), AmazonApiCategory.PRODUCT_PRICING, operationName, storageName, shop.getId(),
                request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 调用 Product Pricing V0 GET 接口并归档响应。
     *
     * @param request 店铺和站点参数
     * @param path API 相对路径
     * @param query 查询参数
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> get(AmazonProductPricingReqVO request, String path, Map<String, String> query,
                                    String operationName, String storageName) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + PRICING_V0_PATH + path + "?" + buildQuery(query));
        return amazonSellingPartnerClient.getByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                AmazonApiCategory.PRODUCT_PRICING, operationName, storageName, shop.getId(), request.getCountryCode(),
                marketplace.getMarketplaceId());
    }

    /** 构造 V0 所需的可信 MarketplaceId 查询参数。 */
    private Map<String, String> marketplaceQuery(AmazonProductPricingReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("MarketplaceId", requireMarketplace(request.getCountryCode()).getMarketplaceId());
        return query;
    }

    /** 查询当前租户的 Amazon 店铺，确保授权令牌不会跨租户使用。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** 解析国家代码对应的站点配置。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /** 验证并返回必填文本参数。 */
    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " 不能为空");
        }
        return value;
    }

    /** 将列表转换为 Amazon V0 使用的逗号分隔参数。 */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /** 仅加入非空的可选查询参数。 */
    private void put(Map<String, String> query, String key, String value) {
        if (value != null && !value.isBlank()) {
            query.put(key, value);
        }
    }

    /** 按 RFC 3986 构造查询字符串，保证 SKU 中的特殊字符正确编码。 */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /** 编码查询参数中的保留字符。 */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 编码单一路径段，防止 SKU 或 ASIN 改变 API 路由。 */
    private String encodePath(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    /** 判断列表是否为空。 */
    private boolean isEmpty(List<?> values) {
        return values == null || values.isEmpty();
    }

    /** 判断批量请求体是否为空对象或空数组。 */
    private boolean isEmptyBody(Object body) {
        if (body == null) {
            return true;
        }
        if (body instanceof Map<?, ?> map) {
            return map.isEmpty();
        }
        return body instanceof Collection<?> collection && collection.isEmpty();
    }
}
