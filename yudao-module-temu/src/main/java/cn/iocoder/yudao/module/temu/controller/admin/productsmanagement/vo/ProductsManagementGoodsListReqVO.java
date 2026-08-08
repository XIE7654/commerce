package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Management 商品列表查询请求参数。
 */
@Schema(description = "管理后台 - Products Management 商品列表查询 Request VO")
@Data
public class ProductsManagementGoodsListReqVO extends ProductsManagementBaseReqVO {

    /** 商品搜索类型，取值以 Temu 平台定义为准。 */
    @Schema(description = "商品搜索类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "商品搜索类型不能为空")
    private Integer goodsSearchType;

    /** 商品排序类型，取值以 Temu 平台定义为准。 */
    @Schema(description = "商品排序类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "商品排序类型不能为空")
    private Integer orderType;

    /** 页码，从 1 开始。 */
    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    private Integer pageNo;

    /** 每页记录数。 */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "每页记录数不能为空")
    private Integer pageSize;
}
