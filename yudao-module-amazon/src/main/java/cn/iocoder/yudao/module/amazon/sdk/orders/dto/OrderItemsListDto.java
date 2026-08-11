package cn.iocoder.yudao.module.amazon.sdk.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/** Orders v0 getOrderItems 的 payload。 */
@Data
public class OrderItemsListDto {
    @JsonProperty("OrderItems") private List<OrderItemDto> orderItems;
    @JsonProperty("NextToken") private String nextToken;
}
