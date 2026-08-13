package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.order.AmazonOrderDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.order.AmazonOrderMapper;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiSdkFactory;
import com.amazon.SellingPartnerAPIAA.LWAException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.api.orders.v0.OrdersV0Api;
import software.amazon.spapi.models.orders.v0.GetOrderItemsResponse;
import software.amazon.spapi.models.orders.v0.GetOrderResponse;
import software.amazon.spapi.models.orders.v0.GetOrdersResponse;
import software.amazon.spapi.models.orders.v0.Order;
import software.amazon.spapi.models.orders.v0.OrdersList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/** Amazon Orders v0 官方 SDK 服务实现。 */
@Service
@Slf4j
public class AmazonOrdersServiceImpl implements AmazonOrdersService {
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonOrderMapper amazonOrderMapper;
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

    /** {@inheritDoc} */
    @Override
    public int syncAllOrders() {
        int syncedCount = 0;
        for (AmazonShopDO shop : amazonShopMapper.selectEnabledList()) {
            try {
                syncedCount += syncShopOrders(shop);
            } catch (ApiException | LWAException | IllegalArgumentException exception) {
                // 单个店铺授权或远端调用失败不能阻断其余店铺的同步。
                log.error("[syncAllOrders][同步 Amazon 店铺({})订单失败]", shop.getId(), exception);
            }
        }
        return syncedCount;
    }

    /**
     * 拉取一个店铺的全部分页订单并幂等保存。
     *
     * @param shop 启用的 Amazon 店铺
     * @return 成功写入的订单数量
     */
    private int syncShopOrders(AmazonShopDO shop) throws ApiException, LWAException {
        AmazonMarketplaceEnum marketplace = requireMarketplace(shop.getRegion());
        OrdersV0Api ordersApi = ordersApi(shop, marketplace);
        LocalDateTime lastSyncTime = amazonOrderMapper.selectLatestSyncTimeByShopId(shop.getId());
        // Orders v0 要求 CreatedAfter 或 LastUpdatedAfter，首次同步仅回溯 Amazon 可查询的最近两年数据。
        String lastUpdatedAfter = (lastSyncTime == null
                ? Instant.now().minus(730, ChronoUnit.DAYS)
                : lastSyncTime.toInstant(ZoneOffset.UTC)).toString();
        String nextToken = null;
        int syncedCount = 0;
        do {
            GetOrdersResponse response = ordersApi.getOrders(List.of(marketplace.getMarketplaceId()), null, null,
                    lastUpdatedAfter, null, null, null, null, null, null, 100, null, null, nextToken,
                    null, null, null, null, null, null, null, null, null);
            OrdersList payload = response.getPayload();
            for (Order order : payload == null || payload.getOrders() == null
                    ? Collections.<Order>emptyList() : payload.getOrders()) {
                syncOrder(shop.getId(), order);
                syncedCount++;
            }
            nextToken = payload == null ? null : payload.getNextToken();
        } while (nextToken != null && !nextToken.isBlank());
        return syncedCount;
    }

    /**
     * 将一个 Amazon 订单映射并按店铺、订单号幂等保存。
     *
     * @param shopId 归属店铺编号
     * @param order Amazon API 返回的订单
     */
    private void syncOrder(Long shopId, Order order) {
        if (order.getAmazonOrderId() == null || order.getAmazonOrderId().isBlank()) {
            log.warn("[syncOrder][忽略未返回 AmazonOrderId 的店铺({})订单]", shopId);
            return;
        }
        AmazonOrderDO localOrder = new AmazonOrderDO();
        localOrder.setShopId(shopId);
        localOrder.setAmazonOrderId(order.getAmazonOrderId());
        localOrder.setMarketplaceId(order.getMarketplaceId());
        localOrder.setPurchaseDate(parseAmazonTime(order.getPurchaseDate()));
        localOrder.setLastUpdateDate(parseAmazonTime(order.getLastUpdateDate()));
        localOrder.setOrderStatus(enumValue(order.getOrderStatus()));
        localOrder.setFulfillmentChannel(enumValue(order.getFulfillmentChannel()));
        localOrder.setNumberOfItemsShipped(order.getNumberOfItemsShipped());
        localOrder.setNumberOfItemsUnshipped(order.getNumberOfItemsUnshipped());
        localOrder.setPaymentMethod(enumValue(order.getPaymentMethod()));
        localOrder.setShipmentServiceLevelCategory(order.getShipmentServiceLevelCategory());
        localOrder.setOrderType(enumValue(order.getOrderType()));
        localOrder.setEarliestShipDate(parseAmazonTime(order.getEarliestShipDate()));
        localOrder.setLatestShipDate(parseAmazonTime(order.getLatestShipDate()));
        localOrder.setIsBusinessOrder(order.isIsBusinessOrder());
        localOrder.setIsPrime(order.isIsPrime());
        localOrder.setIsAccessPointOrder(order.isIsAccessPointOrder());
        localOrder.setIsGlobalExpressEnabled(order.isIsGlobalExpressEnabled());
        localOrder.setIsPremiumOrder(order.isIsPremiumOrder());
        localOrder.setIsSoldByAB(order.isIsSoldByAB());
        localOrder.setIsIBA(order.isIsIBA());
        localOrder.setPaymentMethodDetails(json(order.getPaymentMethodDetails()));
        localOrder.setShippingAddress(json(order.getShippingAddress()));
        localOrder.setBuyerInfo(json(order.getBuyerInfo()));
        localOrder.setLastSyncTime(LocalDateTime.now());

        AmazonOrderDO existing = amazonOrderMapper.selectByShopIdAndAmazonOrderId(shopId, order.getAmazonOrderId());
        if (existing == null) {
            amazonOrderMapper.insert(localOrder);
        } else {
            localOrder.setId(existing.getId());
            amazonOrderMapper.updateById(localOrder);
        }
    }

    /** 将 Amazon API 返回的对象或数组序列化为数据库 JSON 文本。 */
    private String json(Object value) {
        return value == null ? null : JsonUtils.toJsonString(value);
    }

    /** 将 Amazon ISO-8601 UTC 时间转换为数据库时间。 */
    private LocalDateTime parseAmazonTime(String value) {
        return value == null || value.isBlank() ? null : LocalDateTime.ofInstant(Instant.parse(value), ZoneOffset.UTC);
    }

    /** 读取 Amazon SDK 枚举的稳定字符串值。 */
    private String enumValue(Enum<?> value) {
        return value == null ? null : value.toString();
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
