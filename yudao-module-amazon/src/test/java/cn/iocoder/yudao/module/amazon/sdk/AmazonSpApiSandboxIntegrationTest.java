package cn.iocoder.yudao.module.amazon.sdk;

import org.junit.jupiter.api.Test;
import software.amazon.spapi.ApiException;
import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import com.amazon.SellingPartnerAPIAA.LWAException;
import software.amazon.spapi.api.sellers.v1.SellersApi;
import software.amazon.spapi.models.sellers.v1.GetMarketplaceParticipationsResponse;

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

    /**
     * 使用官方 SDK 调用 Sellers API 沙盒端点，验证凭据、端点和 SDK 请求链路均可用。
     *
     * @throws ApiException Amazon 沙盒返回非成功响应时抛出
     */
    @Test
    void shouldCallSellersApiInSandbox() throws ApiException, LWAException {
//        assumeTrue(Boolean.parseBoolean(System.getenv("AMAZON_SPAPI_SANDBOX_ENABLED")),
//                "设置 AMAZON_SPAPI_SANDBOX_ENABLED=true 后执行沙盒集成测试");
        String clientId = requiredEnv("AMAZON_SPAPI_CLIENT_ID");
        String clientSecret = requiredEnv("AMAZON_SPAPI_CLIENT_SECRET");
        String refreshToken = requiredEnv("AMAZON_SPAPI_REFRESH_TOKEN");

        LWAAuthorizationCredentials credentials = LWAAuthorizationCredentials.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .refreshToken(refreshToken)
                .endpoint(LWA_ENDPOINT)
                .build();
        SellersApi sellersApi = new SellersApi.Builder()
                .lwaAuthorizationCredentials(credentials)
                .endpoint(SANDBOX_ENDPOINT)
                .build();

        GetMarketplaceParticipationsResponse response = sellersApi.getMarketplaceParticipations();
        assertNotNull(response, "沙盒 Sellers API 应返回响应");
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
