package cn.iocoder.yudao.module.temu.framework.client.api;

import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateRequest;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenInfoResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.LocalMallTagsResult;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;

/**
 * Temu 授权 API，所有入口均使用强类型请求和响应。
 */
public class AuthApi {
    private final TemuClient client;

    public AuthApi(TemuClient client) {
        this.client = client;
    }

    /**
     * 查询 access token 授权信息。
     */
    public TemuApiResponse<AccessTokenInfoResult> getAccessTokenInfo() {
        return response("bg.open.accesstoken.info.get", Collections.emptyMap(), AccessTokenInfoResult.class);
    }

    /**
     * 使用临时授权码创建 access token。
     */
    public TemuApiResponse<AccessTokenCreateResult> createAccessToken(AccessTokenCreateRequest request) {
        return response("bg.open.accesstoken.create", Map.of("code", request.getCode()), AccessTokenCreateResult.class);
    }

    /**
     * 查询本地店铺标签。
     */
    public TemuApiResponse<LocalMallTagsResult> getLocalMallTags() {
        return response("temu.local.mall.tags.get", Collections.emptyMap(), LocalMallTagsResult.class);
    }

    private <T> TemuApiResponse<T> response(String type, Map<String, ?> params, Class<T> resultType) {
        var raw = client.request(type, HttpMethod.POST, params);
        TemuApiResponse<T> result = new TemuApiResponse<>();
        result.setSuccess(raw.path("success").asBoolean(false));
        result.setRequestId(text(raw, "requestId"));
        result.setErrorCode(raw.path("errorCode").isMissingNode() ? null : raw.path("errorCode").asInt());
        result.setErrorMsg(text(raw, "errorMsg"));
        T converted = client.convert(raw.get("result"), resultType);
        if (converted == null) {
            throw new cn.iocoder.yudao.module.temu.framework.client.TemuClientException(
                    "Temu 响应缺少 result: " + type);
        }
        result.setResult(converted);
        return result;
    }

    private String text(tools.jackson.databind.JsonNode node, String field) {
        var value = node.get(field);
        return value == null || value.isNull() ? null : value.asString();
    }
}
