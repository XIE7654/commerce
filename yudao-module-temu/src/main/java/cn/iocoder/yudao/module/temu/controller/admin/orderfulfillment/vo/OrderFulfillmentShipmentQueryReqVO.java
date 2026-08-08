package cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Order Fulfillment 包裹物流信息查询请求参数。
 */
@Schema(description = "管理后台 - Order Fulfillment 包裹物流信息查询 Request VO")
@Data
public class OrderFulfillmentShipmentQueryReqVO extends OrderFulfillmentBaseReqVO {

    /** Temu 包裹编号。 */
    @Schema(description = "Temu 包裹编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PK-00023989289591120")
    @NotBlank(message = "包裹编号不能为空")
    private String packageSn;

    /** Temu 物流公司编号。 */
    @Schema(description = "Temu 物流公司编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "89289591120")
    @NotBlank(message = "物流公司编号不能为空")
    private String shipCompanyId;

    /** 物流追踪单号。 */
    @Schema(description = "物流追踪单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "211-00023968318071120")
    @NotBlank(message = "物流追踪单号不能为空")
    private String trackingNumber;
}
