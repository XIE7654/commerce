package cn.iocoder.yudao.module.amazon.service.listings;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPatchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPutReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsRestrictionsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.sdk.listings.AmazonListingsApi;
import cn.iocoder.yudao.module.amazon.sdk.listings.AmazonListingsRequest;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Amazon Listings Items 服务实现。 */
@Service
public class AmazonListingsServiceImpl implements AmazonListingsService {

    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonListingsApi amazonListingsApi;

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> searchListingsItems(AmazonListingsSearchReqVO request) {
        AmazonListingsRequest sdkRequest = buildSearchSdkRequest(request);
        sdkRequest.setIncludedData(request.getIncludedData());
        sdkRequest.setIdentifiers(request.getIdentifiers());
        sdkRequest.setIdentifiersType(request.getIdentifiersType());
        sdkRequest.setVariationParentSku(request.getVariationParentSku());
        sdkRequest.setPackageHierarchySku(request.getPackageHierarchySku());
        sdkRequest.setCreatedAfter(request.getCreatedAfter());
        sdkRequest.setCreatedBefore(request.getCreatedBefore());
        sdkRequest.setLastUpdatedAfter(request.getLastUpdatedAfter());
        sdkRequest.setLastUpdatedBefore(request.getLastUpdatedBefore());
        sdkRequest.setWithIssueSeverity(request.getWithIssueSeverity());
        sdkRequest.setWithStatus(request.getWithStatus());
        sdkRequest.setWithoutStatus(request.getWithoutStatus());
        sdkRequest.setSortBy(request.getSortBy());
        sdkRequest.setSortOrder(request.getSortOrder());
        sdkRequest.setPageSize(request.getPageSize());
        sdkRequest.setPageToken(request.getPageToken());
        sdkRequest.setIssueLocale(request.getIssueLocale());
        return amazonListingsApi.searchItems(sdkRequest);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getListingsItem(AmazonListingsItemGetReqVO request) {
        return amazonListingsApi.getItem(buildItemSdkRequest(request));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> putListingsItem(AmazonListingsItemPutReqVO request) {
        AmazonListingsRequest sdkRequest = buildItemSdkRequest(request);
        sdkRequest.setProductType(request.getProductType());
        sdkRequest.setAttributes(request.getAttributes());
        sdkRequest.setRequirements(request.getRequirements());
        sdkRequest.setMode(request.getMode());
        return amazonListingsApi.putItem(sdkRequest);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> patchListingsItem(AmazonListingsItemPatchReqVO request) {
        AmazonListingsRequest sdkRequest = buildItemSdkRequest(request);
        sdkRequest.setProductType(request.getProductType());
        sdkRequest.setPatches(request.getPatches());
        sdkRequest.setMode(request.getMode());
        return amazonListingsApi.patchItem(sdkRequest);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> deleteListingsItem(AmazonListingsItemGetReqVO request) {
        return amazonListingsApi.deleteItem(buildItemSdkRequest(request));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getListingsRestrictions(AmazonListingsRestrictionsReqVO request) {
        AmazonListingsRequest sdkRequest = buildSdkRequest(request.getShopId(), request.getCountryCode());
        sdkRequest.setAsin(request.getAsin());
        sdkRequest.setConditionType(request.getConditionType());
        sdkRequest.setReasonLocale(request.getReasonLocale());
        sdkRequest.setProductType(request.getProductType());
        return amazonListingsApi.getRestrictions(sdkRequest);
    }

    /**
     * 将店铺和站点上下文转换为 SDK 请求；Seller access token 仅在服务层获取。
     *
     * @param shopId 店铺编号
     * @param countryCode 目标 Marketplace 国家代码
     * @return 包含授权、端点及卖家标识的 SDK 请求
     */
    private AmazonListingsRequest buildSdkRequest(Long shopId, String countryCode) {
        AmazonShopDO shop = requireShop(shopId);
        AmazonMarketplaceEnum marketplace = requireMarketplace(countryCode);
        AmazonListingsRequest sdkRequest = new AmazonListingsRequest();
        sdkRequest.setShopId(shop.getId());
        sdkRequest.setEndpoint(amazonMarketplaceProvider.getEndpoint(marketplace));
        sdkRequest.setAccessToken(amazonOAuthService.getSellerAccessToken(shop.getId()));
        sdkRequest.setCountryCode(marketplace.getCountryCode());
        sdkRequest.setMarketplaceId(marketplace.getMarketplaceId());
        sdkRequest.setSellerId(requireSellerId(shop.getSellerId()));
        return sdkRequest;
    }

    /**
     * 构造 Listings 搜索请求，支持同一销售区域内多个 Marketplace。
     *
     * <p>静态沙盒的 Listings Items Mock 需要同时传递 US 和 CA Marketplace；生产环境也可
     * 使用该能力批量查询同一区域站点。跨区域不能共用 SP-API 端点，故在此提前拦截。</p>
     *
     * @param request 搜索请求参数
     * @return 包含多个 Marketplace ID 的 SDK 请求
     */
    private AmazonListingsRequest buildSearchSdkRequest(AmazonListingsSearchReqVO request) {
        List<AmazonMarketplaceEnum> marketplaces = requireSearchMarketplaces(request.getCountryCode(), request.getCountryCodes());
        AmazonListingsRequest sdkRequest = buildSdkRequest(request.getShopId(), marketplaces.getFirst().getCountryCode());
        sdkRequest.setMarketplaceIds(marketplaces.stream().map(AmazonMarketplaceEnum::getMarketplaceId).toList());
        return sdkRequest;
    }

    /**
     * 复制单商品请求共有字段，确保 SDK 路径与查询参数使用相同的 SKU 和返回数据集。
     *
     * @param request 单商品 Listings 请求
     * @return 已包含店铺授权上下文的 SDK 请求
     */
    private AmazonListingsRequest buildItemSdkRequest(AmazonListingsItemGetReqVO request) {
        AmazonListingsRequest sdkRequest = buildSdkRequest(request.getShopId(), request.getCountryCode());
        sdkRequest.setSku(request.getSku());
        sdkRequest.setIncludedData(request.getIncludedData());
        sdkRequest.setIssueLocale(request.getIssueLocale());
        return sdkRequest;
    }

    /**
     * 查询当前租户下的店铺，保证 Listings 授权不能跨租户使用。
     *
     * @param shopId 店铺编号
     * @return 当前租户店铺
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 校验店铺已配置 Seller ID。
     *
     * @param sellerId 店铺 Seller ID
     * @return 非空 Seller ID
     */
    private String requireSellerId(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) {
            throw new IllegalArgumentException("店铺未配置 Amazon sellerId");
        }
        return sellerId;
    }

    /**
     * 解析请求国家代码对应的 Amazon Marketplace。
     *
     * @param countryCode Marketplace 国家代码
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
     * 解析 Listings 搜索的站点列表，并验证它们共用同一个 SP-API 区域端点。
     *
     * @param countryCode 兼容旧接口的单个国家代码
     * @param countryCodes 多站点国家代码
     * @return 已解析的 Marketplace 列表
     */
    private List<AmazonMarketplaceEnum> requireSearchMarketplaces(String countryCode, List<String> countryCodes) {
        if (countryCode != null && !countryCode.isBlank() && countryCodes != null && !countryCodes.isEmpty()) {
            throw new IllegalArgumentException("countryCode 与 countryCodes 不能同时传入");
        }
        List<String> requestedCodes = countryCodes == null || countryCodes.isEmpty()
                ? (countryCode == null || countryCode.isBlank() ? List.of() : List.of(countryCode)) : countryCodes;
        if (requestedCodes.isEmpty()) {
            throw new IllegalArgumentException("countryCode 或 countryCodes 至少传入一个");
        }
        List<AmazonMarketplaceEnum> marketplaces = new ArrayList<>();
        String salesRegion = null;
        for (String requestedCode : requestedCodes) {
            AmazonMarketplaceEnum marketplace = requireMarketplace(requestedCode);
            if (salesRegion == null) {
                salesRegion = marketplace.getSalesRegion();
            } else if (!salesRegion.equals(marketplace.getSalesRegion())) {
                throw new IllegalArgumentException("Listings 搜索的多个站点必须属于同一销售区域");
            }
            marketplaces.add(marketplace);
        }
        return marketplaces;
    }
}
