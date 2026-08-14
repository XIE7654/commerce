package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 初始化 TOTP 多重身份验证 Request VO")
@Data
public class AuthTotpSetupReqVO {

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "登录账号不能为空")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "密码不能为空")
    private String password;
}
