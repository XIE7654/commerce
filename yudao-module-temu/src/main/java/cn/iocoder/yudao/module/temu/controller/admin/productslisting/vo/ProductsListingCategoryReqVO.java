package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Products Listing 分类查询请求参数。
 */
@Schema(description = "管理后台 - Products Listing 分类查询 Request VO")
@Data
public class ProductsListingCategoryReqVO extends ProductsListingBaseReqVO {

    /** 父分类 ID；不传时由 Temu 返回一级分类。 */
    @Schema(description = "父分类 ID", example = "0")
    private Long parentCatId;
}
