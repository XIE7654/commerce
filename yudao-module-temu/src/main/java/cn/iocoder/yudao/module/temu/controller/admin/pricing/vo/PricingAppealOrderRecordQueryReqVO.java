package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 查询 Temu 价格申诉单记录请求参数。 */
@Data
public class PricingAppealOrderRecordQueryReqVO extends PricingBaseReqVO {
    /** Temu SKU ID。 */ @NotNull(message = "SKU ID 不能为空") private Long skuId;
}
