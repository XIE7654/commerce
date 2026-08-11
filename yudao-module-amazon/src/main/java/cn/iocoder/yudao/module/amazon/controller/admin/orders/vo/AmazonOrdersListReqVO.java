package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Amazon 订单列表查询请求参数。
 */
@Data
public class AmazonOrdersListReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "创建时间下限，ISO 8601 格式", example = "2026-08-01T00:00:00Z")
    private String createdAfter;
    @Schema(description = "创建时间上限，ISO 8601 格式", example = "2026-08-02T00:00:00Z")
    private String createdBefore;
    @Schema(description = "最后更新时间下限，ISO 8601 格式", example = "2026-08-01T00:00:00Z")
    private String lastUpdatedAfter;
    @Schema(description = "最后更新时间上限，ISO 8601 格式", example = "2026-08-02T00:00:00Z")
    private String lastUpdatedBefore;
    @Schema(description = "订单状态筛选", example = "Unshipped,Shipped")
    private List<String> orderStatuses;
    @Schema(description = "履行渠道筛选，AFN 或 MFN", example = "MFN")
    private List<String> fulfillmentChannels;
    @Schema(description = "支付方式筛选", example = "Other")
    private List<String> paymentMethods;
    @Schema(description = "买家邮箱筛选")
    private String buyerEmail;
    @Schema(description = "卖家订单编号筛选")
    private String sellerOrderId;
    @Schema(description = "每页最大订单数", example = "100")
    @Min(value = 1, message = "每页最大订单数不能小于 1")
    @Max(value = 100, message = "每页最大订单数不能大于 100")
    private Integer maxResultsPerPage;
    @Schema(description = "Easy Ship 发货状态筛选")
    private List<String> easyShipShipmentStatuses;
    @Schema(description = "电子发票状态筛选")
    private List<String> electronicInvoiceStatuses;
    @Schema(description = "上一页返回的分页令牌")
    private String nextToken;
    @Schema(description = "Amazon 订单编号筛选，最多 50 个")
    @Size(max = 50, message = "Amazon 订单编号最多 50 个")
    private List<String> amazonOrderIds;
    @Schema(description = "实际履行供货源编号")
    private String actualFulfillmentSupplySourceId;
    @Schema(description = "是否为到店自提订单")
    @JsonProperty("isISPU")
    private Boolean isISPU;
    @Schema(description = "连锁店门店编号")
    private String storeChainStoreId;
    @Schema(description = "最早送达时间上限，ISO 8601 格式")
    private String earliestDeliveryDateBefore;
    @Schema(description = "最早送达时间下限，ISO 8601 格式")
    private String earliestDeliveryDateAfter;
    @Schema(description = "最晚送达时间上限，ISO 8601 格式")
    private String latestDeliveryDateBefore;
    @Schema(description = "最晚送达时间下限，ISO 8601 格式")
    private String latestDeliveryDateAfter;

}
