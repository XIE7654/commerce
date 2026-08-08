package cn.iocoder.yudao.module.amazon.controller.admin.reports.vo;

import cn.iocoder.yudao.module.amazon.enums.AmazonReportTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Amazon 创建报表计划请求参数。
 */
@Data
public class AmazonReportScheduleCreateReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "报表所属站点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "Amazon 报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET_MERCHANT_LISTINGS_ALL_DATA")
    @NotNull(message = "报表类型不能为空")
    private AmazonReportTypeEnum reportType;

    @Schema(description = "ISO 8601 周期，必须为 Amazon 支持的固定周期", requiredMode = Schema.RequiredMode.REQUIRED, example = "P1D")
    @NotBlank(message = "报表生成周期不能为空")
    private String period;

    @Schema(description = "下一次生成报表的时间，ISO 8601 格式", example = "2026-08-09T00:00:00Z")
    private String nextReportCreationTime;

    @Schema(description = "随报表类型变化的附加配置", example = "{\"date\":\"2026-08-01\"}")
    private Map<String, String> reportOptions;
}
