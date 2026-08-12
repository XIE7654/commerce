package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuApiResponse;
import tools.jackson.databind.JsonNode;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/** Temu 业务服务的公共基类。 */
public abstract class TemuApiService {
    protected final TemuClient client;

    protected TemuApiService(TemuClient client) { this.client = client; }

    /**
     * 调用指定 Temu 接口。
     * @param apiType 接口 type
     * @param params JSON 业务参数
     * @return Temu JSON 响应
     */
    protected JsonNode call(String apiType, Map<String, Object> params) {
        return client.request(apiType, HttpMethod.POST, params == null ? Collections.emptyMap() : params);
    }

    /**
     * 将 Temu 原始响应转换为通用响应包装，并由调用方转换具体业务结果。
     *
     * @param response Temu 原始响应
     * @param resultConverter 业务结果转换函数
     * @param <T> 业务结果类型
     * @return 通用 Temu 响应包装
     */
    protected <T> TemuApiResponse<T> toResponse(JsonNode response, Function<JsonNode, T> resultConverter) {
        TemuApiResponse<T> result = new TemuApiResponse<>();
        result.setSuccess(booleanValue(response, "success"));
        result.setRequestId(textValue(response, "requestId"));
        result.setErrorCode(integerValue(response, "errorCode"));
        result.setErrorMsg(textValue(response, "errorMsg"));
        result.setResult(resultConverter.apply(response.path("result")));
        return result;
    }

    /**
     * 读取 JSON 中的字符串字段，字段不存在或为 null 时返回 null。
     *
     * @param node JSON 节点
     * @param field 字段名称
     * @return 字符串字段值
     */
    protected String textValue(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asString();
    }

    /**
     * 读取 JSON 中的 Long 字段，字段不存在或为 null 时返回 null。
     *
     * @param node JSON 节点
     * @param field 字段名称
     * @return Long 字段值
     */
    protected Long longValue(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asLong();
    }

    /**
     * 读取 JSON 中的 Integer 字段，字段不存在或为 null 时返回 null。
     *
     * @param node JSON 节点
     * @param field 字段名称
     * @return Integer 字段值
     */
    protected Integer integerValue(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asInt();
    }

    /**
     * 读取 JSON 中的 Boolean 字段，字段不存在或为 null 时返回 null。
     *
     * @param node JSON 节点
     * @param field 字段名称
     * @return Boolean 字段值
     */
    protected Boolean booleanValue(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asBoolean();
    }
}
