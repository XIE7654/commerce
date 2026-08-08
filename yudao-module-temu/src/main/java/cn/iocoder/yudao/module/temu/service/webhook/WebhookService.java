package cn.iocoder.yudao.module.temu.service.webhook;

import cn.iocoder.yudao.module.temu.controller.admin.webhook.vo.WebhookEventSubscriptionUpdateReqVO;
import tools.jackson.databind.JsonNode;

/** Temu Webhook 事件订阅业务 Service。 */
public interface WebhookService {

    /**
     * 更新指定店铺的 Temu Webhook 事件订阅。
     *
     * @param request 站点、授权 Token 和事件订阅变更参数
     * @return Temu 官方订阅更新响应
     */
    JsonNode updateEventSubscription(WebhookEventSubscriptionUpdateReqVO request);

}
