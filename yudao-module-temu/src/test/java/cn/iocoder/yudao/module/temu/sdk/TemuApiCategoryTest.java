package cn.iocoder.yudao.module.temu.sdk;

import cn.iocoder.yudao.module.temu.enums.TemuApiCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Temu API 文档菜单分类测试。 */
class TemuApiCategoryTest {

    /** 验证已接入接口及相邻文档分类可归档至正确目录。 */
    @Test
    void shouldClassifyApiTypesByDocumentationMenu() {
        assertEquals(TemuApiCategory.AUTHORIZATION, TemuApiCategory.fromApiType("bg.open.accesstoken.info.get"));
        assertEquals(TemuApiCategory.PRODUCT, TemuApiCategory.fromApiType("bg.local.goods.cats.get"));
        assertEquals(TemuApiCategory.PRICE, TemuApiCategory.fromApiType("bg.local.goods.priceorder.query"));
        assertEquals(TemuApiCategory.ORDER, TemuApiCategory.fromApiType("bg.order.list.v2.get"));
        assertEquals(TemuApiCategory.FULFILLMENT, TemuApiCategory.fromApiType("bg.logistics.shipment.create"));
        assertEquals(TemuApiCategory.RETURN_AND_REFUND,
                TemuApiCategory.fromApiType("temu.aftersales.refund.issue"));
        assertEquals(TemuApiCategory.PROMOTION, TemuApiCategory.fromApiType("bg.promotion.activity.query"));
        assertEquals(TemuApiCategory.ADS, TemuApiCategory.fromApiType("temu.searchrec.ad.create"));
        assertEquals(TemuApiCategory.OTHER, TemuApiCategory.fromApiType("unknown.api"));
    }

}
