package cn.iocoder.yudao.module.temu.framework.client;

import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenInfoResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateRequest;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.LocalMallTagsResult;
import cn.iocoder.yudao.module.temu.framework.client.api.AuthApi;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 新版 Temu framework client 的真实授权接口验证。 */
class TemuFrameworkAuthApiTest {

    private static final String APP_KEY = "4ebbc9190ae410443d65b4c2faca981f";
    private static final String APP_SECRET = "4782d2d827276688bf4758bed55dbdd4bbe79a79";
    private static final String ACCESS_TOKEN = "uplv3hfyt5kcwoymrgnajnbl1ow5qxlz4sqhev6hl3xosz5dejrtyl2jre7";

    /** 调用 US 授权信息接口，打印响应并验证强类型结果结构。 */
    @Test
    void shouldQueryUsAccessTokenInfoAndMapResponse() throws Exception {
        // 集成测试需要访问 Temu 网络，避免普通单元测试在无网络环境中失败。

        TemuClient client = createUsClient();
        TemuApiResponse<AccessTokenInfoResult> response = new AuthApi(client).getAccessTokenInfo();

        System.out.println("Temu AuthApi response: " + new ObjectMapper().writeValueAsString(response));
        assertNotNull(response, "响应包装不能为空");
        assertNotNull(response.getSuccess(), "success 字段不能为空");
        assertTrue(response.getSuccess(), "Temu 授权信息接口应返回成功");
        assertNotNull(response.getRequestId(), "requestId 字段不能为空");
        assertNotNull(response.getResult(), "result 结果不能为空");
        assertNotNull(response.getResult().getMallId(), "result.mallId 字段不能为空");
    }

    /** 调用 US 本地店铺标签接口，打印响应并验证标签结果结构。 */
    @Test
    void shouldQueryUsLocalMallTagsAndMapResponse() throws Exception {
        assumeIntegrationEnabled();

        TemuApiResponse<LocalMallTagsResult> response = createUsClient().getAuth().getLocalMallTags();

        System.out.println("Temu local mall tags response: " + new ObjectMapper().writeValueAsString(response));
        assertNotNull(response, "响应包装不能为空");
        assertTrue(response.getSuccess(), "Temu 本地店铺标签接口应返回成功");
        assertNotNull(response.getRequestId(), "requestId 字段不能为空");
        assertNotNull(response.getResult(), "result 结果不能为空");
        assertNotNull(response.getResult().getTags(), "result.tags 字段不能为空");
    }

    /** 使用一次性授权码创建 access token，打印响应并验证创建结果结构。 */
    @Test
    void shouldCreateAccessTokenAndMapResponse() throws Exception {
        assumeIntegrationEnabled();
        String code = System.getProperty("temu.auth.code");
        Assumptions.assumeTrue(code != null && !code.isBlank(),
                "使用 -Dtemu.auth.code=<Temu 一次性授权码> 启用创建 access token 测试");
        AccessTokenCreateRequest request = new AccessTokenCreateRequest();
        request.setCode(code);

        TemuApiResponse<AccessTokenCreateResult> response = createUsClient().getAuth().createAccessToken(request);

        System.out.println("Temu create access token response: " + new ObjectMapper().writeValueAsString(response));
        assertNotNull(response, "响应包装不能为空");
        assertTrue(response.getSuccess(), "Temu 创建 access token 接口应返回成功");
        assertNotNull(response.getRequestId(), "requestId 字段不能为空");
        assertNotNull(response.getResult(), "result 结果不能为空");
        assertNotNull(response.getResult().getAccessToken(), "result.accessToken 字段不能为空");
    }

    /** 创建使用用户提供 US 配置的客户端。 */
    private TemuClient createUsClient() {
        return new TemuClient(APP_KEY, APP_SECRET, ACCESS_TOKEN, "US");
    }

    /** 验证调用方已明确启用会访问 Temu 的集成测试。 */
    private void assumeIntegrationEnabled() {
//        Assumptions.assumeTrue(Boolean.getBoolean("temu.integration"),
//                "使用 -Dtemu.integration=true 启用 Temu 真实接口测试");
    }
}
