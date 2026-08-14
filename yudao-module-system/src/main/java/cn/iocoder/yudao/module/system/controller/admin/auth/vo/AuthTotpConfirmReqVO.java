package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "管理后台 - 确认绑定 TOTP 多重身份验证 Request VO")
@Data
public class AuthTotpConfirmReqVO extends AuthTotpSetupReqVO {

    @Schema(description = "初始化返回的 TOTP 密钥", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "TOTP 密钥不能为空")
    private String secret;

    @Schema(description = "认证器动态码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @Pattern(regexp = "^\\d{6}$", message = "TOTP 动态码必须为 6 位数字")
    private String code;
}
