package cn.iocoder.yudao.module.temu.framework.client.api;

import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import org.springframework.http.HttpMethod;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Temu 订单 API。 */
public class OrderApi {

    private final TemuClient client;

    /**
     * 创建订单 API。
     *
     * @param client Temu 传输客户端
     */
    public OrderApi(TemuClient client) {
        this.client = client;
    }

    /**
     * 查询订单列表。
     *
     * @param params 订单状态、区域、分页和更新时间等 Temu 请求参数
     * @return Temu 原始订单列表响应
     */
    public JsonNode listOrdersV2(Map<String, ?> params) {
        return request("bg.order.list.v2.get", params);
    }

    /**
     * 查询父订单详情。
     *
     * @param params 包含 parentOrderSn 的 Temu 请求参数
     * @return Temu 原始订单详情响应
     */
    public JsonNode detailOrderV2(Map<String, ?> params) {
        return request("bg.order.detail.v2.get", params);
    }

    /**
     * 查询父订单收货信息。
     *
     * @param params 包含 parentOrderSn 的 Temu 请求参数
     * @return Temu 原始收货信息响应
     */
    public JsonNode shippinginfoOrderV2(Map<String, ?> params) {
        return request("bg.order.shippinginfo.v2.get", params);
    }

    /**
     * 查询合单发货订单列表。
     *
     * @param params Temu 合单发货查询参数
     * @return Temu 原始合单发货响应
     */
    public JsonNode combinedshipmentListOrder(Map<String, ?> params) {
        return request("bg.order.combinedshipment.list.get", params);
    }

    /**
     * 查询定制订单详情。
     *
     * @param params 包含 orderSnList 的 Temu 请求参数
     * @return Temu 原始定制订单响应
     */
    public JsonNode customizationOrder(Map<String, ?> params) {
        return request("bg.order.customization.get", params);
    }

    /**
     * 解密订单收货信息。
     *
     * @param params Temu 收货信息解密参数
     * @return Temu 原始解密响应
     */
    public JsonNode decryptshippinginfoOrder(Map<String, ?> params) {
        return request("bg.order.decryptshippinginfo.get", params);
    }

    /**
     * 通过新版 client 发送订单 Router 请求；空参数统一转换为空 Map。
     *
     * @param apiType Temu API type
     * @param params 业务请求参数
     * @return Temu Router 原始响应
     */
    private JsonNode request(String apiType, Map<String, ?> params) {
        return client.request(apiType, HttpMethod.POST, params == null ? Collections.emptyMap() : params);
    }
}
