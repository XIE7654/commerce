package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 查询 Temu 价格申诉单请求参数。 */
@Data
public class PricingAppealOrderQueryReqVO extends PricingBaseReqVO {
    /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
    /** 申诉单标签代码，取值以 Temu 平台定义为准。 */ @NotNull(message = "标签代码不能为空") private Integer tabCode;
}
