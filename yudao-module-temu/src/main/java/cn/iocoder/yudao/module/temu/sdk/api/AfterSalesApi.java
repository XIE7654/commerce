package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** AfterSales 业务接口服务。 */
public class AfterSalesApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public AfterSalesApi(TemuClient client) { super(client); }

    /**
     * 调用 bg.aftersales.parentaftersales.list.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode parentAftersalesList(Map<String, Object> params) { return call("bg.aftersales.parentaftersales.list.get", params); }

    /**
     * 调用 bg.aftersales.aftersales.list.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode aftersalesList(Map<String, Object> params) { return call("bg.aftersales.aftersales.list.get", params); }

    /**
     * 调用 temu.aftersales.parentaftersales.detail.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode parentAftersalesDetail(Map<String, Object> params) { return call("temu.aftersales.parentaftersales.detail.get", params); }

    /**
     * 调用 temu.aftersales.refund.issue。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode refundIssue(Map<String, Object> params) { return call("temu.aftersales.refund.issue", params); }

    /**
     * 调用 bg.aftersales.parentreturnorder.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode parentReturnOrder(Map<String, Object> params) { return call("bg.aftersales.parentreturnorder.get", params); }

}
