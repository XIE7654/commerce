package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

/** Auth 业务接口服务。 */
public class AuthApi extends TemuApiService {

    /** 创建业务接口服务。 */
    public AuthApi(TemuClient client) { super(client); }

    /**
     * 调用 bg.open.accesstoken.info.get。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode getAccessTokenInfo(Map<String, Object> params) { return call("bg.open.accesstoken.info.get", params); }

    /** 无业务参数调用 bg.open.accesstoken.info.get。 */
    public JsonNode getAccessTokenInfo() { return getAccessTokenInfo(Collections.emptyMap()); }

    /**
     * 调用 bg.open.accesstoken.create。
     * @param params 接口业务参数，字段名使用 Temu API 的 camelCase 名称
     * @return Temu JSON 响应
     */
    public JsonNode createAccessTokenInfo(Map<String, Object> params) { return call("bg.open.accesstoken.create", params); }

    /** 无业务参数调用 bg.open.accesstoken.create。 */
    public JsonNode createAccessTokenInfo() { return createAccessTokenInfo(Collections.emptyMap()); }

}
