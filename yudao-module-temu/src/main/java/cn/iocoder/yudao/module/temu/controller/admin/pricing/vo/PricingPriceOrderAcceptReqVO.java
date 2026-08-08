package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 接受 Temu 调价单请求参数。 */
@Data
public class PricingPriceOrderAcceptReqVO extends PricingBaseReqVO {
    /** 待接受的调价单列表。 */ @NotEmpty(message = "调价单不能为空") @Valid private List<PriceOrderInfo> priceOrderInfoList;
    /** 单个待接受调价单。 */
    @Data public static class PriceOrderInfo {
        /** 调价单 ID。 */ @NotNull(message = "调价单 ID 不能为空") private Long priceOrderId;
        /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
        /** 调价提交版本，用于并发控制。 */ @NotNull(message = "调价提交版本不能为空") private Integer priceCommitVersion;
    }
}
