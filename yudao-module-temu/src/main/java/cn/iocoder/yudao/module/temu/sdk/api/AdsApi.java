package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Ads 业务接口服务。 */
public class AdsApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public AdsApi(TemuClient client) { super(client); }

    /**
     * 调用 temu.searchrec.ad.roas.pred。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode roasPred(Map<String, Object> params) { return call("temu.searchrec.ad.roas.pred", params); }

    /**
     * 调用 temu.searchrec.ad.reports.mall.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode reportsMallQuery(Map<String, Object> params) { return call("temu.searchrec.ad.reports.mall.query", params); }

    /**
     * 调用 temu.searchrec.ad.reports.goods.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode reportsGoodsQuery(Map<String, Object> params) { return call("temu.searchrec.ad.reports.goods.query", params); }

    /**
     * 调用 temu.searchrec.ad.create。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode adCreate(Map<String, Object> params) { return call("temu.searchrec.ad.create", params); }

    /**
     * 调用 temu.searchrec.ad.detail.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode adDetailQuery(Map<String, Object> params) { return call("temu.searchrec.ad.detail.query", params); }

    /**
     * 调用 temu.searchrec.ad.log.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode adLogQuery(Map<String, Object> params) { return call("temu.searchrec.ad.log.query", params); }

    /**
     * 调用 temu.searchrec.ad.goods.create.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode adGoodsCreateQuery(Map<String, Object> params) { return call("temu.searchrec.ad.goods.create.query", params); }

    /**
     * 调用 temu.searchrec.ad.modify。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode adModify(Map<String, Object> params) { return call("temu.searchrec.ad.modify", params); }

}
