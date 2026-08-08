package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Amazon Orders 2026-01-01 订单列表查询请求参数。
 */
@Data
public class AmazonOrders2026ListReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;
    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
    @Schema(description = "创建时间下限，ISO 8601 格式")
    private String createdAfter;
    @Schema(description = "创建时间上限，ISO 8601 格式")
    private String createdBefore;
    @Schema(description = "最后更新时间下限，ISO 8601 格式")
    private String lastUpdatedAfter;
    @Schema(description = "最后更新时间上限，ISO 8601 格式")
    private String lastUpdatedBefore;
    @Schema(description = "履行状态筛选")
    private List<String> fulfillmentStatuses;
    @Schema(description = "履行方筛选")
    private List<String> fulfilledBy;
    @Schema(description = "每页最大订单数")
    private Integer maxResultsPerPage;
    @Schema(description = "上一页返回的分页令牌")
    private String paginationToken;
    @Schema(description = "需要返回的数据集")
    private List<String> includedData;

}
