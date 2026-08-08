package cn.iocoder.yudao.module.temu.controller.admin.webhook.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Webhook 接口公共认证请求参数。 */
@Data
public class WebhookBaseReqVO {

    /** Temu 站点代码，用于匹配服务端区域应用配置。 */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /** 本次调用使用的店铺 Temu 授权 Token。 */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;

}
