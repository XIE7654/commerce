package cn.iocoder.yudao.module.amazon.service.vendorretailprocurement;

import cn.iocoder.yudao.module.amazon.controller.admin.vendorretailprocurement.vo.VendorRetailProcurementRequestVO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Amazon Vendor Retail Procurement Orders、Invoices、Shipments 与 Transaction Status 服务实现。 */
@Service
public class VendorRetailProcurementServiceImpl implements VendorRetailProcurementService {

    private static final String ORDERS_PATH = "/vendor/orders/v1";
    private static final String INVOICES_PATH = "/vendor/payments/v1";
    private static final String SHIPMENTS_PATH = "/vendor/shipping/v1";
    private static final String TRANSACTIONS_PATH = "/vendor/transactions/v1";
    private static final Set<String> PURCHASE_ORDER_QUERY = fields("limit", "createdAfter", "createdBefore", "sortOrder", "nextToken", "includeDetails", "changedAfter", "changedBefore", "poItemState", "isPOChanged", "purchaseOrderState", "orderingVendorCode");
    private static final Set<String> PURCHASE_ORDER_STATUS_QUERY = fields("limit", "sortOrder", "nextToken", "createdAfter", "createdBefore", "updatedAfter", "updatedBefore", "purchaseOrderNumber", "purchaseOrderStatus", "itemConfirmationStatus", "itemReceiveStatus", "orderingVendorCode", "shipToPartyId");
    private static final Set<String> SHIPMENT_QUERY = fields("limit", "sortOrder", "nextToken", "createdAfter", "createdBefore", "shipmentConfirmedBefore", "shipmentConfirmedAfter", "packageLabelCreatedBefore", "packageLabelCreatedAfter", "shippedBefore", "shippedAfter", "estimatedDeliveryBefore", "estimatedDeliveryAfter", "shipmentDeliveryBefore", "shipmentDeliveryAfter", "requestedPickUpBefore", "requestedPickUpAfter", "scheduledPickUpBefore", "scheduledPickUpAfter", "currentShipmentStatus", "vendorShipmentIdentifier", "buyerReferenceNumber", "buyerWarehouseCode", "sellerWarehouseCode");
    private static final Set<String> LABEL_QUERY = fields("limit", "sortOrder", "nextToken", "labelCreatedAfter", "labelCreatedBefore", "buyerReferenceNumber", "vendorShipmentIdentifier", "sellerWarehouseCode");

    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override public Map<String, Object> getPurchaseOrders(VendorRetailProcurementRequestVO request) { return get(request, ORDERS_PATH + "/purchaseOrders", PURCHASE_ORDER_QUERY, "getPurchaseOrders", "vendor-purchase-orders"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> getPurchaseOrder(VendorRetailProcurementRequestVO request) { return get(request, ORDERS_PATH + "/purchaseOrders/" + pathValue(request.getPurchaseOrderNumber(), "purchaseOrderNumber"), Set.of(), "getPurchaseOrder", "vendor-purchase-order"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> submitAcknowledgement(VendorRetailProcurementRequestVO request) { return post(request, ORDERS_PATH + "/acknowledgements", "submitAcknowledgement", "vendor-acknowledgement"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> getPurchaseOrdersStatus(VendorRetailProcurementRequestVO request) { return get(request, ORDERS_PATH + "/purchaseOrdersStatus", PURCHASE_ORDER_STATUS_QUERY, "getPurchaseOrdersStatus", "vendor-purchase-order-status"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> submitInvoices(VendorRetailProcurementRequestVO request) { return post(request, INVOICES_PATH + "/invoices", "submitInvoices", "vendor-invoices"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> submitShipmentConfirmations(VendorRetailProcurementRequestVO request) { return post(request, SHIPMENTS_PATH + "/shipmentConfirmations", "SubmitShipmentConfirmations", "vendor-shipment-confirmations"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> submitShipments(VendorRetailProcurementRequestVO request) { return post(request, SHIPMENTS_PATH + "/shipments", "SubmitShipments", "vendor-shipments"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> getShipmentDetails(VendorRetailProcurementRequestVO request) { return get(request, SHIPMENTS_PATH + "/shipments", SHIPMENT_QUERY, "GetShipmentDetails", "vendor-shipment-details"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> getShipmentLabels(VendorRetailProcurementRequestVO request) { return get(request, SHIPMENTS_PATH + "/transportLabels", LABEL_QUERY, "GetShipmentLabels", "vendor-shipment-labels"); }
    /** {@inheritDoc} */
    @Override public Map<String, Object> getTransaction(VendorRetailProcurementRequestVO request) { return get(request, TRANSACTIONS_PATH + "/transactions/" + pathValue(request.getTransactionId(), "transactionId"), Set.of(), "getTransaction", "vendor-transaction"); }

    /** 调用 Vendor 只读操作，只接受当前 OpenAPI 操作声明的查询参数以阻止错误请求。 */
    private Map<String, Object> get(VendorRetailProcurementRequestVO request, String path, Set<String> allowedQuery, String operation, String storageName) {
        RequestContext context = context(request);
        return amazonSellingPartnerClient.getByCategory(uri(context.marketplace(), path, query(request, allowedQuery)), context.accessToken(), AmazonApiCategory.VENDOR_RETAIL_PROCUREMENT, operation, storageName, context.shopId(), request.getCountryCode(), context.marketplace().getMarketplaceId());
    }

    /** 调用 Vendor 写操作；异步确认、发票和货件必须保留 Amazon 定义的完整原始请求体。 */
    private Map<String, Object> post(VendorRetailProcurementRequestVO request, String path, String operation, String storageName) {
        if (request.getBody() == null || request.getBody().isEmpty()) throw new IllegalArgumentException("body 不能为空");
        RequestContext context = context(request);
        return amazonSellingPartnerClient.mutateByCategory(uri(context.marketplace(), path, Map.of()), context.accessToken(), HttpMethod.POST, request.getBody(), AmazonApiCategory.VENDOR_RETAIL_PROCUREMENT, operation, storageName, context.shopId(), request.getCountryCode(), context.marketplace().getMarketplaceId());
    }

    /** 校验店铺与区域端点，并使用该店铺的 LWA 授权令牌避免跨租户调用。 */
    private RequestContext context(VendorRetailProcurementRequestVO request) {
        AmazonShopDO shop = amazonShopMapper.selectById(request.getShopId());
        if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + request.getShopId());
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(request.getCountryCode());
        if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + request.getCountryCode());
        return new RequestContext(shop.getId(), amazonOAuthService.getSellerAccessToken(shop.getId()), marketplace);
    }

    /** 过滤空值及未被模型声明的参数，确保请求与指定 API 版本一致。 */
    private Map<String, String> query(VendorRetailProcurementRequestVO request, Set<String> allowedQuery) {
        Map<String, String> result = new LinkedHashMap<>();
        if (request.getQuery() == null) return result;
        request.getQuery().forEach((key, value) -> { if (allowedQuery.contains(key) && value != null && !value.isBlank()) result.put(key, value); });
        return result;
    }

    /** 将路径及查询参数转换为端点 URI，并对可变数据进行百分号编码。 */
    private URI uri(AmazonMarketplaceEnum marketplace, String path, Map<String, String> query) {
        String parameters = query.entrySet().stream().map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue())).collect(Collectors.joining("&"));
        return URI.create(marketplace.getEndpoint() + path + (parameters.isEmpty() ? "" : "?" + parameters));
    }

    /** 校验并编码路径变量，防止采购订单或交易编号改变 URI 路径结构。 */
    private String pathValue(String value, String fieldName) { if (value == null || value.isBlank()) throw new IllegalArgumentException(fieldName + " 不能为空"); return encode(value.trim()); }
    /** 使用 UTF-8 对 URL 组件编码。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~"); }
    /** 将模型中的查询字段常量化，便于每个端点独立校验。 */
    private static Set<String> fields(String... values) { return Set.copyOf(Arrays.asList(values)); }

    /** Vendor 调用所需的店铺、令牌与区域端点上下文。 */
    private record RequestContext(Long shopId, String accessToken, AmazonMarketplaceEnum marketplace) { }
}
