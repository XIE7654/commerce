package cn.iocoder.yudao.module.amazon.service.auth;

import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonTokenReqVO;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import cn.iocoder.yudao.module.amazon.sdk.AmazonOAuthClient;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link AmazonOAuthServiceImpl} Seller Token 端点选择测试。
 */
class AmazonOAuthServiceImplTest {

    @Test
    void refreshAccessTokenUsesMarketplaceEnumSellerTokenUrl() {
        AmazonOAuthServiceImpl service = new AmazonOAuthServiceImpl();
        AwsProperties properties = new AwsProperties();
        properties.getProd().setStoreTokenUrl("https://sandbox.example.com/auth/o2/token");
        RecordingOAuthClient client = new RecordingOAuthClient();
        ReflectionTestUtils.setField(service, "properties", properties);
        ReflectionTestUtils.setField(service, "amazonOAuthClient", client);

        AmazonTokenReqVO request = new AmazonTokenReqVO();
        request.setCountryCode("US");
        request.setRefreshToken("refresh-token");
        service.refreshAccessToken(request);

        assertEquals("https://api.amazon.com/auth/o2/token", client.requestedUrl);
        assertEquals("seller", client.requestedType);
    }

    @Test
    void refreshAccessTokenUsesMarketplaceEnumWhenSellerTokenUrlUnset() {
        AmazonOAuthServiceImpl service = new AmazonOAuthServiceImpl();
        RecordingOAuthClient client = new RecordingOAuthClient();
        ReflectionTestUtils.setField(service, "properties", new AwsProperties());
        ReflectionTestUtils.setField(service, "amazonOAuthClient", client);

        AmazonTokenReqVO request = new AmazonTokenReqVO();
        request.setCountryCode("US");
        request.setRefreshToken("refresh-token");
        service.refreshAccessToken(request);

        assertEquals("https://api.amazon.com/auth/o2/token", client.requestedUrl);
        assertEquals("seller", client.requestedType);
    }

    @Test
    void accessTokenExpiringWithinFiveMinutesIsNotUsable() {
        AmazonOAuthServiceImpl service = new AmazonOAuthServiceImpl();

        boolean expiringSoon = ReflectionTestUtils.invokeMethod(service, "isUsable", "access-token",
                LocalDateTime.now().plusMinutes(4).plusSeconds(59));
        boolean usable = ReflectionTestUtils.invokeMethod(service, "isUsable", "access-token",
                LocalDateTime.now().plusMinutes(5).plusSeconds(1));

        assertFalse(expiringSoon);
        assertTrue(usable);
    }

    /** 记录 OAuth 请求参数并返回固定成功响应的测试客户端。 */
    private static final class RecordingOAuthClient extends AmazonOAuthClient {

        private String requestedUrl;
        private String requestedType;

        @Override
        public Map<String, Object> requestToken(String url, String code, String refreshToken, String type) {
            requestedUrl = url;
            requestedType = type;
            return Map.of("access_token", "access-token", "expires_in", 3600);
        }
    }
}
