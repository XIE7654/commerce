package cn.iocoder.yudao.module.amazon.sdk.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/** Orders v0 getOrders 的 payload，字段名与 Amazon 模型保持映射。 */
@Data
public class OrdersListDto {
    @JsonProperty("Orders") private List<OrderDto> orders;
    @JsonProperty("NextToken") private String nextToken;
    @JsonProperty("LastUpdatedBefore") private String lastUpdatedBefore;
    @JsonProperty("CreatedBefore") private String createdBefore;
}
