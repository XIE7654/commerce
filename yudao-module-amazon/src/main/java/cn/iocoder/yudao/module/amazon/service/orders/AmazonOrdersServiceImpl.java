package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentConfirmationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderRegulatedInfoUpdateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrder2026GetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrders2026ListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopWithMarketplacesDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
import cn.iocoder.yudao.module.amazon.sdk.orders.AmazonOrdersApi;
import cn.iocoder.yudao.module.amazon.sdk.orders.AmazonOrdersRequest;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static cn.hutool.core.collection.CollUtil.isEmpty;
import static cn.hutool.core.util.StrUtil.isBlank;
import static cn.iocoder.yudao.module.amazon.utils.AmazonDateTimeUtils.validateRange;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.join;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.putIfNotBlank;

/**
 * Amazon Orders 服务实现。
 *
 * <p>订单买家信息和地址由 Amazon 按授权范围脱敏或拒绝访问，服务层只转发其原始响应，不自行缓存敏感业务字段。</p>
 */
@Service
public class AmazonOrdersServiceImpl implements AmazonOrdersService {

    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonOAuthService amazonOAuthService;

    @Resource
    private AmazonShopMapper shopMapper;

    @Resource
    private AmazonOrdersApi amazonOrdersApi;

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrders(AmazonOrdersListReqVO request) {
        validateListRequest(request);
        RequestContext context = resolveReadContext(request.getShopId());
        return amazonOrdersApi.getOrders(sdkRequest(context, null, null, buildListQuery(request, context.marketplaceIds()),
                null, "getOrders", "orders"));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrder(AmazonOrderGetReqVO request) {
        return getOrderResource(request, "", "getOrder", "order", amazonOrdersApi::getOrder);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrderItems(AmazonOrderItemsReqVO request) {
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "NextToken", request.getNextToken());
        return getOrderResource(request, "/orderItems", "getOrderItems", "order-items", amazonOrdersApi::getOrderItems, query);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrderItemsBuyerInfo(AmazonOrderItemsReqVO request) {
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "NextToken", request.getNextToken());
        return getOrderResource(request, "/orderItems/buyerInfo", "getOrderItemsBuyerInfo", "order-items-buyer-info",
                amazonOrdersApi::getOrderItemsBuyerInfo, query);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrderBuyerInfo(AmazonOrderGetReqVO request) {
        return getOrderResource(request, "/buyerInfo", "getOrderBuyerInfo", "order-buyer-info", amazonOrdersApi::getOrderBuyerInfo);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrderAddress(AmazonOrderGetReqVO request) {
        return getOrderResource(request, "/address", "getOrderAddress", "order-address", amazonOrdersApi::getOrderAddress);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrderRegulatedInfo(AmazonOrderGetReqVO request) {
        return getOrderResource(request, "/regulatedInfo", "getOrderRegulatedInfo", "order-regulated-info",
                amazonOrdersApi::getOrderRegulatedInfo);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> updateShipmentStatus(AmazonOrderShipmentReqVO request) {
        RequestContext context = resolveWriteContext(request.getShopId(), request.getCountryCode());
        Map<String, Object> body = new TreeMap<>();
        body.put("marketplaceId", context.marketplaceId());
        body.put("shipmentStatus", request.getShipmentStatus());
        if (!isEmpty(request.getOrderItems())) {
            body.put("orderItems", request.getOrderItems());
        }
        return amazonOrdersApi.updateShipmentStatus(sdkRequest(context, request.getOrderId(), "/shipment", Map.of(), body,
                "updateShipmentStatus", "update-shipment-status"));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> confirmShipment(AmazonOrderShipmentConfirmationReqVO request) {
        RequestContext context = resolveWriteContext(request.getShopId(), request.getCountryCode());
        Map<String, Object> body = new TreeMap<>();
        body.put("marketplaceId", context.marketplaceId());
        body.put("packageDetail", request.getPackageDetail());
        if (!isBlank(request.getCodCollectionMethod())) {
            body.put("codCollectionMethod", request.getCodCollectionMethod());
        }
        return amazonOrdersApi.confirmShipment(sdkRequest(context, request.getOrderId(), "/shipmentConfirmation", Map.of(), body,
                "confirmShipment", "confirm-shipment"));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> updateOrderRegulatedInfo(AmazonOrderRegulatedInfoUpdateReqVO request) {
        RequestContext context = resolveWriteContext(request.getShopId(), request.getCountryCode());
        return amazonOrdersApi.updateVerificationStatus(sdkRequest(context, request.getOrderId(), "/regulatedInfo", Map.of(),
                Map.of("regulatedOrderVerificationStatus", request.getRegulatedOrderVerificationStatus()),
                "updateVerificationStatus", "update-verification-status"));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrders2026(AmazonOrders2026ListReqVO request) {
        RequestContext context = resolveReadContext(request.getShopId());
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "createdAfter", request.getCreatedAfter());
        putIfNotBlank(query, "createdBefore", request.getCreatedBefore());
        putIfNotBlank(query, "lastUpdatedAfter", request.getLastUpdatedAfter());
        putIfNotBlank(query, "lastUpdatedBefore", request.getLastUpdatedBefore());
        putIfNotBlank(query, "fulfillmentStatuses", join(request.getFulfillmentStatuses()));
        putIfNotBlank(query, "marketplaceIds", join(context.marketplaceIds()));
        putIfNotBlank(query, "fulfilledBy", join(request.getFulfilledBy()));
        putIfNotBlank(query, "maxResultsPerPage", request.getMaxResultsPerPage() == null ? null : request.getMaxResultsPerPage().toString());
        putIfNotBlank(query, "paginationToken", request.getPaginationToken());
        putIfNotBlank(query, "includedData", join(request.getIncludedData()));
        return amazonOrdersApi.searchOrders(sdkRequest(context, null, null, query, null, "searchOrders", "orders-2026"));
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> getOrder2026(AmazonOrder2026GetReqVO request) {
        RequestContext context = resolveReadContext(request.getShopId());
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "includedData", join(request.getIncludedData()));
        return amazonOrdersApi.getOrder2026(sdkRequest(context, request.getOrderId(), null, query, null,
                "getOrder2026", "order-2026"));
    }

    /**
     * 查询指定的 Orders v0 子资源。
     *
     * @param request 店铺和 Amazon 订单编号
     * @param path 订单资源路径后缀
     * @param operation Amazon 操作名称
     * @param storage 响应归档文件名称
     * @param query Orders SDK 读操作
     * @return Amazon 原始 JSON 响应
     */
    private AmazonApiResponse<Map<String, Object>> getOrderResource(AmazonOrderGetReqVO request, String path, String operation,
                                                                      String storage, OrdersQuery query) {
        return getOrderResource(request, path, operation, storage, query, Map.of());
    }

    /**
     * 查询指定的 Orders v0 子资源，并透传该资源的查询参数。
     *
     * @param request 店铺和 Amazon 订单编号
     * @param path 订单资源路径后缀
     * @param operation Amazon 操作名称
     * @param storage 响应归档文件名称
     * @param query Orders SDK 读操作
     * @param parameters Amazon 查询参数
     * @return Amazon 原始 JSON 响应
     */
    private AmazonApiResponse<Map<String, Object>> getOrderResource(AmazonOrderGetReqVO request, String path, String operation,
                                                                      String storage, OrdersQuery query, Map<String, String> parameters) {
        RequestContext context = resolveReadContext(request.getShopId());
        return query.execute(sdkRequest(context, request.getOrderId(), path, parameters, null, operation, storage));
    }

    /**
     * 构造 Orders SDK 请求上下文，集中注入店铺授权、端点和参与的 Marketplace。
     *
     * @param context 店铺、站点和授权上下文
     * @param orderId Amazon 订单编号；列表查询时为空
     * @param path 订单资源路径后缀
     * @param query Amazon 查询参数
     * @param body Amazon 请求体；读操作时为空
     * @param operation Amazon 操作名称
     * @param storage 响应归档文件名称
     * @return 已完成上下文注入的 SDK 请求
     */
    private AmazonOrdersRequest sdkRequest(RequestContext context, String orderId, String path, Map<String, String> query,
                                           Map<String, Object> body, String operation, String storage) {
        AmazonOrdersRequest request = new AmazonOrdersRequest();
        request.setShopId(context.shop().getId());
        request.setEndpoint(context.endpoint());
        request.setCountryCode(context.countryCode());
        request.setMarketplaceId(context.marketplaceId());
        request.setAccessToken(context.accessToken());
        request.setOrderId(orderId);
        request.setPath(path);
        request.setQuery(query);
        request.setBody(body);
        request.setOperation(operation);
        request.setStorage(storage);
        return request;
    }

    /**
     * 从启用店铺及其参与站点中解析读取请求上下文。
     *
     * <p>Marketplace ID 以 {@code amazon_shop_marketplace} 的同步记录为准，防止使用国家枚举推导出
     * 并未授权给该店铺的站点。</p>
     *
     * @param shopId 店铺编号
     * @return 包含店铺授权、区域端点和数据库 Marketplace ID 的上下文
     */
    private RequestContext resolveReadContext(Long shopId) {
        AmazonShopWithMarketplacesDO target = requireEnabledShop(shopId);
        AmazonMarketplaceEnum endpointMarketplace = AmazonMarketplaceEnum.fromSalesRegion(target.getShop().getRegion());
        if (endpointMarketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 销售区域: " + target.getShop().getRegion());
        }
        List<String> marketplaceIds = target.getParticipations().stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsParticipating()))
                .map(AmazonShopMarketplaceDO::getMarketplaceId)
                .filter(id -> AmazonMarketplaceEnum.fromMarketplaceId(id) != null)
                .filter(id -> endpointMarketplace.getSalesRegion().equals(AmazonMarketplaceEnum.fromMarketplaceId(id).getSalesRegion()))
                .distinct().toList();
        if (marketplaceIds.isEmpty()) {
            throw new IllegalArgumentException("店铺没有可查询的参与站点: " + shopId);
        }
        return new RequestContext(target.getShop(), null, marketplaceIds.getFirst(), marketplaceIds,
                amazonMarketplaceProvider.getEndpoint(endpointMarketplace), amazonOAuthService.getSellerAccessToken(shopId));
    }

    /**
     * 从启用店铺及其参与站点中解析写入请求上下文。
     *
     * @param shopId 店铺编号
     * @param countryCode 写入目标站点国家代码
     * @return 包含单一目标 Marketplace 的上下文
     */
    private RequestContext resolveWriteContext(Long shopId, String countryCode) {
        AmazonShopWithMarketplacesDO target = requireEnabledShop(shopId);
        AmazonShopMarketplaceDO participation = target.getParticipations().stream()
                .filter(item -> Boolean.TRUE.equals(item.getIsParticipating()))
                .filter(item -> countryCode.equalsIgnoreCase(item.getCountryCode()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("店铺未参与站点: " + countryCode));
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromMarketplaceId(participation.getMarketplaceId());
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon Marketplace ID: " + participation.getMarketplaceId());
        }
        return new RequestContext(target.getShop(), countryCode, participation.getMarketplaceId(), List.of(participation.getMarketplaceId()),
                amazonMarketplaceProvider.getEndpoint(marketplace), amazonOAuthService.getSellerAccessToken(shopId));
    }

    /**
     * 按传入店铺编号从启用店铺聚合结果中定位店铺。
     *
     * @param shopId 店铺编号
     * @return 含参与站点记录的启用店铺
     */
    private AmazonShopWithMarketplacesDO requireEnabledShop(Long shopId) {
        List<AmazonShopWithMarketplacesDO> shops = shopMapper.selectEnabledWithMarketplaces();
        return shops.stream()
                .filter(item -> item.getShop() != null && shopId.equals(item.getShop().getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Amazon 店铺不存在或未启用: " + shopId));
    }

    /**
     * 构造 Orders v0 列表查询参数；Marketplace ID 必须来自店铺参与站点记录。
     *
     * @param request 订单列表请求参数
     * @param marketplaceId 数据库中该店铺参与的 Marketplace ID
     * @return 传递给 Orders SDK 的查询参数
     */
    private Map<String, String> buildListQuery(AmazonOrdersListReqVO request, List<String> marketplaceIds) {
        Map<String, String> query = new TreeMap<>();
        query.put("MarketplaceIds", join(marketplaceIds));
        putIfNotBlank(query, "CreatedAfter", request.getCreatedAfter());
        putIfNotBlank(query, "CreatedBefore", request.getCreatedBefore());
        putIfNotBlank(query, "LastUpdatedAfter", request.getLastUpdatedAfter());
        putIfNotBlank(query, "LastUpdatedBefore", request.getLastUpdatedBefore());
        putIfNotBlank(query, "OrderStatuses", join(request.getOrderStatuses()));
        putIfNotBlank(query, "FulfillmentChannels", join(request.getFulfillmentChannels()));
        putIfNotBlank(query, "PaymentMethods", join(request.getPaymentMethods()));
        putIfNotBlank(query, "BuyerEmail", request.getBuyerEmail());
        putIfNotBlank(query, "SellerOrderId", request.getSellerOrderId());
        putIfNotBlank(query, "MaxResultsPerPage", request.getMaxResultsPerPage() == null ? null : request.getMaxResultsPerPage().toString());
        putIfNotBlank(query, "EasyShipShipmentStatuses", join(request.getEasyShipShipmentStatuses()));
        putIfNotBlank(query, "ElectronicInvoiceStatuses", join(request.getElectronicInvoiceStatuses()));
        putIfNotBlank(query, "NextToken", request.getNextToken());
        putIfNotBlank(query, "AmazonOrderIds", join(request.getAmazonOrderIds()));
        putIfNotBlank(query, "ActualFulfillmentSupplySourceId", request.getActualFulfillmentSupplySourceId());
        putIfNotBlank(query, "IsISPU", request.getIsISPU() == null ? null : request.getIsISPU().toString());
        putIfNotBlank(query, "StoreChainStoreId", request.getStoreChainStoreId());
        putIfNotBlank(query, "EarliestDeliveryDateBefore", request.getEarliestDeliveryDateBefore());
        putIfNotBlank(query, "EarliestDeliveryDateAfter", request.getEarliestDeliveryDateAfter());
        putIfNotBlank(query, "LatestDeliveryDateBefore", request.getLatestDeliveryDateBefore());
        putIfNotBlank(query, "LatestDeliveryDateAfter", request.getLatestDeliveryDateAfter());
        return query;
    }

    /**
     * 校验 Orders API 的时间筛选规则，避免 Amazon 忽略互斥条件造成查询范围偏差。
     *
     * @param request 订单列表请求参数
     */
    private void validateListRequest(AmazonOrdersListReqVO request) {
        if (!isBlank(request.getNextToken())) {
            return; // 分页令牌存在时，Amazon 只使用令牌继续读取，其他筛选条件不参与本次查询。
        }
        if (isBlank(request.getCreatedAfter()) && isBlank(request.getLastUpdatedAfter())) {
            throw new IllegalArgumentException("CreatedAfter 或 LastUpdatedAfter 必须传入一个");
        }
        if (!isBlank(request.getCreatedAfter()) && (!isBlank(request.getLastUpdatedAfter()) || !isBlank(request.getLastUpdatedBefore()))) {
            throw new IllegalArgumentException("CreatedAfter 不能与 LastUpdatedAfter 或 LastUpdatedBefore 同时传入");
        }
        validateRange(request.getCreatedAfter(), request.getCreatedBefore(), "CreatedAfter", "CreatedBefore");
        validateRange(request.getLastUpdatedAfter(), request.getLastUpdatedBefore(), "LastUpdatedAfter", "LastUpdatedBefore");
        validateRange(request.getEarliestDeliveryDateAfter(), request.getEarliestDeliveryDateBefore(),
                "EarliestDeliveryDateAfter", "EarliestDeliveryDateBefore");
        validateRange(request.getLatestDeliveryDateAfter(), request.getLatestDeliveryDateBefore(),
                "LatestDeliveryDateAfter", "LatestDeliveryDateBefore");
        if (!isBlank(request.getSellerOrderId()) && (!isEmpty(request.getFulfillmentChannels())
                || !isEmpty(request.getOrderStatuses()) || !isEmpty(request.getPaymentMethods())
                || !isBlank(request.getLastUpdatedAfter()) || !isBlank(request.getLastUpdatedBefore())
                || !isBlank(request.getBuyerEmail()))) {
            throw new IllegalArgumentException("SellerOrderId 不能与履行渠道、订单状态、支付方式、更新时间或买家邮箱同时传入");
        }
    }

    /** Orders SDK 查询操作，避免为每个订单子资源重复构造同类调用。 */
    @FunctionalInterface
    private interface OrdersQuery {

        /**
         * 执行一个已填充上下文的 Orders SDK 查询。
         *
         * @param request 已填充的 SDK 请求
         * @return Amazon 原始 JSON 响应
         */
        AmazonApiResponse<Map<String, Object>> execute(AmazonOrdersRequest request);
    }

    /** 订单调用使用的店铺授权和参与站点上下文。 */
    private record RequestContext(AmazonShopDO shop, String countryCode, String marketplaceId, List<String> marketplaceIds,
                                  String endpoint, String accessToken) {
    }

}
