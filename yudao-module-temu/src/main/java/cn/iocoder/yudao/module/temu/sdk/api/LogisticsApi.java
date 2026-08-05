package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Logistics 业务接口服务。 */
public class LogisticsApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public LogisticsApi(TemuClient client) { super(client); }

    /**
     * 调用 bg.logistics.warehouse.list.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode warehouseList(Map<String, Object> params) { return call("bg.logistics.warehouse.list.get", params); }

    /**
     * 调用 bg.logistics.companies.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode companies(Map<String, Object> params) { return call("bg.logistics.companies.get", params); }

    /**
     * 调用 bg.logistics.shippingservices.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shippingServices(Map<String, Object> params) { return call("bg.logistics.shippingservices.get", params); }

    /**
     * 调用 temu.logistics.shiplogisticstype.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode shipLogisticsType(Map<String, Object> params) { return call("temu.logistics.shiplogisticstype.get", params); }

}
