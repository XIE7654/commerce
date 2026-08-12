package cn.iocoder.yudao.module.amazon.controller.admin.listings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Amazon Listings Items 查询请求参数。
 */
@Data
public class AmazonListingsSearchReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "需要返回的数据集；默认 summaries", example = "summaries,issues")
    private List<String> includedData;
    @Schema(description = "商品标识符，最多 20 个；与 identifiersType 配套使用", example = "SKU-001,SKU-002")
    @Size(max = 20, message = "商品标识符最多 20 个")
    private List<String> identifiers;
    @Schema(description = "商品标识符类型；传 identifiers 时必填，支持 SKU、ASIN、UPC、EAN 等", example = "SKU")
    private String identifiersType;
    @Schema(description = "仅查询指定变体父 SKU 的子体", example = "PARENT-SKU")
    private String variationParentSku;
    @Schema(description = "按包层级 SKU 筛选", example = "PACKAGE-SKU")
    private String packageHierarchySku;
    @Schema(description = "创建时间起点，ISO 8601 格式", example = "2026-01-01T00:00:00Z")
    private String createdAfter;
    @Schema(description = "创建时间终点，ISO 8601 格式", example = "2026-01-31T23:59:59Z")
    private String createdBefore;
    @Schema(description = "最后更新时间起点，ISO 8601 格式", example = "2026-01-01T00:00:00Z")
    private String lastUpdatedAfter;
    @Schema(description = "最后更新时间终点，ISO 8601 格式", example = "2026-01-31T23:59:59Z")
    private String lastUpdatedBefore;
    @Schema(description = "问题严重级别筛选", example = "ERROR,WARNING")
    private List<String> withIssueSeverity;
    @Schema(description = "包含的商品状态筛选", example = "BUYABLE")
    private List<String> withStatus;
    @Schema(description = "排除的商品状态筛选", example = "DISCOVERABLE")
    private List<String> withoutStatus;
    @Schema(description = "排序字段，默认 lastUpdatedDate", example = "lastUpdatedDate")
    private String sortBy;
    @Schema(description = "排序方向，默认 DESC", example = "DESC")
    private String sortOrder;
    @Schema(description = "单页数量，默认 10，最大 20", example = "10")
    @Min(value = 1, message = "单页数量不能小于 1")
    @Max(value = 20, message = "单页数量不能超过 20")
    private Integer pageSize;
    @Schema(description = "分页 Token")
    private String pageToken;
    @Schema(description = "问题本地化语言", example = "en_US")
    private String issueLocale;
}
