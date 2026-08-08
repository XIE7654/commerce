package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Listing 分类模板相关请求参数。
 */
@Schema(description = "管理后台 - Products Listing 分类模板 Request VO")
@Data
public class ProductsListingCatIdReqVO extends ProductsListingBaseReqVO {

    /** Temu 分类 ID。 */
    @Schema(description = "Temu 分类 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
    @NotNull(message = "分类 ID 不能为空")
    private Long catId;
}
