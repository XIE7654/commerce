package cn.iocoder.yudao.module.amazon.service.products;

import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
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

/** Amazon Products 相关 API 服务实现。 */
@Service
public class AmazonProductsServiceImpl implements AmazonProductsService {

    private static final String CATALOG_PATH = "/catalog/2022-04-01/items";
    private static final String CATALOG_V0_CATEGORIES_PATH = "/catalog/v0/categories";
    private static final String PRICING_PATH = "/batches/products/pricing/2022-05-01";
    private static final String FEES_PATH = "/products/fees/v0";
    private static final String PRODUCT_TYPES_PATH = "/definitions/2020-09-01/productTypes";

    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> searchCatalogItems(AmazonProductsReqVO request) {
        validateCatalogSearch(request);
        Map<String, String> query = catalogQuery(request);
        put(query, "identifiers", join(request.getIdentifiers()));
        put(query, "identifiersType", request.getIdentifiersType());
        put(query, "keywords", join(request.getKeywords()));
        put(query, "brandNames", join(request.getBrandNames()));
        put(query, "classificationIds", join(request.getClassificationIds()));
        put(query, "pageSize", asString(request.getPageSize()));
        put(query, "pageToken", request.getPageToken());
        put(query, "keywordsLocale", request.getKeywordsLocale());
        return get(request, CATALOG_PATH, query, AmazonApiCategory.CATALOG_ITEMS, "searchCatalogItems", "catalog-items");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getCatalogItem(AmazonProductsReqVO request) {
        String asin = requireText(request.getAsin(), "asin");
        return get(request, CATALOG_PATH + "/" + encodePath(asin), catalogQuery(request), AmazonApiCategory.CATALOG_ITEMS,
                "getCatalogItem", "catalog-item");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listCatalogCategories(AmazonProductsReqVO request) {
        boolean hasAsin = !isBlank(request.getAsin());
        boolean hasSellerSku = !isBlank(request.getSellerSku());
        if (hasAsin == hasSellerSku) {
            throw new IllegalArgumentException("asin 和 sellerSku 必须且只能传入一项");
        }
        Map<String, String> query = new LinkedHashMap<>();
        // v0 API 的查询键采用单数 MarketplaceId，不能复用 2022-04-01 的 marketplaceIds。
        query.put("MarketplaceId", requireMarketplace(request.getCountryCode()).getMarketplaceId());
        put(query, "ASIN", request.getAsin());
        put(query, "SellerSKU", request.getSellerSku());
        return get(request, CATALOG_V0_CATEGORIES_PATH, query, AmazonApiCategory.CATALOG_ITEMS,
                "listCatalogCategories", "catalog-categories");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getFeaturedOfferExpectedPriceBatch(AmazonProductsReqVO request) {
        return post(request, PRICING_PATH + "/offer/featuredOfferExpectedPrice", AmazonApiCategory.PRODUCT_PRICING,
                "getFeaturedOfferExpectedPriceBatch", "featured-offer-expected-price");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getCompetitiveSummary(AmazonProductsReqVO request) {
        return post(request, PRICING_PATH + "/items/competitiveSummary", AmazonApiCategory.PRODUCT_PRICING,
                "getCompetitiveSummary", "competitive-summary");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getMyFeesEstimateForSku(AmazonProductsReqVO request) {
        String sellerSku = requireText(request.getSellerSku(), "sellerSku");
        return post(request, FEES_PATH + "/listings/" + encodePath(sellerSku) + "/feesEstimate", AmazonApiCategory.PRODUCT_FEES,
                "getMyFeesEstimateForSKU", "fees-estimate-sku");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getMyFeesEstimateForAsin(AmazonProductsReqVO request) {
        String asin = requireText(request.getAsin(), "asin");
        return post(request, FEES_PATH + "/items/" + encodePath(asin) + "/feesEstimate", AmazonApiCategory.PRODUCT_FEES,
                "getMyFeesEstimateForASIN", "fees-estimate-asin");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getMyFeesEstimates(AmazonProductsReqVO request) {
        return post(request, FEES_PATH + "/feesEstimate", AmazonApiCategory.PRODUCT_FEES,
                "getMyFeesEstimates", "fees-estimates");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> searchDefinitionsProductTypes(AmazonProductsReqVO request) {
        Map<String, String> query = marketplaceQuery(request);
        put(query, "keywords", join(request.getKeywords()));
        put(query, "itemName", request.getItemName());
        put(query, "locale", request.getLocale());
        put(query, "searchLocale", request.getSearchLocale());
        return get(request, PRODUCT_TYPES_PATH, query, AmazonApiCategory.PRODUCT_TYPE_DEFINITIONS,
                "searchDefinitionsProductTypes", "product-types");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getDefinitionsProductType(AmazonProductsReqVO request) {
        String productType = requireText(request.getProductType(), "productType");
        Map<String, String> query = marketplaceQuery(request);
        // sellerId 只能取当前租户店铺配置，避免调用方以其他卖家身份读取卖家专属定义。
        AmazonShopDO shop = requireShop(request.getShopId());
        put(query, "sellerId", shop.getSellerId());
        put(query, "productTypeVersion", request.getProductTypeVersion());
        put(query, "requirements", request.getRequirements());
        put(query, "requirementsEnforced", request.getRequirementsEnforced());
        put(query, "locale", request.getLocale());
        put(query, "parentageLevel", request.getParentageLevel());
        return get(request, PRODUCT_TYPES_PATH + "/" + encodePath(productType), query,
                AmazonApiCategory.PRODUCT_TYPE_DEFINITIONS, "getDefinitionsProductType", "product-type-definition");
    }

    /**
     * 执行 Products 的 GET 请求，并统一注入当前店铺的授权令牌和站点归档信息。
     *
     * @param request 店铺和站点请求参数
     * @param path SP-API 路径
     * @param query 查询参数
     * @param category 响应归档分类
     * @param operationName Amazon 操作名称
     * @param storageName 归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> get(AmazonProductsReqVO request, String path, Map<String, String> query,
                                    AmazonApiCategory category, String operationName, String storageName) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + path + "?" + buildQuery(query));
        return amazonSellingPartnerClient.getByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), category,
                operationName, storageName, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 执行 Products 的 POST 请求；请求体保持官方模型结构，避免应用层丢失 Amazon 后续扩展字段。
     *
     * @param request 店铺、站点和 Amazon 请求体
     * @param path SP-API 路径
     * @param category 响应归档分类
     * @param operationName Amazon 操作名称
     * @param storageName 归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> post(AmazonProductsReqVO request, String path, AmazonApiCategory category,
                                     String operationName, String storageName) {
        if (isEmptyBody(request.getBody())) {
            throw new IllegalArgumentException("body 不能为空");
        }
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + path);
        return amazonSellingPartnerClient.mutateByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), HttpMethod.POST,
                request.getBody(), category, operationName, storageName, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 构造 Catalog Items 共用的强制站点与可选展示参数。
     *
     * @param request Catalog 查询参数
     * @return 查询参数集合
     */
    private Map<String, String> catalogQuery(AmazonProductsReqVO request) {
        Map<String, String> query = marketplaceQuery(request);
        put(query, "includedData", join(request.getIncludedData()));
        put(query, "locale", request.getLocale());
        return query;
    }

    /**
     * 构造必须包含当前国家对应 marketplaceId 的查询参数。
     *
     * @param request 店铺和国家代码参数
     * @return 带 marketplaceIds 的查询参数集合
     */
    private Map<String, String> marketplaceQuery(AmazonProductsReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        query.put("marketplaceIds", requireMarketplace(request.getCountryCode()).getMarketplaceId());
        return query;
    }

    /**
     * 校验 Catalog 搜索的标识符、关键字互斥关系，避免 Amazon 将歧义请求按非预期条件处理。
     *
     * @param request Catalog 搜索参数
     */
    private void validateCatalogSearch(AmazonProductsReqVO request) {
        boolean hasIdentifiers = !isEmpty(request.getIdentifiers());
        boolean hasKeywords = !isEmpty(request.getKeywords());
        if (hasIdentifiers == hasKeywords) {
            throw new IllegalArgumentException("identifiers 和 keywords 必须且只能传入一项");
        }
        if (hasIdentifiers != !isBlank(request.getIdentifiersType())) {
            throw new IllegalArgumentException("identifiers 和 identifiersType 必须同时传入");
        }
        if (hasIdentifiers && (!isEmpty(request.getBrandNames()) || !isEmpty(request.getClassificationIds()))) {
            throw new IllegalArgumentException("identifiers 不能与 brandNames 或 classificationIds 同时传入");
        }
    }

    /**
     * 查询当前租户下的店铺，依赖租户拦截器隔离不同租户的数据。
     *
     * @param shopId 店铺编号
     * @return 当前租户的店铺配置
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 解析国家代码对应的 Amazon Marketplace 配置。
     *
     * @param countryCode 国家代码
     * @return Marketplace 配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /**
     * 构造 RFC 3986 编码的查询字符串。
     *
     * @param query 查询参数集合
     * @return 编码后的查询字符串
     */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /**
     * 编码查询参数，空格必须使用 %20 以符合 Amazon 请求规范。
     *
     * @param value 原始参数值
     * @return RFC 3986 编码后的参数值
     */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /**
     * 编码单一路径段，防止 SKU、ASIN 或商品类型中的特殊字符改变 API 路由。
     *
     * @param value 原始路径段
     * @return 编码后的路径段
     */
    private String encodePath(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    /**
     * 仅在参数非空时写入可选查询字段。
     *
     * @param query 查询参数集合
     * @param key 字段名称
     * @param value 字段值
     */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /**
     * 将列表转为 Amazon 要求的逗号分隔查询值。
     *
     * @param values 列表参数
     * @return 逗号分隔值；空列表返回 {@code null}
     */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /**
     * 验证并返回必填文本字段。
     *
     * @param value 字段值
     * @param fieldName 字段名称
     * @return 非空字段值
     */
    private String requireText(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " 不能为空");
        }
        return value;
    }

    /**
     * 将数值参数转换为查询字符串。
     *
     * @param value 数值参数
     * @return 字符串值；空值返回 {@code null}
     */
    private String asString(Integer value) {
        return value == null ? null : value.toString();
    }

    /**
     * 判断文本是否为空白。
     *
     * @param value 待判断文本
     * @return 为空或仅含空白时返回 {@code true}
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * 判断列表是否为空。
     *
     * @param values 待判断列表
     * @return 为 {@code null} 或没有元素时返回 {@code true}
     */
    private boolean isEmpty(List<?> values) {
        return values == null || values.isEmpty();
    }

    /**
     * 判断 Amazon 原始请求体是否为空对象或空数组。
     *
     * @param body 原始 JSON 映射后的请求体
     * @return 请求体为空时返回 {@code true}
     */
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
