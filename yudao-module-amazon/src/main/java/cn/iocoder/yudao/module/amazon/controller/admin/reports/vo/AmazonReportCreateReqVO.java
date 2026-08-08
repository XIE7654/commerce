package cn.iocoder.yudao.module.amazon.controller.admin.reports.vo;

import cn.iocoder.yudao.module.amazon.enums.AmazonReportTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Amazon 创建报表任务请求参数。
 */
@Data
public class AmazonReportCreateReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "报表所属站点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "Amazon 报表类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET_MERCHANT_LISTINGS_ALL_DATA")
    @NotNull(message = "报表类型不能为空")
    private AmazonReportTypeEnum reportType;

    @Schema(description = "报表数据开始时间，ISO 8601 格式", example = "2026-08-01T00:00:00Z")
    private String dataStartTime;

    @Schema(description = "报表数据结束时间，ISO 8601 格式", example = "2026-08-08T23:59:59Z")
    private String dataEndTime;

    @Schema(description = "随报表类型变化的附加配置", example = "{\"date\":\"2026-08-01\"}")
    private Map<String, String> reportOptions;
}
