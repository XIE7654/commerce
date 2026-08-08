package cn.iocoder.yudao.module.amazon.service.applicationmanagement;

import cn.iocoder.yudao.module.amazon.controller.admin.applicationmanagement.vo.AmazonApplicationManagementReqVO;
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

/** Amazon Application Management API 服务实现。 */
@Service
public class AmazonApplicationManagementServiceImpl implements AmazonApplicationManagementService {

    private static final String PATH = "/applications/2023-11-30/clientSecret";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public void rotateApplicationClientSecret(AmazonApplicationManagementReqVO request) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        // Amazon 以 204 表示轮换请求已受理，新密钥只会发送到开发者注册的队列。
        amazonSellingPartnerClient.mutateByCategoryOptional(URI.create(marketplace.getEndpoint() + PATH),
                amazonOAuthService.getSellerAccessToken(shop.getId()), HttpMethod.POST, null,
                AmazonApiCategory.APPLICATION_MANAGEMENT, "rotateApplicationClientSecret", "client-secret", shop.getId(),
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
