package cn.iocoder.yudao.module.amazon.service.listings;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiSdkFactory;
import com.amazon.SellingPartnerAPIAA.LWAException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.api.listings.items.v2021_08_01.ListingsApi;
import software.amazon.spapi.models.listings.items.v2021_08_01.Item;
import software.amazon.spapi.models.listings.items.v2021_08_01.ItemSearchResults;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/** Amazon Listings Items 官方 SDK 服务实现。 */
@Service
public class AmazonListingsServiceImpl implements AmazonListingsService {
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSpApiSdkFactory amazonSpApiSdkFactory;

    /** {@inheritDoc} */
    @Override
    public ItemSearchResults searchListingsItems(AmazonListingsSearchReqVO request) throws ApiException, LWAException {
        List<AmazonMarketplaceEnum> marketplaces = requireSearchMarketplaces(request.getCountryCode(), request.getCountryCodes());
        AmazonShopDO shop = requireShop(request.getShopId());
        return listingsApi(shop, marketplaces.getFirst()).searchListingsItems(requireSellerId(shop.getSellerId()),
                marketplaces.stream().map(AmazonMarketplaceEnum::getMarketplaceId).toList(), request.getIssueLocale(),
                request.getIncludedData(), request.getIdentifiers(), request.getIdentifiersType(), request.getVariationParentSku(),
                request.getPackageHierarchySku(), parseDateTime(request.getCreatedAfter()), parseDateTime(request.getCreatedBefore()),
                parseDateTime(request.getLastUpdatedAfter()), parseDateTime(request.getLastUpdatedBefore()), request.getWithIssueSeverity(),
                request.getWithStatus(), request.getWithoutStatus(), request.getSortBy(), request.getSortOrder(), request.getPageSize(),
                request.getPageToken());
    }

    /** {@inheritDoc} */
    @Override
    public Item getListingsItem(AmazonListingsItemGetReqVO request) throws ApiException, LWAException {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        return listingsApi(shop, marketplace).getListingsItem(requireSellerId(shop.getSellerId()), request.getSku(),
                List.of(marketplace.getMarketplaceId()), request.getIssueLocale(), request.getIncludedData());
    }

    /** 构造指定店铺和区域的官方 Listings SDK 客户端。 */
    private ListingsApi listingsApi(AmazonShopDO shop, AmazonMarketplaceEnum marketplace) {
        return new ListingsApi.Builder()
                .lwaAuthorizationCredentials(amazonSpApiSdkFactory.credentials(shop.getSellerRefreshToken()))
                .endpoint(amazonMarketplaceProvider.getEndpoint(marketplace))
                .build();
    }

    /** 将 ISO 8601 时间字符串转换为 SDK 所需时间类型。 */
    private OffsetDateTime parseDateTime(String value) {
        return value == null || value.isBlank() ? null : OffsetDateTime.parse(value);
    }

    /** 查询当前租户下的店铺。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        return shop;
    }

    /** 校验店铺已配置 Seller ID。 */
    private String requireSellerId(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) throw new IllegalArgumentException("店铺未配置 Amazon sellerId");
        return sellerId;
    }

    /** 解析国家代码对应的 Amazon Marketplace。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        return marketplace;
    }

    /** 解析搜索站点并限制其必须使用同一 SP-API 区域端点。 */
    private List<AmazonMarketplaceEnum> requireSearchMarketplaces(String countryCode, List<String> countryCodes) {
        if (countryCode != null && !countryCode.isBlank() && countryCodes != null && !countryCodes.isEmpty()) {
            throw new IllegalArgumentException("countryCode 与 countryCodes 不能同时传入");
        }
        List<String> codes = countryCodes == null || countryCodes.isEmpty()
                ? (countryCode == null || countryCode.isBlank() ? List.of() : List.of(countryCode)) : countryCodes;
        if (codes.isEmpty()) throw new IllegalArgumentException("countryCode 或 countryCodes 至少传入一个");
        List<AmazonMarketplaceEnum> marketplaces = new ArrayList<>();
        String region = null;
        for (String code : codes) {
            AmazonMarketplaceEnum marketplace = requireMarketplace(code);
            if (region != null && !region.equals(marketplace.getSalesRegion())) {
                throw new IllegalArgumentException("Listings 搜索的多个站点必须属于同一销售区域");
            }
            region = marketplace.getSalesRegion();
            marketplaces.add(marketplace);
        }
        return marketplaces;
    }
}
