package cn.iocoder.yudao.module.amazon.service.sellers;

import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiSdkFactory;
import com.amazon.SellingPartnerAPIAA.LWAException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.api.sellers.v1.SellersApi;
import software.amazon.spapi.models.sellers.v1.GetAccountResponse;
import software.amazon.spapi.models.sellers.v1.GetMarketplaceParticipationsResponse;

/**
 * Amazon Sellers 服务实现。
 */
@Service
public class AmazonSellersServiceImpl implements AmazonSellersService {
    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSpApiSdkFactory amazonSpApiSdkFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public GetMarketplaceParticipationsResponse getMarketplaceParticipations(AmazonSellersReqVO request) throws ApiException, LWAException {
        return sellersApi(request).getMarketplaceParticipations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAccountResponse getAccount(AmazonSellersReqVO request) throws ApiException, LWAException {
        return sellersApi(request).getAccount();
    }

    /**
     * 将店铺上下文转换为 SDK 请求，授权令牌仅在服务层获取，避免暴露给 Controller。
     *
     * @param request 前端传入的店铺参数
     * @return 包含端点和授权信息的 SDK 请求
     */
    private SellersApi sellersApi(AmazonSellersReqVO request) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        return new SellersApi.Builder()
                .lwaAuthorizationCredentials(amazonSpApiSdkFactory.credentials(shop.getSellerRefreshToken()))
                .endpoint(amazonMarketplaceProvider.getEndpoint(marketplace))
                .build();
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
