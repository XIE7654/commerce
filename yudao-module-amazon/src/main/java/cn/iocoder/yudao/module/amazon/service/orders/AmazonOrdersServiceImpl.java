package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiSdkFactory;
import com.amazon.SellingPartnerAPIAA.LWAException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.api.orders.v0.OrdersV0Api;
import software.amazon.spapi.models.orders.v0.GetOrderItemsResponse;
import software.amazon.spapi.models.orders.v0.GetOrderResponse;
import software.amazon.spapi.models.orders.v0.GetOrdersResponse;

import java.util.List;

/** Amazon Orders v0 官方 SDK 服务实现。 */
@Service
public class AmazonOrdersServiceImpl implements AmazonOrdersService {
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSpApiSdkFactory amazonSpApiSdkFactory;

    /** {@inheritDoc} */
    @Override
    public GetOrdersResponse getOrders(AmazonOrdersListReqVO request) throws ApiException, LWAException {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        return ordersApi(shop, marketplace).getOrders(List.of(marketplace.getMarketplaceId()), request.getCreatedAfter(),
                request.getCreatedBefore(), request.getLastUpdatedAfter(), request.getLastUpdatedBefore(), request.getOrderStatuses(),
                request.getFulfillmentChannels(), request.getPaymentMethods(), request.getBuyerEmail(), request.getSellerOrderId(),
                request.getMaxResultsPerPage(), request.getEasyShipShipmentStatuses(), request.getElectronicInvoiceStatuses(), request.getNextToken(),
                request.getAmazonOrderIds(), request.getActualFulfillmentSupplySourceId(), request.getIsISPU(), request.getStoreChainStoreId(),
                request.getEarliestDeliveryDateBefore(), request.getEarliestDeliveryDateAfter(), request.getLatestDeliveryDateBefore(),
                request.getLatestDeliveryDateAfter());
    }

    /** {@inheritDoc} */
    @Override
    public GetOrderResponse getOrder(AmazonOrderGetReqVO request) throws ApiException, LWAException {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        return ordersApi(shop, marketplace).getOrder(request.getOrderId());
    }

    /** {@inheritDoc} */
    @Override
    public GetOrderItemsResponse getOrderItems(AmazonOrderItemsReqVO request) throws ApiException, LWAException {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        return ordersApi(shop, marketplace).getOrderItems(request.getOrderId(), request.getNextToken());
    }

    /** 构造官方 Orders SDK 客户端。 */
    private OrdersV0Api ordersApi(AmazonShopDO shop, AmazonMarketplaceEnum marketplace) {
        return new OrdersV0Api.Builder()
                .lwaAuthorizationCredentials(amazonSpApiSdkFactory.credentials(shop.getSellerRefreshToken()))
                .endpoint(amazonMarketplaceProvider.getEndpoint(marketplace))
                .build();
    }

    /** 查询店铺。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        return shop;
    }

    /** 解析订单请求的 Marketplace。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        return marketplace;
    }
}
