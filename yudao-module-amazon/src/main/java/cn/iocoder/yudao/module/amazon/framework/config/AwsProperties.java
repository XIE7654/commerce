package cn.iocoder.yudao.module.amazon.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Amazon Seller 与 Ads OAuth 配置。
 *
 * <p>密钥只从服务端配置读取，请求参数不得覆盖应用级凭据。正式环境配置在
 * {@code aws.prod}，沙盒环境配置在 {@code aws.sandbox}；{@code sandboxMode} 为
 * {@code true} 时读取沙盒配置，否则读取正式环境配置。</p>
 */
@Data
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    /** 是否启用沙盒模式；true 时读取 sandbox，false 时读取 prod。 */
    private boolean sandboxMode = false;
    /** 正式环境 Seller 与 Ads OAuth 配置。 */
    @NestedConfigurationProperty
    private AwsEnvironmentProperties prod = new AwsEnvironmentProperties();
    /** 沙盒环境 Seller 与 Ads OAuth 配置。 */
    @NestedConfigurationProperty
    private AwsEnvironmentProperties sandbox = new AwsEnvironmentProperties();

    // 以下 getter 统一返回当前生效环境配置，避免各调用方自行判断沙盒开关。
    public String getAppId() {
        return active().getAppId();
    }

    public String getClientId() {
        return active().getClientId();
    }

    public String getClientSecret() {
        return active().getClientSecret();
    }

    public String getAdClientId() {
        return active().getAdClientId();
    }

    public String getAdClientSecret() {
        return active().getAdClientSecret();
    }

    public String getSellerAuthLoginUri() {
        return active().getSellerAuthLoginUri();
    }

    public String getAdAuthLoginUri() {
        return active().getAdAuthLoginUri();
    }

    public long getStateExpires() {
        Long stateExpires = active().getStateExpires();
        return stateExpires == null ? 600 : stateExpires;
    }

    public String getCryptoKey() {
        return active().getCryptoKey();
    }

    public long getRefreshTokenExpires() {
        Long refreshTokenExpires = active().getRefreshTokenExpires();
        return refreshTokenExpires == null ? 3100 : refreshTokenExpires;
    }

    public String getStoreTokenUrl() {
        return active().getStoreTokenUrl();
    }

    public String getAdTokenUrl() {
        return active().getAdTokenUrl();
    }

    /**
     * 获取当前生效的环境配置。
     *
     * @return 沙盒模式下返回 sandbox，否则返回 prod
     */
    private AwsEnvironmentProperties active() {
        return sandboxMode ? sandbox : prod;
    }

    /**
     * 单个环境下的 Amazon Seller 与 Ads OAuth 配置。
     */
    @Data
    public static class AwsEnvironmentProperties {

        /** Amazon 应用标识。 */
        private String appId;
        /** Seller OAuth 客户端 ID。 */
        private String clientId;
        /** Seller OAuth 客户端密钥。 */
        private String clientSecret;
        /** Ads OAuth 客户端 ID。 */
        private String adClientId;
        /** Ads OAuth 客户端密钥。 */
        private String adClientSecret;
        /** Seller 授权登录地址。 */
        private String sellerAuthLoginUri;
        /** Ads 授权登录地址。 */
        private String adAuthLoginUri;
        /** state 在 Redis 中的有效期，单位秒；未配置时默认 600。 */
        private Long stateExpires;
        /** state 加密密钥；实际使用时统一派生为 AES-256 密钥。 */
        private String cryptoKey;
        /** refresh token 业务有效期，单位天；未配置时默认 3100。 */
        private Long refreshTokenExpires;
        /** Seller token endpoint。 */
        private String storeTokenUrl;
        /** Ads token endpoint。 */
        private String adTokenUrl;
    }
}
