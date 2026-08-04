package cn.iocoder.yudao.module.temu.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 使用 Temu 授权码创建 access_token 的请求参数。
 */
@Schema(description = "管理后台 - Temu access_token 创建 Request VO")
@Data
public class TemuAccessTokenCreateReqVO {

    /**
     * Temu 站点代码，例如 US、DE、JP。
     */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /**
     * Temu 授权回调返回的临时授权码，仅能使用一次且十分钟内有效。
     */
    @Schema(description = "Temu 授权回调返回的授权码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "授权码不能为空")
    private String code;

    /**
     * 调用 Temu Router 接口所需的 access_token。
     */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;

}
