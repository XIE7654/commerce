package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;

import java.util.Map;

/** Temu Webhook 事件订阅接口服务。 */
public class WebhookApi extends TemuApiService {

    /** 创建 Webhook 事件订阅接口服务。 */
    public WebhookApi(TemuClient client) {
        super(client);
    }

    /**
     * 调用 {@code bg.tmc.message.update} 更新店铺 Webhook 事件订阅。
     *
     * @param params 订阅或取消订阅的事件码列表
     * @return Temu JSON 响应
     */
    public JsonNode updateMessageSubscription(Map<String, Object> params) {
        return call("bg.tmc.message.update", params);
    }

}
