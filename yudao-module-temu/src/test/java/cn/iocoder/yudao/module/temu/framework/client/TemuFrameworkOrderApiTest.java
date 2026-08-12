package cn.iocoder.yudao.module.temu.framework.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
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
import static org.mockito.Mockito.when;

/** 新版 Temu Order API 测试。 */
class TemuFrameworkOrderApiTest {

    /** 验证订单列表请求通过新版 client 发送并保留原始 JSON 响应。 */
    @Test
    void shouldRequestOrderList() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        {"success":true,"requestId":"order-request-1","result":{"pageItems":[]}}
                        """));
        TemuClient client = new TemuClient("app-key", "app-secret", "access-token", "US",
                restTemplate, new ObjectMapper());

        var response = client.getOrder().listOrdersV2(Map.of("regionId", 1, "pageNumber", 1, "pageSize", 100));

        assertTrue(response.path("success").asBoolean());
        assertEquals("order-request-1", response.path("requestId").asText());
        assertTrue(response.path("result").path("pageItems").isArray());
    }
}
