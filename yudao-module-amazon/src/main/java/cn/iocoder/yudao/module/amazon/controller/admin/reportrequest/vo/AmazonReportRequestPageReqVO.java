package cn.iocoder.yudao.module.amazon.controller.admin.reportrequest.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Amazon 报表请求及异步处理任务分页 Request VO")
@Data
public class AmazonReportRequestPageReqVO extends PageParam {

    @Schema(description = "关联 amazon_shop.id", example = "5728")
    private Long shopId;

    @Schema(description = "请求站点国家代码，例如 US")
    private String countryCode;

    @Schema(description = "Amazon 报表类型，例如 GET_MERCHANT_LISTINGS_ALL_DATA", example = "1")
    private String reportType;

    @Schema(description = "报表数据开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] dataStartTime;

    @Schema(description = "Amazon 状态：IN_QUEUE、IN_PROGRESS、DONE、CANCELLED、FATAL", example = "1")
    private String amazonProcessingStatus;

    @Schema(description = "任务状态：0-待提交，1-等待Amazon处理，2-待下载，3-成功，4-重试等待，5-失败，6-已取消", example = "1")
    private Integer taskStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}