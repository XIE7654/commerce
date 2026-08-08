package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Products Management 商品规格详情查询请求参数。
 */
@Schema(description = "管理后台 - Products Management 商品规格详情查询 Request VO")
@Data
public class ProductsManagementSpecDetailReqVO extends ProductsManagementBaseReqVO {

    /** Temu 规格 ID 列表。 */
    @Schema(description = "Temu 规格 ID 列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "规格 ID 列表不能为空")
    private List<Long> specIdList;
}
