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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    }
}
