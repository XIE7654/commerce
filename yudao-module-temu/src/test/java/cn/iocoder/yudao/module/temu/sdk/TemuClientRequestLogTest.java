package cn.iocoder.yudao.module.temu.sdk;

import cn.iocoder.yudao.module.temu.sdk.product.CatsGetReqVO;
import cn.iocoder.yudao.module.temu.sdk.product.dto.CatsGetCategoryDto;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogContext;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Temu SDK 请求日志测试。 */
class TemuClientRequestLogTest {

    /** 验证分类 SDK 将 Temu 原始响应转换为结构化 DTO。 */
    @Test
    void shouldConvertCatsGetResponseToDto() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok().body("""
                        {"success":true,"requestId":"us-ad77fedd-adbd-4c88-93db-ae8f724dd9b9","errorCode":1000000,"errorMsg":"","result":[
                          {"catId":1,"availableStatus":0,"level":1,"catName":"Apparel","secondHandCategory":false,"catType":1,"leaf":false,"parentId":0,"expandCatType":4}
                        ]}
                        """));
        TemuClient client = new TemuClient("app-key", "app-secret", "access-token", "https://openapi.temu.test",
                restTemplate, new ObjectMapper(), 1000, 1000);
        CatsGetReqVO request = new CatsGetReqVO();
        request.setParentCatId(0L);

        TemuApiResponse<List<CatsGetCategoryDto>> response = client.getProduct().catsGet(request);

        assertTrue(response.getSuccess());
        assertEquals("us-ad77fedd-adbd-4c88-93db-ae8f724dd9b9", response.getRequestId());
        assertEquals(1000000, response.getErrorCode());
        assertEquals(1, response.getResult().size());
        assertEquals(1L, response.getResult().getFirst().getCatId());
        assertEquals(0, response.getResult().getFirst().getAvailableStatus());
        assertEquals(1, response.getResult().getFirst().getLevel());
        assertEquals("Apparel", response.getResult().getFirst().getCatName());
        assertEquals(false, response.getResult().getFirst().getSecondHandCategory());
        assertEquals(1, response.getResult().getFirst().getCatType());
        assertEquals(false, response.getResult().getFirst().getLeaf());
        assertEquals(0L, response.getResult().getFirst().getParentId());
        assertEquals(4, response.getResult().getFirst().getExpandCatType());
    }

    /** 验证成功调用会将请求和响应的审计信息交给日志服务。 */
    @Test
    void shouldLogSuccessfulRequest() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        TemuApiRequestLogService requestLogService = mock(TemuApiRequestLogService.class);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok().header("X-RateLimit-Limit", "100").body("""
                        {"success":true,"request_id":"temu-request-1"}
                        """));
        TemuClient client = new TemuClient("app-key", "app-secret", "access-token", "https://openapi.temu.test",
                restTemplate, new ObjectMapper(), 1000, 1000, null, "US", requestLogService, 100L);

        client.request("bg.local.goods.cats.get", HttpMethod.POST, Map.of("parentCatId", 1L));

        ArgumentCaptor<TemuApiRequestLogContext> contextCaptor = ArgumentCaptor.forClass(TemuApiRequestLogContext.class);
        verify(requestLogService).log(contextCaptor.capture(), eq(200), any(HttpHeaders.class), any(), eq(null));
        TemuApiRequestLogContext context = contextCaptor.getValue();
        assertEquals("bg.local.goods.cats.get", context.apiType());
        assertEquals("US", context.site());
        assertEquals(100L, context.shopId());
        assertEquals("POST", context.method());
        assertEquals("/openapi/router", context.uri().getPath());
        assertTrue(((Map<?, ?>) context.requestParams()).containsKey("access_token"));
    }
}
