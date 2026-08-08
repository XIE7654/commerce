package cn.iocoder.yudao.module.amazon.controller.admin.finances.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** Amazon Finances API 通用请求参数。 */
@Data
public class AmazonFinancesReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;
    @Schema(description = "请求服务端点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
    @Schema(description = "单个 Marketplace ID；交易、付款方式和发票接口使用")
    private String marketplaceId;
    @Schema(description = "多个 Marketplace ID；按逗号编码为 API 查询参数")
    private List<String> marketplaceIds;
    @Schema(description = "分页令牌")
    private String nextToken;
    @Schema(description = "开始过账时间，ISO 8601 date-time")
    private String postedAfter;
    @Schema(description = "结束过账时间，ISO 8601 date-time")
    private String postedBefore;
    @Schema(description = "交易状态", example = "RELEASED")
    private String transactionStatus;
    @Schema(description = "关联标识名称")
    private String relatedIdentifierName;
    @Schema(description = "关联标识值")
    private String relatedIdentifierValue;
    @Schema(description = "余额类型", example = "TOTAL")
    private String balanceType;
    @Schema(description = "账户类型", example = "Standard Orders")
    private String accountType;
    @Schema(description = "余额快照日期，ISO 8601 date")
    private String asOfDate;
    @Schema(description = "汇总开始日期，ISO 8601 date")
    private String periodStart;
    @Schema(description = "汇总结束日期，ISO 8601 date")
    private String periodEnd;
    @Schema(description = "每页最大结果数，旧版财务事件接口使用")
    private Integer maxResultsPerPage;
    @Schema(description = "财务事件组开始时间下界，ISO 8601 date-time")
    private String financialEventGroupStartedAfter;
    @Schema(description = "财务事件组开始时间上界，ISO 8601 date-time")
    private String financialEventGroupStartedBefore;
    @Schema(description = "财务事件组编号")
    private String eventGroupId;
    @Schema(description = "订单编号")
    private String orderId;
    @Schema(description = "付款创建时间下界，ISO 8601 date-time")
    private String createdAfter;
    @Schema(description = "付款创建时间上界，ISO 8601 date-time")
    private String createdBefore;
    @Schema(description = "付款编号")
    private String payoutId;
    @Schema(description = "付款方式类型列表")
    private List<String> paymentMethodTypes;
    @Schema(description = "发票签发日期下界，ISO 8601 date")
    private String fromIssueDate;
    @Schema(description = "发票签发日期上界，ISO 8601 date")
    private String toIssueDate;
    @Schema(description = "发票最后修改时间下界，ISO 8601 date-time")
    private String invoicesModifiedAfter;
    @Schema(description = "发票编号")
    private String invoiceIdentifier;
    @Schema(description = "发票行项目分页令牌")
    private String nextTokenForLineItems;
    @Schema(description = "发起付款请求体，必须包含 marketplaceId 与 accountType")
    private Map<String, Object> payoutBody;

}
