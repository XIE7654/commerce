package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Product 业务接口服务。 */
public class ProductApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public ProductApi(TemuClient client) { super(client); }

    /**
     * 调用 temu.local.goods.illegal.vocabulary.check。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode illegalVocabularyCheck(Map<String, Object> params) { return call("temu.local.goods.illegal.vocabulary.check", params); }

    /**
     * 调用 temu.local.goods.sku.net.content.unit.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode skuNetContentUnitQuery(Map<String, Object> params) { return call("temu.local.goods.sku.net.content.unit.query", params); }

    /**
     * 调用 temu.local.goods.delete。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode deleteGoods(Map<String, Object> params) { return call("temu.local.goods.delete", params); }

    /**
     * 调用 temu.local.sku.list.retrieve。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode skuListRetrieve(Map<String, Object> params) { return call("temu.local.sku.list.retrieve", params); }

    /**
     * 调用 temu.local.goods.list.retrieve。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode goodsListRetrieve(Map<String, Object> params) { return call("temu.local.goods.list.retrieve", params); }

    /**
     * 调用 bg.local.goods.compliance.info.fill.list.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode complianceInfoFillListQuery(Map<String, Object> params) { return call("bg.local.goods.compliance.info.fill.list.query", params); }

    /**
     * 调用 bg.local.goods.spec.id.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode specIdGet(Map<String, Object> params) { return call("bg.local.goods.spec.id.get", params); }

    /**
     * 调用 bg.local.goods.size.element.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode sizeElementGet(Map<String, Object> params) { return call("bg.local.goods.size.element.get", params); }

    /**
     * 调用 bg.local.goods.cats.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode catsGet(Map<String, Object> params) { return call("bg.local.goods.cats.get", params); }

    /**
     * 调用 bg.local.goods.compliance.extra.template.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode complianceExtraTemplateGet(Map<String, Object> params) { return call("bg.local.goods.compliance.extra.template.get", params); }

    /**
     * 调用 bg.local.goods.compliance.rules.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode complianceRulesGet(Map<String, Object> params) { return call("bg.local.goods.compliance.rules.get", params); }

    /**
     * 调用 bg.local.goods.template.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode templateGet(Map<String, Object> params) { return call("bg.local.goods.template.get", params); }

    /**
     * 调用 bg.local.goods.brand.trademark.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode brandTrademarkGet(Map<String, Object> params) { return call("bg.local.goods.brand.trademark.get", params); }

    /**
     * 调用 bg.local.goods.gallery.signature.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode gallerySignatureGet(Map<String, Object> params) { return call("bg.local.goods.gallery.signature.get", params); }

    /**
     * 调用 bg.local.goods.compliance.property.check。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode compliancePropertyCheck(Map<String, Object> params) { return call("bg.local.goods.compliance.property.check", params); }

    /**
     * 调用 bg.local.goods.stock.edit。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode stockEdit(Map<String, Object> params) { return call("bg.local.goods.stock.edit", params); }

    /**
     * 调用 bg.local.goods.list.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode goodsListQuery(Map<String, Object> params) { return call("bg.local.goods.list.query", params); }

    /**
     * 调用 bg.local.goods.sku.list.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode skuListQuery(Map<String, Object> params) { return call("bg.local.goods.sku.list.query", params); }

    /**
     * 调用 bg.local.goods.compliance.edit。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode complianceEdit(Map<String, Object> params) { return call("bg.local.goods.compliance.edit", params); }

    /**
     * 调用 bg.local.compliance.goods.list.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode complianceGoodsListQuery(Map<String, Object> params) { return call("bg.local.compliance.goods.list.query", params); }

    /**
     * 调用 bg.local.goods.category.recommend。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode categoryRecommend(Map<String, Object> params) { return call("bg.local.goods.category.recommend", params); }

    /**
     * 调用 bg.local.goods.property.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode propertyGet(Map<String, Object> params) { return call("bg.local.goods.property.get", params); }

    /**
     * 调用 bg.local.goods.add。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode goodsAdd(Map<String, Object> params) { return call("bg.local.goods.add", params); }

    /**
     * 调用 bg.local.goods.image.upload。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode imageUpload(Map<String, Object> params) { return call("bg.local.goods.image.upload", params); }

    /**
     * 调用 bg.local.goods.videocoverimage.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode videocoverimageGet(Map<String, Object> params) { return call("bg.local.goods.videocoverimage.get", params); }

    /**
     * 调用 bg.freight.template.list.query。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode freightTemplateListQuery(Map<String, Object> params) { return call("bg.freight.template.list.query", params); }

    /**
     * 调用 bg.local.goods.sale.status.set。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode saleStatusSet(Map<String, Object> params) { return call("bg.local.goods.sale.status.set", params); }

    /**
     * 调用 bg.local.goods.publish.status.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode publishStatusGet(Map<String, Object> params) { return call("bg.local.goods.publish.status.get", params); }

    /**
     * 调用 bg.local.goods.sku.out.sn.check。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode skuOutSnCheck(Map<String, Object> params) { return call("bg.local.goods.sku.out.sn.check", params); }

    /**
     * 调用 bg.local.goods.sku.out.sn.set。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode skuOutSnSet(Map<String, Object> params) { return call("bg.local.goods.sku.out.sn.set", params); }

    /**
     * 调用 bg.local.goods.tax.code.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode taxCodeGet(Map<String, Object> params) { return call("bg.local.goods.tax.code.get", params); }

    /**
     * 调用 bg.local.goods.out.sn.set。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode goodsOutSnSet(Map<String, Object> params) { return call("bg.local.goods.out.sn.set", params); }

    /**
     * 调用 bg.local.goods.out.sn.check。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode goodsOutSnCheck(Map<String, Object> params) { return call("bg.local.goods.out.sn.check", params); }

    /**
     * 调用 bg.local.goods.property.relations。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode propertyRelations(Map<String, Object> params) { return call("bg.local.goods.property.relations", params); }

    /**
     * 调用 bg.local.goods.property.relations.level.template。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode propertyRelationsLevelTemplate(Map<String, Object> params) { return call("bg.local.goods.property.relations.level.template", params); }

    /**
     * 调用 bg.local.goods.property.relations.template。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode propertyRelationsTemplate(Map<String, Object> params) { return call("bg.local.goods.property.relations.template", params); }

    /**
     * 调用 bg.local.goods.category.check。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode categoryCheck(Map<String, Object> params) { return call("bg.local.goods.category.check", params); }

    /**
     * 调用 temu.local.goods.spec.info.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode specInfoGet(Map<String, Object> params) { return call("temu.local.goods.spec.info.get", params); }

}
