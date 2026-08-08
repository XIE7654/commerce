package cn.iocoder.yudao.module.temu.controller.admin.webhook.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** 更新 Temu Webhook 事件订阅请求参数。 */
@Schema(description = "管理后台 - Temu Webhook 事件订阅更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class WebhookEventSubscriptionUpdateReqVO extends WebhookBaseReqVO {

    /** 需要订阅的 Temu 事件码列表。 */
    @Schema(description = "需要订阅的 Temu 事件码列表", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "[\"bg_order_status_change_event\", \"bg_aftersales_status_change\"]")
    @NotEmpty(message = "订阅事件码列表不能为空")
    private List<String> permitEventCodeList;

    /** 需要取消订阅的 Temu 事件码列表；不传时按空列表处理。 */
    @Schema(description = "需要取消订阅的 Temu 事件码列表", example = "[\"bg_cancel_order_status_change\"]")
    private List<String> cancelEventCodeList;

}
