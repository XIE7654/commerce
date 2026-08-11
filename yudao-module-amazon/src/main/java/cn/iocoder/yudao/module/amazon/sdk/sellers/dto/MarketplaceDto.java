package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

/**
 * Amazon Marketplace 模型。
 */
@Data
public class MarketplaceDto {
    private String id;
    private String name;
    private String countryCode;
    private String defaultCurrencyCode;
    private String defaultLanguageCode;
    private String domainName;
}
