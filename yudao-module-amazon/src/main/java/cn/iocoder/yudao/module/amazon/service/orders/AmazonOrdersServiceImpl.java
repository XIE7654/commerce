package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentConfirmationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderRegulatedInfoUpdateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrder2026GetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrders2026ListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.service.shop.AmazonShopService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static cn.hutool.core.collection.CollUtil.isEmpty;
import static cn.hutool.core.util.StrUtil.isBlank;
import static cn.iocoder.yudao.module.amazon.utils.AmazonDateTimeUtils.validateRange;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.buildQuery;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.join;
import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.putIfNotBlank;

/**
 * Amazon Orders 服务实现。
 *
 * <p>订单买家信息和地址由 Amazon 按授权范围脱敏或拒绝访问，服务层只转发其原始响应，不自行缓存敏感业务字段。</p>
 */
@Service
public class AmazonOrdersServiceImpl implements AmazonOrdersService {

    private static final String ORDERS_PATH = "/orders/v0/orders";
    private static final String ORDERS_2026_PATH = "/orders/2026-01-01/orders";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopService amazonShopService;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrders(AmazonOrdersListReqVO request) {
        validateListRequest(request);
        return execute(request.getShopId(), request.getCountryCode(), buildListUri(request), "getOrders", "orders");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrder(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "", "getOrder", "order");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderItems(AmazonOrderItemsReqVO request) {
        return executeOrderItemsRequest(request, "/orderItems", "getOrderItems", "order-items");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderItemsBuyerInfo(AmazonOrderItemsReqVO request) {
        return executeOrderItemsRequest(request, "/orderItems/buyerInfo", "getOrderItemsBuyerInfo", "order-items-buyer-info");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderBuyerInfo(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "/buyerInfo", "getOrderBuyerInfo", "order-buyer-info");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderAddress(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "/address", "getOrderAddress", "order-address");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderRegulatedInfo(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "/regulatedInfo", "getOrderRegulatedInfo", "order-regulated-info");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> updateShipmentStatus(AmazonOrderShipmentReqVO request) {
        Map<String, Object> body = new TreeMap<>();
        body.put("marketplaceId", amazonShopService.requireMarketplace(request.getCountryCode()).getMarketplaceId());
        body.put("shipmentStatus", request.getShipmentStatus());
        if (!isEmpty(request.getOrderItems())) {
            body.put("orderItems", request.getOrderItems());
        }
        return executeMutation(request, "/shipment", HttpMethod.POST, body, "updateShipmentStatus");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> confirmShipment(AmazonOrderShipmentConfirmationReqVO request) {
        Map<String, Object> body = new TreeMap<>();
        body.put("marketplaceId", amazonShopService.requireMarketplace(request.getCountryCode()).getMarketplaceId());
        body.put("packageDetail", request.getPackageDetail());
        if (!isBlank(request.getCodCollectionMethod())) {
            body.put("codCollectionMethod", request.getCodCollectionMethod());
        }
        return executeMutation(request, "/shipmentConfirmation", HttpMethod.POST, body, "confirmShipment");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> updateOrderRegulatedInfo(AmazonOrderRegulatedInfoUpdateReqVO request) {
        return executeMutation(request, "/regulatedInfo", HttpMethod.PATCH,
                Map.of("regulatedOrderVerificationStatus", request.getRegulatedOrderVerificationStatus()),
                "updateVerificationStatus");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrders2026(AmazonOrders2026ListReqVO request) {
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "createdAfter", request.getCreatedAfter());
        putIfNotBlank(query, "createdBefore", request.getCreatedBefore());
        putIfNotBlank(query, "lastUpdatedAfter", request.getLastUpdatedAfter());
        putIfNotBlank(query, "lastUpdatedBefore", request.getLastUpdatedBefore());
        putIfNotBlank(query, "fulfillmentStatuses", join(request.getFulfillmentStatuses()));
        putIfNotBlank(query, "marketplaceIds", marketplace.getMarketplaceId());
        putIfNotBlank(query, "fulfilledBy", join(request.getFulfilledBy()));
        putIfNotBlank(query, "maxResultsPerPage", request.getMaxResultsPerPage() == null ? null : request.getMaxResultsPerPage().toString());
        putIfNotBlank(query, "paginationToken", request.getPaginationToken());
        putIfNotBlank(query, "includedData", join(request.getIncludedData()));
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_2026_PATH + "?" + buildQuery(query));
        return execute(request.getShopId(), request.getCountryCode(), uri, "getOrders2026", "orders-2026");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrder2026(AmazonOrder2026GetReqVO request) {
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "includedData", join(request.getIncludedData()));
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_2026_PATH + "/" + orderId + "?" + buildQuery(query));
        return execute(request.getShopId(), request.getCountryCode(), uri, "getOrder2026", "order-2026");
    }

    /**
     * 调用指定订单子资源，确保所有订单详情接口使用相同的店铺授权和站点解析规则。
     *
     * @param request 店铺、国家代码和 Amazon 订单编号
     * @param suffix 订单资源路径后缀
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> executeOrderRequest(AmazonOrderGetReqVO request, String suffix, String operationName,
                                                    String storageName) {
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_PATH + "/" + orderId + suffix);
        return execute(request.getShopId(), request.getCountryCode(), uri, operationName, storageName);
    }

    /**
     * 调用订单商品资源并传递 Amazon 返回的分页令牌。
     *
     * @param request 订单商品查询参数
     * @param suffix 订单资源路径后缀
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> executeOrderItemsRequest(AmazonOrderItemsReqVO request, String suffix, String operationName,
                                                         String storageName) {
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        Map<String, String> query = new TreeMap<>();
        putIfNotBlank(query, "NextToken", request.getNextToken());
        String queryString = query.isEmpty() ? "" : "?" + buildQuery(query);
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_PATH + "/" + orderId + suffix + queryString);
        return execute(request.getShopId(), request.getCountryCode(), uri, operationName, storageName);
    }

    /**
     * 调用指定订单写接口，并复用店铺授权、站点解析和审计归档链路。
     *
     * @param request 订单及站点定位参数
     * @param suffix 订单资源路径后缀
     * @param method HTTP 请求方式
     * @param body Amazon 请求体
     * @param operationName Amazon 操作名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> executeMutation(AmazonOrderGetReqVO request, String suffix, HttpMethod method,
                                                Map<String, Object> body, String operationName) {
        AmazonShopDO shop = amazonShopService.requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_PATH + "/" + orderId + suffix);
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        return amazonSellingPartnerClient.mutateOrders(uri, accessToken, method, body, operationName, shop.getId(),
                request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 使用店铺 Seller Token 调用 Orders API，并将响应交由统一客户端审计和归档。
     *
     * @param shopId 当前租户的店铺编号
     * @param countryCode 站点国家代码
     * @param uri 已构造的 Orders API 地址
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> execute(Long shopId, String countryCode, URI uri, String operationName, String storageName) {
        AmazonShopDO shop = amazonShopService.requireShop(shopId);
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(countryCode);
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        return amazonSellingPartnerClient.getOrders(uri, accessToken, operationName, storageName, shop.getId(), countryCode,
                marketplace.getMarketplaceId());
    }

    /**
     * 构造订单列表 URI；Amazon 要求以站点 Marketplace ID 作为必传筛选条件。
     *
     * @param request 订单列表请求参数
     * @return 可直接发起请求的 URI
     */
    private URI buildListUri(AmazonOrdersListReqVO request) {
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        Map<String, String> query = new TreeMap<>();
        query.put("MarketplaceIds", marketplace.getMarketplaceId());
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
        return URI.create(marketplace.getEndpoint() + ORDERS_PATH + "?" + buildQuery(query));
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

}
