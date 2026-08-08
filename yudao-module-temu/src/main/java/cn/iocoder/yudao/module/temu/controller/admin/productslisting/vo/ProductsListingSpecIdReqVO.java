package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Listing 规格 ID 创建请求参数。
 */
@Schema(description = "管理后台 - Products Listing 规格 ID 创建 Request VO")
@Data
public class ProductsListingSpecIdReqVO extends ProductsListingBaseReqVO {

    /** Temu 分类 ID。 */
    @Schema(description = "Temu 分类 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
    @NotNull(message = "分类 ID 不能为空")
    private Long catId;

    /** 父规格 ID。 */
    @Schema(description = "父规格 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "父规格 ID 不能为空")
    private Long parentSpecId;

    /** 子规格名称。 */
    @Schema(description = "子规格名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "spec name")
    @NotBlank(message = "子规格名称不能为空")
    private String childSpecName;
}
