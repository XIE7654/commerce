package cn.iocoder.yudao.module.temu.controller.admin.authorization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 查询 Temu access_token 授权信息的请求参数。
 */
@Schema(description = "管理后台 - Temu access_token 授权信息查询 Request VO")
@Data
public class AuthorizationAccessTokenInfoReqVO {

    /** Temu 站点代码，例如 US、DE、JP。 */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /** Temu Router 调用所需的店铺授权 Token。 */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;

}
