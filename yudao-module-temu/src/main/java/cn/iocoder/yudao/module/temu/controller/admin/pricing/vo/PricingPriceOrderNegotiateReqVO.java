package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 议价 Temu 调价单请求参数。 */
@Data
public class PricingPriceOrderNegotiateReqVO extends PricingBaseReqVO {
    /** 调价单 ID。 */ @NotNull(message = "调价单 ID 不能为空") private Long priceOrderId;
    /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
    /** 调价提交版本，用于并发控制。 */ @NotNull(message = "调价提交版本不能为空") private Integer priceCommitVersion;
    /** 调价提交 ID。 */ @NotNull(message = "调价提交 ID 不能为空") private Long priceCommitId;
    /** 议价 SKU 明细。 */ @NotEmpty(message = "议价 SKU 明细不能为空") @Valid private List<NegotiatedPriceSku> negotiatedPriceSkuList;
    /** 单个 SKU 的议价条件。 */
    @Data public static class NegotiatedPriceSku {
        /** 议价原因。 */ @NotBlank(message = "议价原因不能为空") private String reason;
        /** Temu SKU ID。 */ @NotNull(message = "SKU ID 不能为空") private Long skuId;
        /** 议价后的供货价。 */ @NotNull(message = "新供货价不能为空") @Valid private PricingUpdateGoodsPriceReqVO.Money newSupplierPrice;
    }
}
