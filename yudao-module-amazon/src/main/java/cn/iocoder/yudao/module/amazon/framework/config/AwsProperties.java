package cn.iocoder.yudao.module.amazon.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Amazon Seller 与 Ads OAuth 配置。
 *
 * <p>密钥只从服务端配置读取，请求参数不得覆盖应用级凭据。</p>
 */
@Data
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

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
    /** state 在 Redis 中的有效期，单位秒。 */
    private long stateExpires = 600;
    /** state 加密密钥；实际使用时统一派生为 AES-256 密钥。 */
    private String cryptoKey;
    /** refresh token 业务有效期，单位天。 */
    private long refreshTokenExpires = 3100;
    /** Seller token endpoint。 */
    private String storeTokenUrl;
    /** Ads token endpoint。 */
    private String adTokenUrl;
}
