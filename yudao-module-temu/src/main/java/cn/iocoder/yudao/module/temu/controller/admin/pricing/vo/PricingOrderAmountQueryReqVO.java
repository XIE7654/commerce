package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 查询 Temu 订单金额请求参数。 */
@Data
public class PricingOrderAmountQueryReqVO extends PricingBaseReqVO {
    /** Temu 父订单编号。 */ @NotBlank(message = "父订单编号不能为空") private String parentOrderSn;
}
