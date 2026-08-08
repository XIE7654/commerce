package cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 删除应用通知的请求参数。 */
@Data
public class AmazonDeleteNotificationsReqVO extends AmazonAppIntegrationsShopReqVO {

    @NotBlank
    @Schema(description = "应用入驻时配置的通知模板标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "PRICE_CHANGE")
    private String templateId;

    @NotBlank
    @Pattern(regexp = "INCORRECT_CONTENT|INCORRECT_RECIPIENT", message = "删除原因不正确")
    @Schema(description = "删除原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "INCORRECT_CONTENT")
    private String deletionReason;
}
