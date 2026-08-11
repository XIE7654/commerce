package cn.iocoder.yudao.module.amazon.service.sellers;

import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.sellers.AmazonSellersApi;
import cn.iocoder.yudao.module.amazon.sdk.sellers.AmazonSellersRequest;
import cn.iocoder.yudao.module.amazon.sdk.sellers.AmazonSellersResponse;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.AccountDto;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.MarketplaceParticipationDto;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.service.seller.AmazonSellerAccountService;
import cn.iocoder.yudao.module.amazon.service.seller.AmazonShopMarketplaceParticipationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private AmazonSellersApi amazonSellersApi;
    @Resource
    private AmazonSellerAccountService amazonSellerAccountService;
    @Resource
    private AmazonShopMarketplaceParticipationService marketplaceParticipationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public AmazonSellersResponse<List<MarketplaceParticipationDto>> getMarketplaceParticipations(AmazonSellersReqVO request) {
        return amazonSellersApi.getMarketplaceParticipations(buildSdkRequest(request));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmazonSellersResponse<AccountDto> getAccount(AmazonSellersReqVO request) {
        return amazonSellersApi.getAccount(buildSdkRequest(request));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AmazonSellersResponse<AccountDto> syncAccount(AmazonSellersReqVO request) {
        AmazonSellersResponse<AccountDto> accountResponse = getAccount(request);
        amazonSellerAccountService.syncSellerAccount(request.getShopId(), accountResponse.getData());

        // Marketplace 参与状态不包含在 Account 响应中，需通过独立的 Sellers 接口获取。
        AmazonSellersResponse<List<MarketplaceParticipationDto>> marketplaceResponse = getMarketplaceParticipations(request);
        marketplaceParticipationService.syncMarketplaceParticipations(request.getShopId(), marketplaceResponse.getData());
        return accountResponse;
    }

    /**
     * 将店铺上下文转换为 SDK 请求，授权令牌仅在服务层获取，避免暴露给 Controller。
     *
     * @param request 前端传入的店铺参数
     * @return 包含端点和授权信息的 SDK 请求
     */
    private AmazonSellersRequest buildSdkRequest(AmazonSellersReqVO request) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        AmazonSellersRequest sdkRequest = new AmazonSellersRequest();
        sdkRequest.setShopId(shop.getId());
        sdkRequest.setEndpoint(marketplace.getEndpoint());
        sdkRequest.setAccessToken(amazonOAuthService.getSellerAccessToken(shop.getId()));
        return sdkRequest;
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
