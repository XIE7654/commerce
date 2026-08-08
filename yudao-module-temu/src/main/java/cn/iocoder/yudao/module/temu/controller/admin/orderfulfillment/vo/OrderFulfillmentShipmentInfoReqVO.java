package cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Order Fulfillment 订单发货信息查询请求参数。
 */
@Schema(description = "管理后台 - Order Fulfillment 订单发货信息查询 Request VO")
@Data
public class OrderFulfillmentShipmentInfoReqVO extends OrderFulfillmentBaseReqVO {

    /** Temu 父订单编号。 */
    @Schema(description = "Temu 父订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PO-211-00023989289591120")
    @NotBlank(message = "父订单编号不能为空")
    private String parentOrderSn;

    /** Temu 子订单编号。 */
    @Schema(description = "Temu 子订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "211-00023968318071120")
    @NotBlank(message = "子订单编号不能为空")
    private String orderSn;
}
