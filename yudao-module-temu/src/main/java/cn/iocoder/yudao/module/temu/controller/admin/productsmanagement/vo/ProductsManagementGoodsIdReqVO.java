package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Management 单商品请求参数。
 */
@Schema(description = "管理后台 - Products Management 单商品 Request VO")
@Data
public class ProductsManagementGoodsIdReqVO extends ProductsManagementBaseReqVO {

    /** Temu 商品 ID。 */
    @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "602771461874681")
    @NotNull(message = "商品 ID 不能为空")
    private Long goodsId;
}
