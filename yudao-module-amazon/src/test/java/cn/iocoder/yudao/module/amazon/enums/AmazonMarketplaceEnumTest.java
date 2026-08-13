package cn.iocoder.yudao.module.amazon.enums;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link AmazonMarketplaceEnum} 解析规则测试。
 */
class AmazonMarketplaceEnumTest {

    /** 验证历史店铺保存的销售区域可解析为 Orders API 所需的默认站点。 */
    @Test
    void fromSalesRegionUsesUsAsNorthAmericaDefaultMarketplace() {
        assertEquals(AmazonMarketplaceEnum.US, AmazonMarketplaceEnum.fromSalesRegion("NA"));
    }

    /** 验证 North America 区域返回全部站点 ID。 */
    @Test
    void getMarketplaceIdsBySalesRegionReturnsAllNorthAmericaMarketplaces() {
        assertEquals(List.of("A2EUQ1WTGCTBG2", "ATVPDKIKX0DER", "A1AM78C64UM0Y8", "A2Q3Y263D00KWC"),
                AmazonMarketplaceEnum.getMarketplaceIdsBySalesRegion("NA"));
    }

    /** 验证 Europe 区域返回全部站点 ID。 */
    @Test
    void getMarketplaceIdsBySalesRegionReturnsAllEuropeMarketplaces() {
        assertEquals(16, AmazonMarketplaceEnum.getMarketplaceIdsBySalesRegion("EU").size());
    }
}
