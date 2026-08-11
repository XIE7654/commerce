package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopWithMarketplacesDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.sdk.orders.AmazonOrdersApi;
import cn.iocoder.yudao.module.amazon.sdk.orders.AmazonOrdersRequest;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** {@link AmazonOrdersServiceImpl} 的店铺参与 Marketplace 解析测试。 */
class AmazonOrdersServiceImplTest {

    @Test
    void getOrdersUsesRequestedShopsParticipatingMarketplace() {
        AmazonOrdersServiceImpl service = new AmazonOrdersServiceImpl();
        CapturingOrdersApi ordersApi = new CapturingOrdersApi();
        ReflectionTestUtils.setField(service, "shopMapper", shopMapper());
        ReflectionTestUtils.setField(service, "amazonOAuthService", oauthService());
        ReflectionTestUtils.setField(service, "amazonMarketplaceProvider", marketplaceProvider());
        ReflectionTestUtils.setField(service, "amazonOrdersApi", ordersApi);

        AmazonOrdersListReqVO request = new AmazonOrdersListReqVO();
        request.setShopId(2L);
        request.setCreatedAfter("2026-08-01T00:00:00Z");
        service.getOrders(request);

        assertEquals(2L, ordersApi.request.getShopId());
        assertEquals("https://sellingpartnerapi-na.amazon.com", ordersApi.request.getEndpoint());
        assertEquals("ATVPDKIKX0DER", ordersApi.request.getMarketplaceId());
        assertEquals("ATVPDKIKX0DER", ordersApi.request.getQuery().get("MarketplaceIds"));
        assertEquals("seller-access-token", ordersApi.request.getAccessToken());
    }

    /** 创建返回两个启用店铺的 Mapper，以验证服务按请求店铺编号筛选。 */
    private AmazonShopMapper shopMapper() {
        return (AmazonShopMapper) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{AmazonShopMapper.class},
                (proxy, method, args) -> "selectEnabledWithMarketplaces".equals(method.getName())
                        ? List.of(shopWithMarketplace(1L, "A2EUQ1WTGCTBG2", "CA"),
                        shopWithMarketplace(2L, "ATVPDKIKX0DER", "US")) : null);
    }

    /** 创建已参与指定 Marketplace 的店铺关联数据。 */
    private AmazonShopWithMarketplacesDO shopWithMarketplace(Long shopId, String marketplaceId, String countryCode) {
        AmazonShopDO shop = new AmazonShopDO();
        shop.setId(shopId);
        shop.setRegion("NA");
        shop.setMarketplaceId("legacy-marketplace-id");
        AmazonShopMarketplaceDO participation = new AmazonShopMarketplaceDO();
        participation.setShopId(shopId);
        participation.setMarketplaceId(marketplaceId);
        participation.setCountryCode(countryCode);
        participation.setIsParticipating(true);
        AmazonShopWithMarketplacesDO result = new AmazonShopWithMarketplacesDO();
        result.setShop(shop);
        result.setParticipations(List.of(participation));
        return result;
    }

    /** 创建仅返回固定 Seller Token 的 OAuth Service 代理。 */
    private AmazonOAuthService oauthService() {
        return (AmazonOAuthService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{AmazonOAuthService.class},
                (proxy, method, args) -> "getSellerAccessToken".equals(method.getName()) ? "seller-access-token" : null);
    }

    /** 创建生产环境端点提供器，避免测试依赖 Spring 容器。 */
    private AmazonMarketplaceProvider marketplaceProvider() {
        AmazonMarketplaceProvider provider = new AmazonMarketplaceProvider();
        ReflectionTestUtils.setField(provider, "properties", new AwsProperties());
        return provider;
    }

    /** 捕获服务层组装的 SDK 请求，避免测试发起真实 Amazon HTTP 调用。 */
    private static final class CapturingOrdersApi extends AmazonOrdersApi {

        private AmazonOrdersRequest request;

        @Override
        public AmazonApiResponse<Map<String, Object>> getOrders(AmazonOrdersRequest request) {
            this.request = request;
            return new AmazonApiResponse<>(200, Map.of(), null);
        }
    }
}
