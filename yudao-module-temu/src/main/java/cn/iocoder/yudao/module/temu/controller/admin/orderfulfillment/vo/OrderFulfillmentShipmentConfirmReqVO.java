package cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Order Fulfillment 订单发货确认请求参数。
 */
@Schema(description = "管理后台 - Order Fulfillment 订单发货确认 Request VO")
@Data
public class OrderFulfillmentShipmentConfirmReqVO extends OrderFulfillmentBaseReqVO {

    /** 发货包裹列表，按 Temu sendRequestList 结构传入。 */
    @Schema(description = "发货包裹列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货包裹列表不能为空")
    private JsonNode sendRequestList;

    /** 发货方式，取值以 Temu 平台定义为准。 */
    @Schema(description = "发货方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "发货方式不能为空")
    private Integer sendType;
}
