package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 创建 Temu 价格申诉单请求参数。 */
@Data
public class PricingAppealOrderCreateReqVO extends PricingBaseReqVO {
    /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
    /** 商家申诉原因代码。 */ @NotEmpty(message = "申诉原因不能为空") private List<String> merchantAppealReasonCodeList;
    /** 参与申诉的 SKU 价格信息。 */ @NotEmpty(message = "申诉 SKU 信息不能为空") @Valid private List<SkuInfo> skuInfoList;
    /** 单个 SKU 的申诉价格信息。 */
    @Data public static class SkuInfo {
        /** Temu SKU ID。 */ @NotNull(message = "SKU ID 不能为空") private Long skuId;
        /** 当前供货价。 */ @NotNull(message = "供货价不能为空") @Valid private PricingUpdateGoodsPriceReqVO.Money supplyPrice;
        /** 平台建议供货价。 */ @NotNull(message = "建议供货价不能为空") @Valid private PricingUpdateGoodsPriceReqVO.Money recommendedSupplyPrice;
        /** 商家目标供货价。 */ @NotNull(message = "目标供货价不能为空") @Valid private PricingUpdateGoodsPriceReqVO.Money targetSupplyPrice;
    }
}
