package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** 修改 Temu 商品 SKU 供货价请求参数。 */
@Data
public class PricingUpdateGoodsPriceReqVO extends PricingBaseReqVO {
    /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
    /** 按调价原因分组的 SKU 调价明细。 */ @NotEmpty(message = "调价明细不能为空") @Valid private List<ChangeSkuPrice> changeSkuPriceDTOList;
    /** 一组相同调价原因的 SKU 调价明细。 */
    @Data public static class ChangeSkuPrice {
        /** 调价原因，取值以 Temu 平台定义为准。 */ @NotBlank(message = "调价原因不能为空") private String reason;
        /** SKU 新供货价明细。 */ @NotEmpty(message = "SKU 调价明细不能为空") @Valid private List<SkuChangePriceBase> skuChangePriceBaseDTOList;
    }
    /** SKU 调价明细。 */
    @Data public static class SkuChangePriceBase {
        /** 新供货价。 */ @NotNull(message = "新供货价不能为空") @Valid private Money newSupplierPrice;
        /** Temu SKU ID。 */ @NotNull(message = "SKU ID 不能为空") private Long skuId;
    }
    /** Temu 金额对象。 */
    @Data public static class Money {
        /** 金额。 */ @NotNull(message = "金额不能为空") private BigDecimal amount;
        /** ISO 货币代码。 */ @NotBlank(message = "货币代码不能为空") private String currency;
    }
}
