package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Price 业务接口服务。 */
public class PriceApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public PriceApi(TemuClient client) { super(client); }

    /**
     * 调用 temu.local.goods.recommendedprice.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode recommendedPriceQuery(Map<String, Object> params) { return call("temu.local.goods.recommendedprice.query", params); }

    /**
     * 调用 temu.local.goods.appealorder.record.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode appealorderRecordQuery(Map<String, Object> params) { return call("temu.local.goods.appealorder.record.query", params); }

    /**
     * 调用 temu.local.goods.appealorder.create。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode appealorderCreate(Map<String, Object> params) { return call("temu.local.goods.appealorder.create", params); }

    /**
     * 调用 temu.local.goods.appealorder.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode appealorderQuery(Map<String, Object> params) { return call("temu.local.goods.appealorder.query", params); }

    /**
     * 调用 temu.local.goods.priceorder.reject。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode priceorderReject(Map<String, Object> params) { return call("temu.local.goods.priceorder.reject", params); }

    /**
     * 调用 bg.local.goods.sku.list.price.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode skuListPriceQuery(Map<String, Object> params) { return call("bg.local.goods.sku.list.price.query", params); }

    /**
     * 调用 bg.local.goods.priceorder.change.sku.price。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode changeSkuPrice(Map<String, Object> params) { return call("bg.local.goods.priceorder.change.sku.price", params); }

    /**
     * 调用 bg.local.goods.priceorder.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode priceorderQuery(Map<String, Object> params) { return call("bg.local.goods.priceorder.query", params); }

    /**
     * 调用 bg.local.goods.priceorder.accept。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode priceorderAccept(Map<String, Object> params) { return call("bg.local.goods.priceorder.accept", params); }

    /**
     * 调用 bg.local.goods.priceorder.negotiate。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode priceorderNegotiate(Map<String, Object> params) { return call("bg.local.goods.priceorder.negotiate", params); }

    /**
     * 调用 bg.order.amount.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode amountQuery(Map<String, Object> params) { return call("bg.order.amount.query", params); }

}
