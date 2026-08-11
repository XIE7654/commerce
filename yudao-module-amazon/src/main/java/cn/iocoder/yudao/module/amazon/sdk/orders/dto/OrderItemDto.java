package cn.iocoder.yudao.module.amazon.sdk.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Orders v0 OrderItem 模型的常用字段。 */
@Data
public class OrderItemDto {
    @JsonProperty("ASIN") private String asin;
    @JsonProperty("SellerSKU") private String sellerSku;
    @JsonProperty("OrderItemId") private String orderItemId;
    @JsonProperty("Title") private String title;
    @JsonProperty("QuantityOrdered") private Integer quantityOrdered;
    @JsonProperty("QuantityShipped") private Integer quantityShipped;
    @JsonProperty("ItemPrice") private MoneyDto itemPrice;
    @JsonProperty("ItemTax") private MoneyDto itemTax;
    @JsonProperty("PromotionDiscount") private MoneyDto promotionDiscount;
}
