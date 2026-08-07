package cn.iocoder.yudao.module.amazon.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Amazon Token 调试请求参数。
 */
@Data
public class AmazonTokenReqVO {

    @Schema(description = "Amazon 授权码；获取 Token 接口必填", example = "ANDrasffGNatieRieOYBG")
    private String code;

    @Schema(description = "Amazon refresh token；刷新 Token 接口必填", example = "Atzr|IQEBLzAtAhexample")
    private String refreshToken;

    @Schema(description = "国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
}
