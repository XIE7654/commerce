package cn.iocoder.yudao.module.amazon.sdk.listings.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/** Listings Items API ListingsItem 模型；属性值保留为动态 JSON 以覆盖商品类型扩展字段。 */
@Data
public class ListingsItemDto {
    private String sku;
    private String marketplaceId;
    private String productType;
    private Map<String, Object> summaries;
    private Map<String, Object> attributes;
    private List<IssueDto> issues;
    private List<String> offers;
    private List<String> fulfillmentAvailability;
}
