package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Management 商品上架状态更新请求参数。
 */
@Schema(description = "管理后台 - Products Management 商品上架状态更新 Request VO")
@Data
public class ProductsManagementSaleStatusUpdateReqVO extends ProductsManagementGoodsIdReqVO {

    /** 上架状态，1 表示上架，0 表示下架，具体以 Temu 平台定义为准。 */
    @Schema(description = "上架状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "上架状态不能为空")
    private Integer onsale;
}
