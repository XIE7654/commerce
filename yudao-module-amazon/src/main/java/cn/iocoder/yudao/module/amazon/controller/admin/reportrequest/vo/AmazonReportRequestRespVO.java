package cn.iocoder.yudao.module.amazon.controller.admin.reportrequest.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Amazon 报表请求及异步处理任务 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AmazonReportRequestRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30746")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "本地报表请求唯一编号，用于任务幂等与关联日志", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("本地报表请求唯一编号，用于任务幂等与关联日志")
    private String requestNo;

    @Schema(description = "关联 amazon_shop.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5728")
    @ExcelProperty("关联 amazon_shop.id")
    private Long shopId;

    @Schema(description = "请求站点国家代码，例如 US", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("请求站点国家代码，例如 US")
    private String countryCode;

    @Schema(description = "Amazon Marketplace ID 列表，Reports API 最多支持 25 个", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Amazon Marketplace ID 列表，Reports API 最多支持 25 个")
    private String marketplaceIds;

    @Schema(description = "Amazon 报表类型，例如 GET_MERCHANT_LISTINGS_ALL_DATA", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Amazon 报表类型，例如 GET_MERCHANT_LISTINGS_ALL_DATA")
    private String reportType;

    @Schema(description = "报表附加选项，随报表类型变化")
    @ExcelProperty("报表附加选项，随报表类型变化")
    private String reportOptions;

    @Schema(description = "报表数据开始时间")
    @ExcelProperty("报表数据开始时间")
    private LocalDateTime dataStartTime;

    @Schema(description = "报表数据结束时间")
    @ExcelProperty("报表数据结束时间")
    private LocalDateTime dataEndTime;

    @Schema(description = "Amazon reportId；在店铺维度唯一", example = "31181")
    @ExcelProperty("Amazon reportId；在店铺维度唯一")
    private String amazonReportId;

    @Schema(description = "创建该报表的 Amazon 计划编号；手工请求为空", example = "16414")
    @ExcelProperty("创建该报表的 Amazon 计划编号；手工请求为空")
    private String amazonReportScheduleId;

    @Schema(description = "Amazon 状态：IN_QUEUE、IN_PROGRESS、DONE、CANCELLED、FATAL", example = "1")
    @ExcelProperty("Amazon 状态：IN_QUEUE、IN_PROGRESS、DONE、CANCELLED、FATAL")
    private String amazonProcessingStatus;

    @Schema(description = "Amazon 创建报表时间")
    @ExcelProperty("Amazon 创建报表时间")
    private LocalDateTime amazonCreatedTime;

    @Schema(description = "Amazon 开始处理时间")
    @ExcelProperty("Amazon 开始处理时间")
    private LocalDateTime processingStartTime;

    @Schema(description = "Amazon 完成处理时间")
    @ExcelProperty("Amazon 完成处理时间")
    private LocalDateTime processingEndTime;

    @Schema(description = "Amazon reportDocumentId", example = "23149")
    @ExcelProperty("Amazon reportDocumentId")
    private String reportDocumentId;

    @Schema(description = "下载文件压缩算法，例如 GZIP")
    @ExcelProperty("下载文件压缩算法，例如 GZIP")
    private String compressionAlgorithm;

    @Schema(description = "已下载并归档的文件编号，对应 infra_file.id", example = "21250")
    @ExcelProperty("已下载并归档的文件编号，对应 infra_file.id")
    private Long fileId;

    @Schema(description = "报表文件下载完成时间")
    @ExcelProperty("报表文件下载完成时间")
    private LocalDateTime downloadTime;

    @Schema(description = "任务状态：0-待提交，1-等待Amazon处理，2-待下载，3-成功，4-重试等待，5-失败，6-已取消", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("任务状态：0-待提交，1-等待Amazon处理，2-待下载，3-成功，4-重试等待，5-失败，6-已取消")
    private Integer taskStatus;

    @Schema(description = "当前执行阶段：0-提交，1-查询状态，2-下载文件", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("当前执行阶段：0-提交，1-查询状态，2-下载文件")
    private Integer executeStage;

    @Schema(description = "当前阶段已重试次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "22504")
    @ExcelProperty("当前阶段已重试次数")
    private Integer retryCount;

    @Schema(description = "当前阶段最大重试次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "27511")
    @ExcelProperty("当前阶段最大重试次数")
    private Integer maxRetryCount;

    @Schema(description = "下次可执行时间；用于退避重试和轮询调度")
    @ExcelProperty("下次可执行时间；用于退避重试和轮询调度")
    private LocalDateTime nextRetryTime;

    @Schema(description = "最近一次调用 Amazon 时间")
    @ExcelProperty("最近一次调用 Amazon 时间")
    private LocalDateTime lastRequestTime;

    @Schema(description = "最近一次失败错误码")
    @ExcelProperty("最近一次失败错误码")
    private String lastErrorCode;

    @Schema(description = "最近一次失败原因")
    @ExcelProperty("最近一次失败原因")
    private String lastErrorMessage;

    @Schema(description = "任务成功、失败或取消的最终完成时间")
    @ExcelProperty("任务成功、失败或取消的最终完成时间")
    private LocalDateTime completedTime;

    @Schema(description = "乐观锁版本，防止多个任务执行器重复处理", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("乐观锁版本，防止多个任务执行器重复处理")
    private Integer lockVersion;

    @Schema(description = "任务执行租约到期时间；超时后允许其他执行器接管")
    @ExcelProperty("任务执行租约到期时间；超时后允许其他执行器接管")
    private LocalDateTime lockExpireTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}