package cn.iocoder.yudao.module.amazon.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Amazon OAuth 回调参数。
 */
@Data
@Schema(description = "管理后台 - Amazon OAuth 回调 Request VO")
public class AmazonCallbackReqVO {

    /** state 防重放和租户绑定凭据。 */
    @NotBlank(message = "state 不能为空")
    private String state;
    /** Amazon 授权码。 */
    @NotBlank(message = "授权码不能为空")
    private String sellingPartnerId;
    /** Amazon 返回的授权 code。 */
    @NotBlank(message = "授权 code 不能为空")
    private String spapiOauthCode;
}
