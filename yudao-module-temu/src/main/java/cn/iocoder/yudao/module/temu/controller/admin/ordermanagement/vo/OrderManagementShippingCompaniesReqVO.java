package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Order Management 承运商查询请求参数。
 */
@Schema(description = "管理后台 - Order Management 承运商查询 Request VO")
@Data
public class OrderManagementShippingCompaniesReqVO extends OrderManagementBaseReqVO {

    /** 订单所属区域编号。 */
    @Schema(description = "区域编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "211")
    @NotBlank(message = "区域编号不能为空")
    private String regionId;
}
