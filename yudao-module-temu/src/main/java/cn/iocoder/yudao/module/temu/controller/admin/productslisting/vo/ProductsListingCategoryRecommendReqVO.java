package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Products Listing 分类推荐请求参数。
 */
@Schema(description = "管理后台 - Products Listing 分类推荐 Request VO")
@Data
public class ProductsListingCategoryRecommendReqVO extends ProductsListingBaseReqVO {

    /** 待推荐分类的商品名称。 */
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "REX Commuter")
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;
}
