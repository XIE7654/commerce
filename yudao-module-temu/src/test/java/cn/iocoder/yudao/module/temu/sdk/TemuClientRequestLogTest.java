package cn.iocoder.yudao.module.temu.sdk;

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
