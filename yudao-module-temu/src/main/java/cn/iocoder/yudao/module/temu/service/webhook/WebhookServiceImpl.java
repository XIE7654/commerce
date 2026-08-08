package cn.iocoder.yudao.module.temu.service.webhook;

import cn.iocoder.yudao.module.temu.controller.admin.webhook.vo.WebhookBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.webhook.vo.WebhookEventSubscriptionUpdateReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/** Temu Webhook 事件订阅业务 Service 实现。 */
@Service
@Validated
public class WebhookServiceImpl implements WebhookService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;

    /**
     * 调用 {@code bg.tmc.message.update} 更新店铺的事件订阅。
     *
     * @param request 站点、授权 Token 和订阅变更参数
     * @return Temu 官方订阅更新响应
     */
    @Override
    public JsonNode updateEventSubscription(WebhookEventSubscriptionUpdateReqVO request) {
        // Temu 要求两个事件码字段均为数组，避免 null 被请求签名器过滤后改变接口语义。
        return createClient(request).getWebhook().updateMessageSubscription(Map.of(
                "permitEventCodeList", request.getPermitEventCodeList(),
                "cancelEventCodeList", request.getCancelEventCodeList() == null
                        ? Collections.emptyList() : request.getCancelEventCodeList()));
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求暴露敏感配置。</p>
     *
     * @param request 包含站点与授权 Token 的 Webhook 请求
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(WebhookBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(),
                site.getEndpoint(), temuJsonStorageService);
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断配置值
     * @return 值为空或仅包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
