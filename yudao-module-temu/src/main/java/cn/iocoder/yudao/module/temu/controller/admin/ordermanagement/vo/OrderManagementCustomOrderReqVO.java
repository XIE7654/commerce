package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Order Management 定制订单查询请求参数。
 */
@Schema(description = "管理后台 - Order Management 定制订单查询 Request VO")
@Data
public class OrderManagementCustomOrderReqVO extends OrderManagementBaseReqVO {

    /** 需要查询定制信息的子订单编号列表。 */
    @Schema(description = "子订单编号列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"211-08979834258470637\"]")
    @NotEmpty(message = "子订单编号列表不能为空")
    private List<String> orderSnList;
}
