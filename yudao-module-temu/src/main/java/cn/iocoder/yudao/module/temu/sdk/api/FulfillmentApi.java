package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Fulfillment 业务接口服务。 */
public class FulfillmentApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public FulfillmentApi(TemuClient client) { super(client); }

    /**
     * 调用 bg.order.fulfillment.info.sync。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode fulfillmentInfoSync(Map<String, Object> params) { return call("bg.order.fulfillment.info.sync", params); }

    /**
     * 调用 bg.logistics.shipment.v2.confirm。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentV2Confirm(Map<String, Object> params) { return call("bg.logistics.shipment.v2.confirm", params); }

    /**
     * 调用 bg.logistics.shipment.sub.confirm。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentSubConfirm(Map<String, Object> params) { return call("bg.logistics.shipment.sub.confirm", params); }

    /**
     * 调用 bg.logistics.shipment.shippingtype.update。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentShippingtypeUpdate(Map<String, Object> params) { return call("bg.logistics.shipment.shippingtype.update", params); }

    /**
     * 调用 bg.logistics.shipment.create。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentCreate(Map<String, Object> params) { return call("bg.logistics.shipment.create", params); }

    /**
     * 调用 bg.logistics.shipment.result.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentResultGet(Map<String, Object> params) { return call("bg.logistics.shipment.result.get", params); }

    /**
     * 调用 bg.logistics.shipment.update。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentUpdate(Map<String, Object> params) { return call("bg.logistics.shipment.update", params); }

    /**
     * 调用 bg.logistics.shipment.document.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentDocumentGet(Map<String, Object> params) { return call("bg.logistics.shipment.document.get", params); }

    /**
     * 调用 bg.logistics.shipment.v2.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipmentV2Get(Map<String, Object> params) { return call("bg.logistics.shipment.v2.get", params); }

    /**
     * 调用 bg.order.unshipped.package.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode unshippedPackageGet(Map<String, Object> params) { return call("bg.order.unshipped.package.get", params); }

    /**
     * 调用 bg.logistics.shipped.package.confirm。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shippedPackageConfirm(Map<String, Object> params) { return call("bg.logistics.shipped.package.confirm", params); }

}
