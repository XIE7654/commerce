package cn.iocoder.yudao.module.amazon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Amazon Marketplace、SP-API Endpoint 和 AWS Region 配置。
 *
 * <p>国家代码可直接使用，也支持将配置模板中的 {@code {{US}}} 形式解析为对应市场。
 * 枚举保存生产 SP-API 端点，并通过 {@link #getSandboxEndpoint()} 提供对应沙盒端点。</p>
 */
@Getter
@AllArgsConstructor
public enum AmazonMarketplaceEnum {

    CA("Canada", "CA", "A2EUQ1WTGCTBG2", "NA", "https://sellingpartnerapi-na.amazon.com", "us-east-1"),
    US("United States of America", "US", "ATVPDKIKX0DER", "NA", "https://sellingpartnerapi-na.amazon.com", "us-east-1"),
    MX("Mexico", "MX", "A1AM78C64UM0Y8", "NA", "https://sellingpartnerapi-na.amazon.com", "us-east-1"),
    BR("Brazil", "BR", "A2Q3Y263D00KWC", "NA", "https://sellingpartnerapi-na.amazon.com", "us-east-1"),

    IE("Ireland", "IE", "A28R8C7NBKEWEA", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    ES("Spain", "ES", "A1RKKUPIHCS9HS", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    UK("United Kingdom", "UK", "A1F83G8C2ARO7P", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    FR("France", "FR", "A13V1IB3VIYZZH", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    BE("Belgium", "BE", "AMEN7PMS3EDWL", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    NL("Netherlands", "NL", "A1805IZSGTT6HS", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    DE("Germany", "DE", "A1PA6795UKMFR9", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    IT("Italy", "IT", "APJ6JRA9NG5V4", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    SE("Sweden", "SE", "A2NODRKZP88ZB9", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    ZA("South Africa", "ZA", "AE08WJ6YKNBMC", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    PL("Poland", "PL", "A1C3SOZRARQ6R3", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    EG("Egypt", "EG", "ARBP9OOSHTCHU", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    TR("Turkey", "TR", "A33AVAJ2PDY3EV", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    SA("Saudi Arabia", "SA", "A17E79C6D8DWNP", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    AE("United Arab Emirates", "AE", "A2VIGQ35RCS4UG", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),
    IN("India", "IN", "A21TJRUUN4KGV", "EU", "https://sellingpartnerapi-eu.amazon.com", "eu-west-1"),

    SG("Singapore", "SG", "A19VAU5U5O7RUS", "FE", "https://sellingpartnerapi-fe.amazon.com", "us-west-2"),
    AU("Australia", "AU", "A39IBJ37TRP1C6", "FE", "https://sellingpartnerapi-fe.amazon.com", "us-west-2"),
    JP("Japan", "JP", "A1VC38T7YXB528", "FE", "https://sellingpartnerapi-fe.amazon.com", "us-west-2");

    /** Login with Amazon 的 Seller OAuth Token 全局端点。 */
    private static final String SELLER_OAUTH_TOKEN_ENDPOINT = "https://api.amazon.com/auth/o2/token";

    /** 国家英文名称。 */
    private final String countryName;
    /** 国家代码，也是常用的模板输入值。 */
    private final String countryCode;
    /** Amazon Marketplace ID。 */
    private final String marketplaceId;
    /** Amazon 销售区域：NA、EU 或 FE。 */
    private final String salesRegion;
    /** SP-API 请求端点。 */
    private final String endpoint;
    /** SP-API 对应的 AWS Region。 */
    private final String awsRegion;

    /**
     * 获取当前区域对应的 SP-API 沙盒端点。
     *
     * @return 沙盒环境 SP-API 端点
     */
    public String getSandboxEndpoint() {
        return switch (salesRegion) {
            case "NA" -> "https://sandbox.sellingpartnerapi-na.amazon.com";
            case "EU" -> "https://sandbox.sellingpartnerapi-eu.amazon.com";
            case "FE" -> "https://sandbox.sellingpartnerapi-fe.amazon.com";
            default -> throw new IllegalArgumentException("不支持的 Amazon 销售区域: " + salesRegion);
        };
    }

    /**
     * 获取 Seller OAuth Token 端点。
     *
     * <p>Seller Token 由 Login with Amazon 统一签发，不能使用 SP-API 区域端点或环境配置的代理地址，
     * 否则会导致店铺业务请求在 Token 刷新阶段失败。</p>
     *
     * @return Login with Amazon Seller OAuth Token 端点
     */
    public static String getSellerOAuthTokenEndpoint() {
        return SELLER_OAUTH_TOKEN_ENDPOINT;
    }

    /**
     * 根据国家代码查找 Marketplace 配置。
     *
     * @param value 国家代码，或类似 {@code {{US}}} 的模板值
     * @return 对应的 Marketplace 配置；输入为空或不存在时返回 {@code null}
     */
    public static AmazonMarketplaceEnum fromCountryCode(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String countryCode = value.trim();
        if (countryCode.startsWith("{{") && countryCode.endsWith("}}")) {
            countryCode = countryCode.substring(2, countryCode.length() - 2).trim();
        }
        for (AmazonMarketplaceEnum marketplace : values()) {
            if (marketplace.countryCode.equalsIgnoreCase(countryCode)) {
                return marketplace;
            }
        }
        return null;
    }

    /**
     * 根据销售区域获取用于访问该区域 SP-API 的 Marketplace 配置。
     *
     * <p>仅需要区域端点的 API 不依赖具体站点，返回该区域中的任一 Marketplace 配置即可。</p>
     *
     * @param value Amazon 销售区域，例如 NA、EU 或 FE
     * @return 区域对应的 Marketplace 配置；输入为空或不存在时返回 {@code null}
     */
    public static AmazonMarketplaceEnum fromSalesRegion(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        for (AmazonMarketplaceEnum marketplace : values()) {
            if (marketplace.salesRegion.equalsIgnoreCase(value.trim())) {
                return marketplace;
            }
        }
        return null;
    }

    /**
     * 根据国家代码获取 Marketplace ID。
     *
     * @param value 国家代码，或类似 {@code {{US}}} 的模板值
     * @return Marketplace ID；输入不存在时返回 {@code null}
     */
    public static String getMarketplaceId(String value) {
        AmazonMarketplaceEnum marketplace = fromCountryCode(value);
        return marketplace == null ? null : marketplace.marketplaceId;
    }

    /**
     * 根据 Amazon Marketplace ID 查找站点配置。
     *
     * @param marketplaceId Amazon Marketplace ID
     * @return 对应的站点配置；输入为空或不存在时返回 {@code null}
     */
    public static AmazonMarketplaceEnum fromMarketplaceId(String marketplaceId) {
        if (marketplaceId == null || marketplaceId.trim().isEmpty()) {
            return null;
        }
        for (AmazonMarketplaceEnum marketplace : values()) {
            if (marketplace.marketplaceId.equalsIgnoreCase(marketplaceId.trim())) {
                return marketplace;
            }
        }
        return null;
    }
}
