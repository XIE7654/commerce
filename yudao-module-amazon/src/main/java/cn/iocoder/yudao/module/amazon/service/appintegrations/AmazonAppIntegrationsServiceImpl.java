package cn.iocoder.yudao.module.amazon.service.appintegrations;

import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonAppIntegrationsShopReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonCreateNotificationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonDeleteNotificationsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonRecordActionFeedbackReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/** Amazon Application Integrations API 服务实现。 */
@Service
public class AmazonAppIntegrationsServiceImpl implements AmazonAppIntegrationsService {

    private static final String PATH = "/appIntegrations/2024-04-01/notifications";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createNotification(AmazonCreateNotificationReqVO request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("templateId", request.getTemplateId());
        body.put("notificationParameters", request.getNotificationParameters());
        if (request.getMarketplaceId() != null && !request.getMarketplaceId().isBlank()) {
            body.put("marketplaceId", request.getMarketplaceId());
        }
        return execute(request, PATH, body, "createNotification", false);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteNotifications(AmazonDeleteNotificationsReqVO request) {
        Map<String, Object> body = Map.of("templateId", request.getTemplateId(), "deletionReason", request.getDeletionReason());
        execute(request, PATH + "/deletion", body, "deleteNotifications", true);
    }

    /** {@inheritDoc} */
    @Override
    public void recordActionFeedback(AmazonRecordActionFeedbackReqVO request) {
        Map<String, Object> body = Map.of("feedbackActionCode", request.getFeedbackActionCode());
        execute(request, PATH + "/" + UriUtils.encodePathSegment(request.getNotificationId(), StandardCharsets.UTF_8)
                + "/feedback", body, "recordActionFeedback", true);
    }

    /**
     * 使用当前租户店铺的 Seller 授权调用通知接口。
     *
     * @param request 店铺与站点参数
     * @param path Application Integrations 资源路径
     * @param body 请求体
     * @param operationName Amazon 操作名称
     * @param allowEmptyResponse 是否允许 Amazon 以 204 空响应表示成功
     * @return Amazon JSON 响应；204 响应返回空 Map
     */
    private Map<String, Object> execute(AmazonAppIntegrationsShopReqVO request, String path, Object body,
                                        String operationName, boolean allowEmptyResponse) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + path);
        return allowEmptyResponse
                ? amazonSellingPartnerClient.mutateByCategoryOptional(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.POST, body, AmazonApiCategory.APP_INTEGRATIONS, operationName, "notification", shop.getId(),
                request.getCountryCode(), marketplace.getMarketplaceId())
                : amazonSellingPartnerClient.mutateByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.POST, body, AmazonApiCategory.APP_INTEGRATIONS, operationName, "notification", shop.getId(),
                request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 查询当前租户下的店铺，避免跨租户使用店铺授权。
     *
     * @param shopId 店铺编号
     * @return 当前租户店铺
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 将国家代码转换为 Amazon Marketplace 配置。
     *
     * @param countryCode 国家代码
     * @return Marketplace 配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }
}
