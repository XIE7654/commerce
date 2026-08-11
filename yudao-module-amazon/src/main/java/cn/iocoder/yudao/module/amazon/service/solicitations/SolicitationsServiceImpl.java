package cn.iocoder.yudao.module.amazon.service.solicitations;

import cn.iocoder.yudao.module.amazon.controller.admin.solicitations.vo.SolicitationsReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Amazon Solicitations 服务实现。 */
@Service
public class SolicitationsServiceImpl implements SolicitationsService {
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonOAuthService amazonOAuthService; @Resource private AmazonShopMapper amazonShopMapper; @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */ @Override public Map<String, Object> getSolicitationActionsForOrder(SolicitationsReqVO request) { return invoke(request, HttpMethod.GET, "", "getSolicitationActionsForOrder"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> createProductReviewAndSellerFeedbackSolicitation(SolicitationsReqVO request) { return invoke(request, HttpMethod.POST, "/solicitations/productReviewAndSellerFeedback", "createProductReviewAndSellerFeedbackSolicitation"); }
    /** 为订单操作建立带 marketplaceIds 查询参数的 URI；征集创建成功会返回空响应。 */
    private Map<String, Object> invoke(SolicitationsReqVO request, HttpMethod method, String suffix, String operation) {
        AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode());
        List<String> ids = request.getMarketplaceIds() == null || request.getMarketplaceIds().isEmpty() ? List.of(marketplace.getMarketplaceId()) : request.getMarketplaceIds();
        String query = ids.stream().map(id -> "marketplaceIds=" + UriUtils.encodeQueryParam(id, StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + "/solicitations/v1/orders/" + UriUtils.encodePathSegment(request.getAmazonOrderId(), StandardCharsets.UTF_8) + suffix + "?" + query);
        return method == HttpMethod.GET ? amazonSellingPartnerClient.getByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), AmazonApiCategory.SOLICITATIONS, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()) : amazonSellingPartnerClient.mutateByCategoryOptional(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), method, null, AmazonApiCategory.SOLICITATIONS, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 获取当前租户店铺。 */ private AmazonShopDO shop(Long shopId) { AmazonShopDO value = amazonShopMapper.selectById(shopId); if (value == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return value; }
    /** 解析 Amazon 调用站点。 */ private AmazonMarketplaceEnum marketplace(String countryCode) { AmazonMarketplaceEnum value = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (value == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return value; }
}
