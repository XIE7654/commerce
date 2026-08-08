package cn.iocoder.yudao.module.amazon.controller.admin.fbainventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Amazon FBA 库存摘要查询请求参数。
 */
@Data
public class FbaInventorySummariesReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "是否返回额外汇总库存明细和数量，默认 false", example = "true")
    private Boolean details;

    @Schema(description = "仅返回此时间之后发生变更的库存，ISO 8601 格式", example = "2026-08-01T00:00:00Z")
    private String startDateTime;

    @Schema(description = "需要返回库存摘要的卖家 SKU，最多 50 个", example = "SKU-001,SKU-002")
    @Size(max = 50, message = "卖家 SKU 最多 50 个")
    private List<String> sellerSkus;

    @Schema(description = "需要返回库存摘要的单个卖家 SKU", example = "SKU-001")
    private String sellerSku;

    @Schema(description = "上一页响应返回的分页令牌；仅与 startDateTime 配合使用")
    private String nextToken;
}
