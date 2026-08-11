package cn.iocoder.yudao.module.amazon.sdk;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link AmazonSellingPartnerClient} 公共请求头测试。
 */
class AmazonSellingPartnerClientTest {

    /**
     * 验证所有 SP-API 请求均携带 Amazon 沙盒要求的 User-Agent。
     */
    @Test
    void buildHeadersIncludesUserAgent() {
        AmazonSellingPartnerClient client = new AmazonSellingPartnerClient();

        HttpHeaders headers = ReflectionTestUtils.invokeMethod(client, "buildHeaders", "access-token", false);

        assertEquals("commerce-amazon/1.0 (Language=Java)", headers.getFirst(HttpHeaders.USER_AGENT));
        assertEquals("access-token", headers.getFirst("x-amz-access-token"));
    }
}
