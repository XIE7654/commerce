package cn.iocoder.yudao.module.amazon.sdk.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/** Orders v0 文档中的 Order 模型；敏感字段按 Amazon 授权范围可能为空。 */
@Data
public class OrderDto {
    @JsonProperty("AmazonOrderId") private String amazonOrderId;
    @JsonProperty("SellerOrderId") private String sellerOrderId;
    @JsonProperty("PurchaseDate") private String purchaseDate;
    @JsonProperty("LastUpdateDate") private String lastUpdateDate;
    @JsonProperty("OrderStatus") private String orderStatus;
    @JsonProperty("FulfillmentChannel") private String fulfillmentChannel;
    @JsonProperty("NumberOfItemsShipped") private Integer numberOfItemsShipped;
    @JsonProperty("NumberOfItemsUnshipped") private Integer numberOfItemsUnshipped;
    @JsonProperty("PaymentMethod") private String paymentMethod;
    @JsonProperty("PaymentMethodDetails") private List<String> paymentMethodDetails;
    @JsonProperty("MarketplaceId") private String marketplaceId;
    @JsonProperty("ShipmentServiceLevelCategory") private String shipmentServiceLevelCategory;
    @JsonProperty("OrderType") private String orderType;
    @JsonProperty("EarliestShipDate") private String earliestShipDate;
    @JsonProperty("LatestShipDate") private String latestShipDate;
    @JsonProperty("IsBusinessOrder") private Boolean businessOrder;
    @JsonProperty("IsPrime") private Boolean prime;
    @JsonProperty("IsPremiumOrder") private Boolean premiumOrder;
}
