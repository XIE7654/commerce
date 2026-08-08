package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Order Management 父订单查询请求参数。
 */
@Schema(description = "管理后台 - Order Management 父订单查询 Request VO")
@Data
public class OrderManagementParentOrderReqVO extends OrderManagementBaseReqVO {

    /** Temu 父订单编号。 */
    @Schema(description = "Temu 父订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PO-211-08979834258470637")
    @NotBlank(message = "父订单编号不能为空")
    private String parentOrderSn;
}
