package cn.iocoder.yudao.module.amazon.sdk.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Orders v0 买家信息模型；Amazon 可能根据授权对字段脱敏。 */
@Data
public class OrderBuyerInfoDto {
    @JsonProperty("BuyerEmail") private String buyerEmail;
    @JsonProperty("BuyerName") private String buyerName;
    @JsonProperty("BuyerCounty") private String buyerCounty;
    @JsonProperty("PurchaseOrderNumber") private String purchaseOrderNumber;
}
