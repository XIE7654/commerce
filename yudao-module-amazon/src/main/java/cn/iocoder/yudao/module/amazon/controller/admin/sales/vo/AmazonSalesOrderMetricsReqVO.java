package cn.iocoder.yudao.module.amazon.controller.admin.sales.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Amazon Sales 订单指标查询参数。 */
@Data
public class AmazonSalesOrderMetricsReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;
    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
    @Schema(description = "统计时间区间，格式为起止 ISO 8601 时间以 -- 连接", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "统计时间区间不能为空")
    private String interval;
    @Schema(description = "统计粒度：Hour、Day、Week、Month、Year 或 Total", requiredMode = Schema.RequiredMode.REQUIRED, example = "Day")
    @NotBlank(message = "统计粒度不能为空")
    private String granularity;
    @Schema(description = "粒度时区；粒度大于 Hour 时必填", example = "America/Los_Angeles")
    private String granularityTimeZone;
    @Schema(description = "买家类型：B2B、B2C 或 All", example = "All")
    private String buyerType;
    @Schema(description = "履约网络", example = "AFN")
    private String fulfillmentNetwork;
    @Schema(description = "每周首日：Monday 或 Sunday", example = "Monday")
    private String firstDayOfWeek;
    @Schema(description = "ASIN 筛选")
    private String asin;
    @Schema(description = "SKU 筛选")
    private String sku;
    @Schema(description = "Amazon 项目筛选", example = "AmazonHaul")
    private String amazonProgram;
}
