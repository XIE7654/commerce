package cn.iocoder.yudao.module.temu.framework.client;

import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentCaptor;

/** 新版 Temu Product API 测试。 */
class TemuFrameworkProductApiTest {

    /** 验证 catsGet 使用新版 client 请求并转换分类数组。 */
    @Test
    void shouldConvertCatsGetResponse() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        {"success":true,"requestId":"request-1","errorCode":1000000,"errorMsg":"","result":[
                          {"catId":1,"availableStatus":0,"level":1,"catName":"Apparel","secondHandCategory":false,"catType":1,"leaf":false,"parentId":0,"expandCatType":4}
                        ]}
                        """));
        TemuClient client = new TemuClient("app-key", "app-secret", "access-token", "US",
                restTemplate, new ObjectMapper());
        CatsGetRequest request = new CatsGetRequest();
        request.setParentCatId(0L);

        TemuApiResponse<List<CatsGetCategoryResult>> response = client.getProduct().catsGet(request);

        assertTrue(response.getSuccess());
        assertEquals("request-1", response.getRequestId());
        assertEquals(1, response.getResult().size());
        assertEquals(1L, response.getResult().getFirst().getCatId());
        assertEquals("Apparel", response.getResult().getFirst().getCatName());
        assertEquals(false, response.getResult().getFirst().getLeaf());
        ArgumentCaptor<HttpEntity<?>> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(any(URI.class), eq(HttpMethod.POST), entityCaptor.capture(), eq(String.class));
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) entityCaptor.getValue().getBody();
        assertEquals("bg.local.goods.cats.get", payload.get("type"));
        assertEquals(0L, payload.get("parentCatId"));
    }

    /** 验证商品模板查询将 API type 和分类参数发送给 Temu。 */
    @Test
    void shouldRequestGoodsTemplate() {
        assertRawProductRequest("bg.local.goods.template.get", Map.of("catId", 123L),
                client -> client.getProduct().templateGet(Map.of("catId", 123L)));
    }

    /** 验证自定义规格 ID 生成将规格参数发送给 Temu。 */
    @Test
    void shouldRequestSpecId() {
        assertRawProductRequest("bg.local.goods.spec.id.get", Map.of("specName", "Color"),
                client -> client.getProduct().specIdGet(Map.of("specName", "Color")));
    }

    /** 验证尺码元素查询将分类参数发送给 Temu。 */
    @Test
    void shouldRequestSizeElements() {
        assertRawProductRequest("bg.local.goods.size.element.get", Map.of("catId", 123L),
                client -> client.getProduct().sizeElementGet(Map.of("catId", 123L)));
    }

    /**
     * 验证原始商品接口的 Router 请求内容和响应透传。
     *
     * @param apiType 预期的 Temu API type
     * @param businessParams 预期的业务参数
     * @param invocation 商品 API 调用
     */
    private void assertRawProductRequest(String apiType, Map<String, Object> businessParams,
                                         java.util.function.Function<TemuClient, tools.jackson.databind.JsonNode> invocation) {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        {"success":true,"requestId":"product-request-1","result":{"supported":true}}
                        """));
        TemuClient client = new TemuClient("app-key", "app-secret", "access-token", "US",
                restTemplate, new ObjectMapper());

        var response = invocation.apply(client);

        assertTrue(response.path("success").asBoolean());
        assertTrue(response.path("result").path("supported").asBoolean());
        ArgumentCaptor<HttpEntity<?>> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(any(URI.class), eq(HttpMethod.POST), entityCaptor.capture(), eq(String.class));
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) entityCaptor.getValue().getBody();
        assertEquals(apiType, payload.get("type"));
        businessParams.forEach((key, value) -> assertEquals(value, payload.get(key)));
    }
}
