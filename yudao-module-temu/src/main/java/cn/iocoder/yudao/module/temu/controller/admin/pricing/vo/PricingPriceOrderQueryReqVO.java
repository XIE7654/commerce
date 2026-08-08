package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 查询 Temu 调价单请求参数。 */
@Data
public class PricingPriceOrderQueryReqVO extends PricingBaseReqVO {
    /** 页码，从 1 开始。 */ @NotNull(message = "页码不能为空") @Min(value = 1, message = "页码必须大于 0") private Integer page;
    /** 每页条数。 */ @NotNull(message = "每页条数不能为空") @Min(value = 1, message = "每页条数必须大于 0") private Integer size;
    /** 调价单类型，取值以 Temu 平台定义为准。 */ @NotNull(message = "调价单类型不能为空") private Integer priceOrderType;
    /** 可选的 Temu 商品 ID 筛选条件。 */ private Long goodsId;
}
