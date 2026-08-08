package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Products Listing 品牌与商标查询请求参数。
 */
@Schema(description = "管理后台 - Products Listing 品牌与商标查询 Request VO")
@Data
public class ProductsListingBrandTrademarkReqVO extends ProductsListingBaseReqVO {

    /** 品牌 ID。 */
    @Schema(description = "品牌 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
    @NotNull(message = "品牌 ID 不能为空")
    private Long brandId;

    /** 页码，从 1 开始。 */
    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    private Integer page;

    /** 每页记录数。 */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "25")
    @NotNull(message = "每页记录数不能为空")
    private Integer size;
}
