package cn.iocoder.yudao.module.amazon.service.tokens;

import cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo.AmazonRestrictedDataTokenCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo.AmazonRestrictedDataTokenRespVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/** Amazon Tokens API 服务实现。 */
@Service
public class AmazonTokensServiceImpl implements AmazonTokensService {

    private static final String RESTRICTED_DATA_TOKEN_PATH = "/tokens/2021-03-01/restrictedDataToken";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public AmazonRestrictedDataTokenRespVO createRestrictedDataToken(AmazonRestrictedDataTokenCreateReqVO request) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, Object> body = new LinkedHashMap<>();
        if (request.getTargetApplication() != null && !request.getTargetApplication().isBlank()) {
            body.put("targetApplication", request.getTargetApplication());
        }
        body.put("restrictedResources", request.getRestrictedResources());
        Map<String, Object> response = amazonSellingPartnerClient.createRestrictedDataToken(
                URI.create(marketplace.getEndpoint() + RESTRICTED_DATA_TOKEN_PATH),
                amazonOAuthService.getSellerAccessToken(shop.getId()), body, shop.getId(), request.getCountryCode(),
                marketplace.getMarketplaceId());
        return AmazonRestrictedDataTokenRespVO.of(response);
    }

    /**
     * 查询当前租户下的店铺，避免跨租户使用店铺授权创建 RDT。
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
