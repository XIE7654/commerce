package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Products Listing 属性推荐请求参数。
 */
@Schema(description = "管理后台 - Products Listing 属性推荐 Request VO")
@Data
public class ProductsListingPropertyRecommendReqVO extends ProductsListingBaseReqVO {

    /** Temu 分类 ID。 */
    @Schema(description = "Temu 分类 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "40023")
    @NotNull(message = "分类 ID 不能为空")
    private Long catId;

    /** 商品名称。 */
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    /** 商品已有属性列表，按 Temu goodsPropList 结构传入。 */
    @Schema(description = "商品属性列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品属性列表不能为空")
    private JsonNode goodsPropList;
}
