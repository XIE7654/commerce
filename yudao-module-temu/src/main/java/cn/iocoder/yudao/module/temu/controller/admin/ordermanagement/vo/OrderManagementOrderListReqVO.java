package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Order Management 订单列表查询请求参数。
 */
@Schema(description = "管理后台 - Order Management 订单列表查询 Request VO")
@Data
public class OrderManagementOrderListReqVO extends OrderManagementBaseReqVO {

    /** 父订单状态，取值以 Temu 平台定义为准。 */
    @Schema(description = "父订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
    @NotNull(message = "父订单状态不能为空")
    private Integer parentOrderStatus;

    /** 订单所属区域编号。 */
    @Schema(description = "区域编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "211")
    @NotNull(message = "区域编号不能为空")
    private Long regionId;

    /** 页码，从 1 开始。 */
    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于等于 1")
    private Integer pageNumber;

    /** 每页记录数。 */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "每页记录数不能为空")
    @Min(value = 1, message = "每页记录数必须大于等于 1")
    private Integer pageSize;
}
