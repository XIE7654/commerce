package cn.iocoder.yudao.module.amazon.service.spapi;

import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import software.amazon.spapi.api.listings.items.v2021_08_01.ListingsApi;
import software.amazon.spapi.api.orders.v0.OrdersV0Api;

/** 官方 SP-API SDK 客户端授权配置工厂。 */
@Component
public class AmazonSpApiSdkFactory {

    @Resource
    private AwsProperties awsProperties;
    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonShopMapper amazonShopMapper;

    /**
     * 构造店铺级 LWA 凭据，官方 SDK 会负责获取和缓存 access token。
     *
     * @param refreshToken 店铺 Seller refresh token
     * @return 官方 SDK 使用的 LWA 凭据
     */
    public LWAAuthorizationCredentials credentials(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("店铺未配置 Amazon Seller refresh token");
        }
        return LWAAuthorizationCredentials.builder()
                .clientId(awsProperties.getClientId())
                .clientSecret(awsProperties.getClientSecret())
                .refreshToken(refreshToken)
                .endpoint(awsProperties.getStoreTokenUrl())
                .build();
    }

    /**
     * 根据店铺编号构造官方 Orders SDK 客户端。
     *
     * @param shopId Amazon 店铺编号
     * @return 已配置店铺授权及区域端点的 Orders 客户端
     */
    public OrdersV0Api createOrdersApi(Long shopId) {
        AmazonShopDO shop = requireShop(shopId);
        return new OrdersV0Api.Builder()
                .lwaAuthorizationCredentials(credentials(shop.getSellerRefreshToken()))
                .endpoint(amazonMarketplaceProvider.getEndpoint(requireMarketplace(shop.getRegion())))
                .build();
    }

    /**
     * 根据店铺编号构造官方 Listings SDK 客户端。
     *
     * @param shopId Amazon 店铺编号
     * @return 已配置店铺授权及区域端点的 Listings 客户端
     */
    public ListingsApi createListingsApi(Long shopId) {
        AmazonShopDO shop = requireShop(shopId);
        return new ListingsApi.Builder()
                .lwaAuthorizationCredentials(credentials(shop.getSellerRefreshToken()))
                .endpoint(amazonMarketplaceProvider.getEndpoint(requireMarketplace(shop.getRegion())))
                .build();
    }

    /**
     * 查询当前租户下的店铺，避免客户端使用不存在或越权店铺的凭据。
     *
     * @param shopId Amazon 店铺编号
     * @return 店铺授权信息
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 解析店铺销售区域，用于选择 SDK 请求端点。
     *
     * @param regionOrCountry 店铺保存的销售区域或国家代码
     * @return 区域对应的 Marketplace 配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String regionOrCountry) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(regionOrCountry);
        if (marketplace == null) {
            marketplace = AmazonMarketplaceEnum.fromSalesRegion(regionOrCountry);
        }
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 销售区域或国家代码: " + regionOrCountry);
        }
        return marketplace;
    }
}
