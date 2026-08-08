package cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Buy Shipping 待发货包裹发货确认请求参数。
 */
@Schema(description = "管理后台 - Buy Shipping 待发货包裹确认 Request VO")
@Data
public class BuyShippingShipLaterConfirmReqVO extends BuyShippingBaseReqVO {

    /** 发货确认明细，按 Temu packageSendInfoList 结构传入。 */
    @Schema(description = "发货确认明细", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货确认明细不能为空")
    private JsonNode packageSendInfoList;
}
