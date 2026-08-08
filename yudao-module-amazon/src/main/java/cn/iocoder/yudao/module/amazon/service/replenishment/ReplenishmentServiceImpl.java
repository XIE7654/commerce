package cn.iocoder.yudao.module.amazon.service.replenishment;

import cn.iocoder.yudao.module.amazon.controller.admin.replenishment.vo.ReplenishmentReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.Map;

/** Amazon Replenishment 服务实现。 */
@Service
public class ReplenishmentServiceImpl implements ReplenishmentService {
    private static final String BASE_PATH = "/replenishment/2022-11-07";
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */ @Override public Map<String, Object> getSellingPartnerMetrics(ReplenishmentReqVO request) { return post(request, "/sellingPartners/metrics/search", "getSellingPartnerMetrics"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> listOfferMetrics(ReplenishmentReqVO request) { return post(request, "/offers/metrics/search", "listOfferMetrics"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> listOffers(ReplenishmentReqVO request) { return post(request, "/offers/search", "listOffers"); }
    /** 按官方模型透传补货筛选条件，并使用当前租户店铺的授权令牌调用 Amazon。 */
    private Map<String, Object> post(ReplenishmentReqVO request, String path, String operation) {
        AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + BASE_PATH + path);
        return amazonSellingPartnerClient.mutateByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), HttpMethod.POST,
                request.getBody(), AmazonApiCategory.REPLENISHMENT, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 获取受租户插件保护的店铺记录，防止跨租户调用。 */
    private AmazonShopDO shop(Long shopId) { AmazonShopDO value = amazonShopMapper.selectById(shopId); if (value == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return value; }
    /** 解析站点国家代码为 Amazon 调用端点。 */
    private AmazonMarketplaceEnum marketplace(String countryCode) { AmazonMarketplaceEnum value = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (value == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return value; }
}
