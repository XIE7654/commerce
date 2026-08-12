package cn.iocoder.yudao.module.amazon.sdk;

import org.junit.jupiter.api.Test;
import software.amazon.spapi.ApiException;
import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.SellingPartnerAPIAA.LWAException;
import software.amazon.spapi.api.listings.items.v2021_08_01.ListingsApi;
import software.amazon.spapi.api.orders.v0.OrdersV0Api;
import software.amazon.spapi.api.sellers.v1.SellersApi;
import software.amazon.spapi.models.listings.items.v2021_08_01.Item;
import software.amazon.spapi.models.orders.v0.GetOrdersResponse;
import software.amazon.spapi.models.sellers.v1.GetMarketplaceParticipationsResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Amazon 官方 SP-API SDK 沙盒集成测试。
 *
 * <p>该测试默认跳过，避免普通构建依赖外部网络和真实凭据。设置
 * {@code AMAZON_SPAPI_SANDBOX_ENABLED=true} 以及三项凭据后，可手工执行真实沙盒请求。</p>
 */
class AmazonSpApiSandboxIntegrationTest {

    private static final String SANDBOX_ENDPOINT = "https://sandbox.sellingpartnerapi-na.amazon.com";
    private static final String LWA_ENDPOINT = "https://api.amazon.com/auth/o2/token";
    private static final String SANDBOX_MARKETPLACE_ID = "ATVPDKIKX0DER";
    private static final String SANDBOX_LISTING_SKU = "GM-ZDPI-9B4E";

    /**
     * 使用官方 SDK 调用 Sellers API 沙盒端点，验证凭据、端点和 SDK 请求链路均可用。
     *
     * @throws ApiException Amazon 沙盒返回非成功响应时抛出
     */
    @Test
    void shouldCallSellersApiInSandbox() throws ApiException, LWAException {
//        assumeTrue(Boolean.parseBoolean(System.getenv("AMAZON_SPAPI_SANDBOX_ENABLED")),
//                "设置 AMAZON_SPAPI_SANDBOX_ENABLED=true 后执行沙盒集成测试");
        LWAAuthorizationCredentials credentials = credentials();
        SellersApi sellersApi = new SellersApi.Builder()
                .lwaAuthorizationCredentials(credentials)
                .endpoint(SANDBOX_ENDPOINT)
                .build();

        GetMarketplaceParticipationsResponse response = sellersApi.getMarketplaceParticipations();
        System.out.println("getMarketplaceParticipations response: " + response);

        assertNotNull(response, "沙盒 Sellers API 应返回响应");
    }

    /**
     * 调用 Listings Items 沙盒只读接口，验证商品查询的 SDK 请求链路。
     *
     * @throws ApiException Amazon 沙盒返回非成功响应时抛出
     * @throws LWAException LWA 授权失败时抛出
     */
    @Test
    void shouldGetListingsItemInSandbox() throws ApiException, LWAException {
        String sellerId = "test";
        ListingsApi listingsApi = new ListingsApi.Builder()
                .lwaAuthorizationCredentials(credentials())
                .endpoint(SANDBOX_ENDPOINT)
                .build();

        Item item = listingsApi.getListingsItem(sellerId, SANDBOX_LISTING_SKU, List.of(SANDBOX_MARKETPLACE_ID),
                "en_US", List.of("summaries"));
        System.out.println("getListingsItem response: " + item);
        assertNotNull(item, "沙盒 Listings Items API 应返回响应");
    }

    /**
     * 调用 Orders v0 沙盒订单列表接口，使用 Amazon 约定的测试时间值获取静态响应。
     *
     * @throws ApiException Amazon 沙盒返回非成功响应时抛出
     * @throws LWAException LWA 授权失败时抛出
     */
    @Test
    void shouldGetOrdersInSandbox() throws ApiException, LWAException {
        OrdersV0Api ordersApi = new OrdersV0Api.Builder()
                .lwaAuthorizationCredentials(credentials())
                .endpoint(SANDBOX_ENDPOINT)
                .build();
        GetOrdersResponse response = ordersApi.getOrders(List.of(SANDBOX_MARKETPLACE_ID), "TEST_CASE_200",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null);
        System.out.println("getOrders response: " + response);
        assertNotNull(response, "沙盒 Orders API 应返回响应");
    }

    /**
     * 构造各沙盒测试共享的 LWA 授权凭据。
     *
     * @return 配置完整的 LWA 授权凭据
     */
    private LWAAuthorizationCredentials credentials() {
        return LWAAuthorizationCredentials.builder()
                .clientId(requiredEnv("AMAZON_SPAPI_CLIENT_ID"))
                .clientSecret(requiredEnv("AMAZON_SPAPI_CLIENT_SECRET"))
                .refreshToken(requiredEnv("AMAZON_SPAPI_REFRESH_TOKEN"))
                .endpoint(LWA_ENDPOINT)
                .build();
    }

    /**
     * 读取集成测试凭据；缺失时跳过测试而不是让普通单元测试构建失败。
     *
     * @param name 环境变量名称
     * @return 非空环境变量值
     */
    private String requiredEnv(String name) {
        String value = System.getenv(name);
        assumeTrue(value != null && !value.isBlank(), "缺少环境变量 " + name + "，跳过沙盒集成测试");
        return value;
    }
}
