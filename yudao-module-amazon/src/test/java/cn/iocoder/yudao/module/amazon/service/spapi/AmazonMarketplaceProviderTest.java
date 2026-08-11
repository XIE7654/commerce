package cn.iocoder.yudao.module.amazon.service.spapi;

import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link AmazonMarketplaceProvider} 沙盒 SP-API 端点选择测试。
 */
class AmazonMarketplaceProviderTest {

    @Test
    void productionModeUsesProductionEndpoints() {
        AmazonMarketplaceProvider provider = createProvider(false);

        assertEquals("https://sellingpartnerapi-na.amazon.com", provider.getEndpoint(AmazonMarketplaceEnum.US));
        assertEquals("https://sellingpartnerapi-eu.amazon.com", provider.getEndpoint(AmazonMarketplaceEnum.DE));
        assertEquals("https://sellingpartnerapi-fe.amazon.com", provider.getEndpoint(AmazonMarketplaceEnum.JP));
    }

    @Test
    void sandboxModeUsesSandboxEndpoints() {
        AmazonMarketplaceProvider provider = createProvider(true);

        assertEquals("https://sandbox.sellingpartnerapi-na.amazon.com", provider.getEndpoint(AmazonMarketplaceEnum.US));
        assertEquals("https://sandbox.sellingpartnerapi-eu.amazon.com", provider.getEndpoint(AmazonMarketplaceEnum.DE));
        assertEquals("https://sandbox.sellingpartnerapi-fe.amazon.com", provider.getEndpoint(AmazonMarketplaceEnum.JP));
    }

    @Test
    void enumProvidesSandboxEndpointsBySalesRegion() {
        assertEquals("https://sandbox.sellingpartnerapi-na.amazon.com", AmazonMarketplaceEnum.US.getSandboxEndpoint());
        assertEquals("https://sandbox.sellingpartnerapi-eu.amazon.com", AmazonMarketplaceEnum.DE.getSandboxEndpoint());
        assertEquals("https://sandbox.sellingpartnerapi-fe.amazon.com", AmazonMarketplaceEnum.JP.getSandboxEndpoint());
    }

    private AmazonMarketplaceProvider createProvider(boolean sandboxMode) {
        AwsProperties properties = new AwsProperties();
        properties.setSandboxMode(sandboxMode);
        AmazonMarketplaceProvider provider = new AmazonMarketplaceProvider();
        ReflectionTestUtils.setField(provider, "properties", properties);
        return provider;
    }
}
