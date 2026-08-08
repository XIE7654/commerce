package cn.iocoder.yudao.module.amazon.service.fulfillmentoutbound;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.service.fulfillment.AmazonFulfillmentApiServiceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Amazon Fulfillment Outbound v2020-07-01 与 v2026-07-04 API 服务实现。 */
@Service
public class FulfillmentOutboundServiceImpl extends AmazonFulfillmentApiServiceSupport implements FulfillmentOutboundService {

    private static final Map<String, OperationDefinition> OPERATIONS = Map.ofEntries(
            entry("getFulfillmentPreview", HttpMethod.POST, "/fba/outbound/2020-07-01/fulfillmentOrders/preview"),
            entry("deliveryOffers", HttpMethod.POST, "/fba/outbound/2020-07-01/deliveryOffers"),
            entry("listAllFulfillmentOrders", HttpMethod.GET, "/fba/outbound/2020-07-01/fulfillmentOrders"),
            entry("createFulfillmentOrder", HttpMethod.POST, "/fba/outbound/2020-07-01/fulfillmentOrders"),
            entry("getPackageTrackingDetails", HttpMethod.GET, "/fba/outbound/2020-07-01/tracking"),
            entry("listReturnReasonCodes", HttpMethod.GET, "/fba/outbound/2020-07-01/returnReasonCodes"),
            entry("createFulfillmentReturn", HttpMethod.PUT, "/fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}/return"),
            entry("getFulfillmentOrder", HttpMethod.GET, "/fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}"),
            entry("updateFulfillmentOrder", HttpMethod.PUT, "/fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}"),
            entry("cancelFulfillmentOrder", HttpMethod.PUT, "/fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}/cancel"),
            entry("submitFulfillmentOrderStatusUpdate", HttpMethod.PUT, "/fba/outbound/2020-07-01/fulfillmentOrders/{sellerFulfillmentOrderId}/status"),
            entry("getFeatures", HttpMethod.GET, "/fba/outbound/2020-07-01/features"),
            entry("getFeatureInventory", HttpMethod.GET, "/fba/outbound/2020-07-01/features/inventory/{featureName}"),
            entry("getFeatureSKU", HttpMethod.GET, "/fba/outbound/2020-07-01/features/inventory/{featureName}/{sellerSku}"),
            entry("getOrderPreview", HttpMethod.POST, "/fulfillment/outbound/2026-07-04/previews"),
            entry("getOffers", HttpMethod.POST, "/fulfillment/outbound/2026-07-04/offers"),
            entry("cancelOrder", HttpMethod.PUT, "/fulfillment/outbound/2026-07-04/orders/{orderId}/cancel"),
            entry("updateOrderStatus", HttpMethod.PUT, "/fulfillment/outbound/2026-07-04/orders/{orderId}/status"),
            entry("updatePackage", HttpMethod.PUT, "/fulfillment/outbound/2026-07-04/orders/{orderId}/packages/{packageId}"),
            entry("updateOrder", HttpMethod.PUT, "/fulfillment/outbound/2026-07-04/orders/{orderId}"),
            entry("getOrder", HttpMethod.GET, "/fulfillment/outbound/2026-07-04/orders/{orderId}"),
            entry("listOrders", HttpMethod.GET, "/fulfillment/outbound/2026-07-04/orders"),
            entry("createOrder", HttpMethod.POST, "/fulfillment/outbound/2026-07-04/orders")
    );

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(String operation, AmazonFulfillmentApiReqVO request) {
        OperationDefinition definition = OPERATIONS.get(operation);
        if (definition == null) {
            throw new IllegalArgumentException("不支持的 Fulfillment Outbound operation: " + operation);
        }
        return invoke(request, operation, definition, AmazonApiCategory.FULFILLMENT_OUTBOUND, "fulfillment-outbound-" + operation);
    }

    /**
     * 创建不可变的 operation 路由定义。
     *
     * @param operation Amazon operationId
     * @param method 对应 HTTP 方法
     * @param path 模型定义的资源路径
     * @return operation 到路由定义的映射项
     */
    private static Map.Entry<String, OperationDefinition> entry(String operation, HttpMethod method, String path) {
        return Map.entry(operation, new OperationDefinition(method, path));
    }
}
