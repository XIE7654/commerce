package cn.iocoder.yudao.module.amazon.service.sellers;

import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

/**
 * Amazon Sellers 服务实现。
 */
@Service
public class AmazonSellersServiceImpl implements AmazonSellersService {
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getMarketplaceParticipations(AmazonSellersReqVO request) {
        return execute(request, "/sellers/v1/marketplaceParticipations", "getMarketplaceParticipations", "marketplace-participations");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getAccount(AmazonSellersReqVO request) {
        return execute(request, "/sellers/v1/account", "getAccount", "account");
    }

    /**
     * 使用店铺授权调用 Sellers 资源，并按店铺所属区域选择 Amazon SP-API 端点。
     *
     * @param request       店铺参数
     * @param path          Sellers 资源路径
     * @param operationName Amazon 操作名称
     * @param storageName   响应归档名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> execute(AmazonSellersReqVO request, String path, String operationName, String storageName) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        return amazonSellingPartnerClient.getByCategory(URI.create(marketplace.getEndpoint() + path),
                amazonOAuthService.getSellerAccessToken(shop.getId()), AmazonApiCategory.SELLERS, operationName, storageName,
                shop.getId(), null, null);
    }

    /**
     * 查询当前租户下的店铺，保证店铺授权不能跨租户使用。
     *
     * @param shopId 店铺编号
     * @return 当前租户店铺
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        return shop;
    }

    /**
     * 将店铺销售区域转换为用于选择 SP-API 端点的 Marketplace 配置。
     *
     * @param region Amazon 销售区域
     * @return Marketplace 配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String region) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromSalesRegion(region);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 销售区域: " + region);
        }
        return marketplace;
    }
}
