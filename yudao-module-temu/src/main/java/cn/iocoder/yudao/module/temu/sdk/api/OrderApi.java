package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Order 业务接口服务。 */
public class OrderApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public OrderApi(TemuClient client) { super(client); }

    /**
     * 调用 bg.order.list.v2.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode listOrdersV2(Map<String, Object> params) { return call("bg.order.list.v2.get", params); }

    /**
     * 调用 bg.order.detail.v2.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode detailOrderV2(Map<String, Object> params) { return call("bg.order.detail.v2.get", params); }

    /**
     * 调用 bg.order.shippinginfo.v2.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shippinginfoOrderV2(Map<String, Object> params) { return call("bg.order.shippinginfo.v2.get", params); }

    /**
     * 调用 bg.order.combinedshipment.list.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode combinedshipmentListOrder(Map<String, Object> params) { return call("bg.order.combinedshipment.list.get", params); }

    /**
     * 调用 bg.order.customization.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode customizationOrder(Map<String, Object> params) { return call("bg.order.customization.get", params); }

    /**
     * 调用 bg.order.decryptshippinginfo.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode decryptshippinginfoOrder(Map<String, Object> params) { return call("bg.order.decryptshippinginfo.get", params); }

}
