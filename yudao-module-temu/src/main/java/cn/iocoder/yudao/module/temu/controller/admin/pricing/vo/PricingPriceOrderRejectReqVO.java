package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 拒绝 Temu 调价单请求参数。 */
@Data
public class PricingPriceOrderRejectReqVO extends PricingBaseReqVO {
    /** 是否在拒绝后下架商品。 */ private boolean rejectDelist;
    /** 待拒绝的调价单列表。 */ @NotEmpty(message = "调价单不能为空") @Valid private List<PriceOrderBase> priceOrderBaseList;
    /** 单个待拒绝调价单。 */
    @Data public static class PriceOrderBase {
        /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
        /** 调价单编号。 */ @NotBlank(message = "调价单编号不能为空") private String priceOrderSn;
        /** 调价提交版本，用于并发控制。 */ @NotNull(message = "调价提交版本不能为空") private Integer priceCommitVersion;
    }
}
