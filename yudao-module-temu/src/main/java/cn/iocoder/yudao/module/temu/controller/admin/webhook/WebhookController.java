package cn.iocoder.yudao.module.temu.controller.admin.webhook;

import cn.iocoder.yudao.module.temu.controller.admin.webhook.vo.WebhookEventSubscriptionUpdateReqVO;
import cn.iocoder.yudao.module.temu.service.webhook.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

/** 管理后台 Temu Webhook 接口。 */
@Tag(name = "管理后台 - Temu Webhook")
@RestController
@RequestMapping("/temu/webhook")
@Validated
public class WebhookController {

    @Resource
    private WebhookService webhookService;

    /**
     * 更新当前 Temu 店铺的 Webhook 事件订阅。
     *
     * @param request 站点、授权 Token 和订阅变更参数
     * @return Temu 官方订阅更新响应
     */
    @PostMapping("/events/subscribe")
    @Operation(summary = "更新 Temu Webhook 事件订阅")
    @PreAuthorize("@ss.hasPermission('temu:webhook:update')")
    public JsonNode updateEventSubscription(@Valid @RequestBody WebhookEventSubscriptionUpdateReqVO request) {
        return webhookService.updateEventSubscription(request);
    }

}
