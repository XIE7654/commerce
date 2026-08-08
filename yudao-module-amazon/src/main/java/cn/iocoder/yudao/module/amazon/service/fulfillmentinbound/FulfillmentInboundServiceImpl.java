package cn.iocoder.yudao.module.amazon.service.fulfillmentinbound;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.service.fulfillment.AmazonFulfillmentApiServiceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Amazon Fulfillment Inbound v0 与 v2024-03-20 API 服务实现。 */
@Service
public class FulfillmentInboundServiceImpl extends AmazonFulfillmentApiServiceSupport implements FulfillmentInboundService {

    private static final Map<String, OperationDefinition> OPERATIONS = Map.ofEntries(
            entry("getPrepInstructions", HttpMethod.GET, "/fba/inbound/v0/prepInstructions"),
            entry("getLabels", HttpMethod.GET, "/fba/inbound/v0/shipments/{shipmentId}/labels"),
            entry("getBillOfLading", HttpMethod.GET, "/fba/inbound/v0/shipments/{shipmentId}/billOfLading"),
            entry("getShipments", HttpMethod.GET, "/fba/inbound/v0/shipments"),
            entry("getShipmentItemsByShipmentId", HttpMethod.GET, "/fba/inbound/v0/shipments/{shipmentId}/items"),
            entry("getShipmentItems", HttpMethod.GET, "/fba/inbound/v0/shipmentItems"),
            entry("listInboundPlans", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans"),
            entry("createInboundPlan", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans"),
            entry("getInboundPlan", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}"),
            entry("listInboundPlanBoxes", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/boxes"),
            entry("cancelInboundPlan", HttpMethod.PUT, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/cancellation"),
            entry("listInboundPlanItems", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/items"),
            entry("updateInboundPlanName", HttpMethod.PUT, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/name"),
            entry("listPackingGroupBoxes", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/packingGroups/{packingGroupId}/boxes"),
            entry("listPackingGroupItems", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/packingGroups/{packingGroupId}/items"),
            entry("setPackingInformation", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/packingInformation"),
            entry("listPackingOptions", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/packingOptions"),
            entry("generatePackingOptions", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/packingOptions"),
            entry("confirmPackingOption", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/packingOptions/{packingOptionId}/confirmation"),
            entry("listInboundPlanPallets", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/pallets"),
            entry("listPlacementOptions", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/placementOptions"),
            entry("generatePlacementOptions", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/placementOptions"),
            entry("confirmPlacementOption", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/placementOptions/{placementOptionId}/confirmation"),
            entry("getShipment", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}"),
            entry("listShipmentBoxes", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/boxes"),
            entry("listShipmentContentUpdatePreviews", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/contentUpdatePreviews"),
            entry("generateShipmentContentUpdatePreviews", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/contentUpdatePreviews"),
            entry("getShipmentContentUpdatePreview", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/contentUpdatePreviews/{contentUpdatePreviewId}"),
            entry("confirmShipmentContentUpdatePreview", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/contentUpdatePreviews/{contentUpdatePreviewId}/confirmation"),
            entry("getDeliveryChallanDocument", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/deliveryChallanDocument"),
            entry("listDeliveryWindowOptions", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/deliveryWindowOptions"),
            entry("generateDeliveryWindowOptions", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/deliveryWindowOptions"),
            entry("confirmDeliveryWindowOptions", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/deliveryWindowOptions/{deliveryWindowOptionId}/confirmation"),
            entry("listShipmentItems", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/items"),
            entry("updateShipmentName", HttpMethod.PUT, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/name"),
            entry("listShipmentPallets", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/pallets"),
            entry("cancelSelfShipAppointment", HttpMethod.PUT, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/selfShipAppointmentCancellation"),
            entry("getSelfShipAppointmentSlots", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/selfShipAppointmentSlots"),
            entry("generateSelfShipAppointmentSlots", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/selfShipAppointmentSlots"),
            entry("scheduleSelfShipAppointment", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/selfShipAppointmentSlots/{slotId}/schedule"),
            entry("updateShipmentSourceAddress", HttpMethod.PUT, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/sourceAddress"),
            entry("updateShipmentTrackingDetails", HttpMethod.PUT, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/shipments/{shipmentId}/trackingDetails"),
            entry("listTransportationOptions", HttpMethod.GET, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/transportationOptions"),
            entry("generateTransportationOptions", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/transportationOptions"),
            entry("confirmTransportationOptions", HttpMethod.POST, "/inbound/fba/2024-03-20/inboundPlans/{inboundPlanId}/transportationOptions/confirmation"),
            entry("listItemComplianceDetails", HttpMethod.GET, "/inbound/fba/2024-03-20/items/compliance"),
            entry("updateItemComplianceDetails", HttpMethod.PUT, "/inbound/fba/2024-03-20/items/compliance"),
            entry("createMarketplaceItemLabels", HttpMethod.POST, "/inbound/fba/2024-03-20/items/labels"),
            entry("listPrepDetails", HttpMethod.GET, "/inbound/fba/2024-03-20/items/prepDetails"),
            entry("setPrepDetails", HttpMethod.POST, "/inbound/fba/2024-03-20/items/prepDetails"),
            entry("getInboundOperationStatus", HttpMethod.GET, "/inbound/fba/2024-03-20/operations/{operationId}")
    );

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(String operation, AmazonFulfillmentApiReqVO request) {
        OperationDefinition definition = OPERATIONS.get(operation);
        if (definition == null) {
            throw new IllegalArgumentException("不支持的 Fulfillment Inbound operation: " + operation);
        }
        return invoke(request, operation, definition, AmazonApiCategory.FULFILLMENT_INBOUND, "fulfillment-inbound-" + operation);
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
