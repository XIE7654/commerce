package cn.iocoder.yudao.module.temu.sdk.api;

import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import tools.jackson.databind.JsonNode;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;

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
}
