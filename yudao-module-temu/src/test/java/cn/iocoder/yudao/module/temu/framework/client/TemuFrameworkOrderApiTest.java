package cn.iocoder.yudao.module.temu.framework.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** 新版 Temu Order API 测试。 */
class TemuFrameworkOrderApiTest {

    /** 验证订单列表请求 VO 会映射为 Temu 参数，并转换为强类型响应 DTO。 */
    @Test
    void shouldRequestOrderList() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        {"success":true,"requestId":"order-request-1","result":{"pageItems":[]}}
                        """));
        TemuClient client = new TemuClient("app-key", "app-secret", "access-token", "US",
                restTemplate, new ObjectMapper());

        var request = new cn.iocoder.yudao.module.temu.framework.client.order.OrderListReqVO();
        request.setParentOrderStatus(4);
        request.setRegionId(1L);
        request.setPageNumber(1);
        request.setPageSize(100);
        var response = client.getOrder().listOrdersV2(request);

        assertTrue(response.getSuccess());
        assertEquals("order-request-1", response.getRequestId());
        assertTrue(response.getResult().getPageItems().isEmpty());
    }
}
