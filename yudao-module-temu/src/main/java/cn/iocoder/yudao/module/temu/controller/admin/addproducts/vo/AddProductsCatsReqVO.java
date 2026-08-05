package cn.iocoder.yudao.module.temu.controller.admin.addproducts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Add Products 分类查询请求参数。
 */
@Schema(description = "管理后台 - Add Products 分类查询 Request VO")
@Data
public class AddProductsCatsReqVO {

    /** Temu 站点代码，例如 US、DE、JP。 */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /** 本次调用使用的 Temu 授权 Token。 */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;

    /** 分类名称语言，未传时由 Temu 使用默认语言。 */
    @Schema(description = "分类名称语言", example = "en")
    private String language;

    /** 父分类 ID；未传时查询一级分类。 */
    @Schema(description = "父分类 ID，不传时查询一级分类", example = "1")
    private Long parentCatId;
}
