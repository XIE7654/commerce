package cn.iocoder.yudao.module.amazon.service.spapi;

import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/** 官方 SP-API SDK 客户端授权配置工厂。 */
@Component
public class AmazonSpApiSdkFactory {

    @Resource
    private AwsProperties awsProperties;

    /**
     * 构造店铺级 LWA 凭据，官方 SDK 会负责获取和缓存 access token。
     *
     * @param refreshToken 店铺 Seller refresh token
     * @return 官方 SDK 使用的 LWA 凭据
     */
    public LWAAuthorizationCredentials credentials(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("店铺未配置 Amazon Seller refresh token");
        }
        return LWAAuthorizationCredentials.builder()
                .clientId(awsProperties.getClientId())
                .clientSecret(awsProperties.getClientSecret())
                .refreshToken(refreshToken)
                .endpoint(awsProperties.getStoreTokenUrl())
                .build();
    }
}
