package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Temu Pricing 接口公共认证请求参数。 */
@Data
public class PricingBaseReqVO {

    /** Temu 站点代码，用于选择服务端区域应用配置。 */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /** 仅用于本次 Temu OpenAPI 调用的店铺授权 Token。 */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;
}
