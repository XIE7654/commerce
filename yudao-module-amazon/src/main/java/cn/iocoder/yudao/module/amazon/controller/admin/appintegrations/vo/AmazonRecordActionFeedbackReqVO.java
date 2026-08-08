package cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** 记录卖家通知操作反馈的请求参数。 */
@Data
public class AmazonRecordActionFeedbackReqVO extends AmazonAppIntegrationsShopReqVO {

    @NotBlank
    @Schema(description = "待反馈的通知编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "0cf25616-f68c-4eba-a5d3-6823b61506c0")
    private String notificationId;

    @NotBlank
    @Pattern(regexp = "SELLER_ACTION_COMPLETED", message = "反馈操作代码不正确")
    @Schema(description = "卖家操作反馈代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SELLER_ACTION_COMPLETED")
    private String feedbackActionCode;
}
