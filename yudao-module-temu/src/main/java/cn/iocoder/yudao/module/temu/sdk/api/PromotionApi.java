package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Promotion 业务接口服务。 */
public class PromotionApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public PromotionApi(TemuClient client) { super(client); }

    /**
     * 调用 bg.promotion.activity.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode activityQuery(Map<String, Object> params) { return call("bg.promotion.activity.query", params); }

    /**
     * 调用 bg.promotion.activity.candidate.goods.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode activityCandidateGoodsQuery(Map<String, Object> params) { return call("bg.promotion.activity.candidate.goods.query", params); }

    /**
     * 调用 bg.promotion.activity.goods.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode activityGoodsQuery(Map<String, Object> params) { return call("bg.promotion.activity.goods.query", params); }

    /**
     * 调用 bg.promotion.activity.goods.enroll。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode activityGoodsEnroll(Map<String, Object> params) { return call("bg.promotion.activity.goods.enroll", params); }

    /**
     * 调用 bg.promotion.activity.goods.operation.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode activityGoodsOperationQuery(Map<String, Object> params) { return call("bg.promotion.activity.goods.operation.query", params); }

    /**
     * 调用 bg.promotion.activity.goods.update。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode activityGoodsUpdate(Map<String, Object> params) { return call("bg.promotion.activity.goods.update", params); }

}
