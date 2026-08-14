package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "管理后台 - TOTP 多重身份验证初始化 Response VO")
@Data
@Builder
public class AuthTotpSetupRespVO {

    @Schema(description = "可手动录入认证器的 Base32 密钥")
    private String secret;

    @Schema(description = "认证器标准配置 URI")
    private String otpauthUri;

    @Schema(description = "认证器二维码 PNG 的 Base64 Data URL")
    private String qrCode;
}
