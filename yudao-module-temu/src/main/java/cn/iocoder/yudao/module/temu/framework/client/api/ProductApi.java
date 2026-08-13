package cn.iocoder.yudao.module.temu.framework.client.api;

import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import cn.iocoder.yudao.module.temu.framework.client.TemuClientException;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetRequest;
import org.springframework.http.HttpMethod;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Temu 商品 API。 */
public class ProductApi {

    private static final String CATS_GET_API_TYPE = "bg.local.goods.cats.get";
    private static final String TEMPLATE_GET_API_TYPE = "bg.local.goods.template.get";
    private static final String SPEC_ID_GET_API_TYPE = "bg.local.goods.spec.id.get";
    private static final String SIZE_ELEMENT_GET_API_TYPE = "bg.local.goods.size.element.get";

    private final TemuClient client;

    /**
     * 创建商品 API。
     *
     * @param client Temu 传输客户端
     */
    public ProductApi(TemuClient client) {
        this.client = client;
    }

    /**
     * 查询指定父分类下的商品分类。
     *
     * @param request 分类查询参数，父分类为空时查询一级分类
     * @return Temu 分类列表及其响应元数据
     */
    public TemuApiResponse<List<CatsGetCategoryResult>> catsGet(CatsGetRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("分类查询参数不能为空");
        }
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("language", request.getLanguage());
        params.put("parentCatId", request.getParentCatId());
        JsonNode raw = client.request(CATS_GET_API_TYPE, HttpMethod.POST, params);
        return toResponse(raw);
    }

    /**
     * 查询指定 Temu 分类对应的商品属性和变体模板。
     *
     * @param params Temu 模板查询参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu 原始模板响应
     */
    public JsonNode templateGet(Map<String, Object> params) {
        return request(TEMPLATE_GET_API_TYPE, params);
    }

    /**
     * 生成自定义变体规格 ID。
     *
     * @param params Temu 规格 ID 生成参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu 原始规格 ID 响应
     */
    public JsonNode specIdGet(Map<String, Object> params) {
        return request(SPEC_ID_GET_API_TYPE, params);
    }

    /**
     * 查询分类是否需要填写尺码表及其填写要求。
     *
     * @param params Temu 尺码元素查询参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu 原始尺码元素响应
     */
    public JsonNode sizeElementGet(Map<String, Object> params) {
        return request(SIZE_ELEMENT_GET_API_TYPE, params);
    }

    /**
     * 使用 Temu Router 调用商品接口。
     *
     * @param apiType Temu API type
     * @param params 业务参数；为 {@code null} 时按空参数请求
     * @return Temu Router 原始响应
     */
    private JsonNode request(String apiType, Map<String, Object> params) {
        return client.request(apiType, HttpMethod.POST, params == null ? Map.of() : params);
    }

    /**
     * 将 Temu 原始响应转换为分类结果，数组结果需逐项映射以保留泛型信息。
     *
     * @param raw Temu Router 原始响应
     * @return 强类型分类响应
     */
    private TemuApiResponse<List<CatsGetCategoryResult>> toResponse(JsonNode raw) {
        JsonNode items = raw.get("result");
        if (items == null || !items.isArray()) {
            throw new TemuClientException("Temu 响应缺少分类结果: " + CATS_GET_API_TYPE);
        }
        List<CatsGetCategoryResult> categories = new ArrayList<>(items.size());
        items.forEach(item -> categories.add(client.convert(item, CatsGetCategoryResult.class)));

        TemuApiResponse<List<CatsGetCategoryResult>> response = new TemuApiResponse<>();
        response.setSuccess(raw.path("success").asBoolean(false));
        response.setRequestId(text(raw, "requestId"));
        response.setErrorCode(raw.path("errorCode").isMissingNode() ? null : raw.path("errorCode").asInt());
        response.setErrorMsg(text(raw, "errorMsg"));
        response.setResult(categories);
        return response;
    }

    /**
     * 读取可为空的文本字段。
     *
     * @param node 响应节点
     * @param field 字段名
     * @return 字段文本；字段缺失时返回 {@code null}
     */
    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asString();
    }
}
