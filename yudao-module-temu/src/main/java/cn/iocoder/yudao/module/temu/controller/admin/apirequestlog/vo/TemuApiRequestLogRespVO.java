package cn.iocoder.yudao.module.temu.controller.admin.apirequestlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Temu OpenAPI 请求调用日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemuApiRequestLogRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25955")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "本次调用唯一请求编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "24205")
    @ExcelProperty("本次调用唯一请求编号")
    private String requestId;

    @Schema(description = "链路追踪编号", example = "26834")
    @ExcelProperty("链路追踪编号")
    private String traceId;

    @Schema(description = "Temu 店铺编号", example = "29212")
    @ExcelProperty("Temu 店铺编号")
    private Long shopId;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP")
    @ExcelProperty("Temu 站点代码，例如 US、DE、JP")
    private String site;

    @Schema(description = "API 分类，例如 product、order", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("API 分类，例如 product、order")
    private String apiCategory;

    @Schema(description = "Temu OpenAPI 接口 type", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("Temu OpenAPI 接口 type")
    private String operationName;

    @Schema(description = "HTTP 请求方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("HTTP 请求方式")
    private String requestMethod;

    @Schema(description = "脱敏后的完整请求 URL", example = "https://www.iocoder.cn")
    @ExcelProperty("脱敏后的完整请求 URL")
    private String requestUrl;

    @Schema(description = "请求路径")
    @ExcelProperty("请求路径")
    private String requestPath;

    @Schema(description = "响应归档文件编号，对应 infra_file.id", example = "17067")
    @ExcelProperty("响应归档文件编号，对应 infra_file.id")
    private Long fileId;

    @Schema(description = "脱敏后的查询参数或请求体")
    @ExcelProperty("脱敏后的查询参数或请求体")
    private String requestParams;

    @Schema(description = "脱敏后的请求头")
    @ExcelProperty("脱敏后的请求头")
    private String requestHeaders;

    @Schema(description = "原始请求体 SHA-256")
    @ExcelProperty("原始请求体 SHA-256")
    private String requestBodyHash;

    @Schema(description = "HTTP 状态码；网络异常时为空")
    @ExcelProperty("HTTP 状态码；网络异常时为空")
    private Integer httpStatusCode;

    @Schema(description = "调用结果：0-处理中，1-成功，2-失败", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("调用结果：0-处理中，1-成功，2-失败")
    private Integer resultStatus;

    @Schema(description = "Temu 或应用错误码")
    @ExcelProperty("Temu 或应用错误码")
    private String errorCode;

    @Schema(description = "脱敏后的错误信息")
    @ExcelProperty("脱敏后的错误信息")
    private String errorMessage;

    @Schema(description = "Temu 返回的请求编号", example = "25690")
    @ExcelProperty("Temu 返回的请求编号")
    private String temuRequestId;

    @Schema(description = "Temu 返回的速率限制")
    @ExcelProperty("Temu 返回的速率限制")
    private String rateLimit;

    @Schema(description = "本次调用已重试次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "17537")
    @ExcelProperty("本次调用已重试次数")
    private Integer retryCount;

    @Schema(description = "请求总耗时，单位毫秒")
    @ExcelProperty("请求总耗时，单位毫秒")
    private Integer durationMs;

    @Schema(description = "开始请求时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始请求时间")
    private LocalDateTime requestedAt;

    @Schema(description = "请求完成时间")
    @ExcelProperty("请求完成时间")
    private LocalDateTime completedAt;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}