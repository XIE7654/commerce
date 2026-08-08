package cn.iocoder.yudao.module.amazon.controller.admin.reports.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Amazon 报表任务列表查询请求参数。
 */
@Data
public class AmazonReportsListReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "报表类型，最多 10 个；与 nextToken 二选一", example = "GET_MERCHANT_LISTINGS_ALL_DATA")
    @Size(max = 10, message = "报表类型最多 10 个")
    private List<String> reportTypes;

    @Schema(description = "报表处理状态筛选", example = "DONE,IN_PROGRESS")
    private List<String> processingStatuses;

    @Schema(description = "每页任务数，默认 10，最大 100", example = "10")
    @Min(value = 1, message = "每页任务数不能小于 1")
    @Max(value = 100, message = "每页任务数不能超过 100")
    private Integer pageSize;

    @Schema(description = "任务创建时间起点，ISO 8601 格式", example = "2026-08-01T00:00:00Z")
    private String createdSince;

    @Schema(description = "任务创建时间终点，ISO 8601 格式", example = "2026-08-08T23:59:59Z")
    private String createdUntil;

    @Schema(description = "分页令牌；传入时不能与其他筛选条件共同使用")
    private String nextToken;
}
