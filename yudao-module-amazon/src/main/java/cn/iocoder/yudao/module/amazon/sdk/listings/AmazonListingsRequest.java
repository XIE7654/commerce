package cn.iocoder.yudao.module.amazon.sdk.listings;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Listings SDK 请求参数。
 *
 * <p>店铺授权上下文由 Service 注入，业务请求参数由 Controller 请求对象映射而来。</p>
 */
@Data
public class AmazonListingsRequest {

    private Long shopId;
    private String endpoint;
    private String accessToken;
    private String countryCode;
    private String marketplaceId;
    private String sellerId;
    private String sku;
    private List<String> includedData;
    private String issueLocale;
    private List<String> identifiers;
    private String identifiersType;
    private String variationParentSku;
    private String packageHierarchySku;
    private String createdAfter;
    private String createdBefore;
    private String lastUpdatedAfter;
    private String lastUpdatedBefore;
    private List<String> withIssueSeverity;
    private List<String> withStatus;
    private List<String> withoutStatus;
    private String sortBy;
    private String sortOrder;
    private Integer pageSize;
    private String pageToken;
    private String productType;
    private Map<String, Object> attributes;
    private List<Map<String, Object>> patches;
    private String requirements;
    private String mode;
    private String asin;
    private String conditionType;
    private String reasonLocale;
}
