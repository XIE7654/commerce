package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Management SKU 列表查询请求参数。
 */
@Schema(description = "管理后台 - Products Management SKU 列表查询 Request VO")
@Data
public class ProductsManagementSkuListReqVO extends ProductsManagementBaseReqVO {

    /** SKU 状态筛选类型，取值以 Temu 平台定义为准。 */
    @Schema(description = "SKU 状态筛选类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "SKU 状态筛选类型不能为空")
    private Integer skuStatusFilterType;

    /** 页码，从 1 开始。 */
    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    private Integer pageNo;

    /** 每页记录数。 */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "25")
    @NotNull(message = "每页记录数不能为空")
    private Integer pageSize;
}
