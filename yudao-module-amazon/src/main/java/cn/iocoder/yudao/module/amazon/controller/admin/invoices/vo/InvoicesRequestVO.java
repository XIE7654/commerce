package cn.iocoder.yudao.module.amazon.controller.admin.invoices.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** Amazon Invoices API 请求参数。 */
@Data
public class InvoicesRequestVO {

    @NotNull(message = "店铺编号不能为空")
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @NotBlank(message = "国家代码不能为空")
    @Schema(description = "请求服务端点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "BR")
    private String countryCode;

    @Schema(description = "Marketplace ID", example = "A2Q3Y263D00KWC")
    private String marketplaceId;

    @Schema(description = "发票导出文档编号")
    private String invoicesDocumentId;

    @Schema(description = "导出任务编号")
    private String exportId;

    @Schema(description = "货件编号")
    private String shipmentId;

    @Schema(description = "发票编号")
    private String invoiceId;

    @Schema(description = "导出或发票创建开始日期，ISO 8601 date-time")
    private String dateStart;

    @Schema(description = "导出或发票创建结束日期，ISO 8601 date-time")
    private String dateEnd;

    @Schema(description = "分页令牌")
    private String nextToken;

    @Schema(description = "每页结果数")
    private Integer pageSize;

    @Schema(description = "发票状态列表")
    private List<String> statuses;

    @Schema(description = "发票类型")
    private String invoiceType;

    @Schema(description = "交易类型")
    private String transactionType;

    @Schema(description = "交易标识名称")
    private String transactionIdentifierName;

    @Schema(description = "交易标识值")
    private String transactionIdentifierId;

    @Schema(description = "外部发票编号")
    private String externalInvoiceId;

    @Schema(description = "发票系列号")
    private String series;

    @Schema(description = "排序字段")
    private String sortBy;

    @Schema(description = "排序方向")
    private String sortOrder;

    @Schema(description = "入库计划编号")
    private String inboundPlanId;

    @Schema(description = "政府发票文件格式")
    private String fileFormat;

    @Schema(description = "创建发票导出的原始请求体，必须包含 marketplaceId")
    private Map<String, Object> exportBody;

    @Schema(description = "创建政府发票的原始请求体，必须包含 marketplaceId、shipmentId、invoiceType、transactionType")
    private Map<String, Object> governmentInvoiceBody;
}
