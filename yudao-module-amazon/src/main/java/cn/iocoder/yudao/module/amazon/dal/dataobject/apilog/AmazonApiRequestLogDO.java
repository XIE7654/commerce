package cn.iocoder.yudao.module.amazon.dal.dataobject.apilog;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Amazon SP-API 请求调用日志。
 */
@TableName("amazon_api_request_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonApiRequestLogDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 本次调用唯一请求编号。 */
    private String requestId;
    /** 链路追踪编号。 */
    private String traceId;
    /** Amazon 店铺编号。 */
    private Long shopId;
    /** 站点国家代码。 */
    private String countryCode;
    /** Marketplace ID JSON 数组。 */
    private String marketplaceIds;
    /** API 分类。 */
    private String apiCategory;
    /** API 操作名。 */
    private String operationName;
    /** HTTP 请求方式。 */
    private String requestMethod;
    /** 脱敏后的完整请求 URL。 */
    private String requestUrl;
    /** 请求路径。 */
    private String requestPath;
    /** 响应归档文件编号，对应 infra_file.id。 */
    private Long fileId;
    /** 脱敏后的请求参数 JSON。 */
    private String requestParams;
    /** 脱敏后的请求头 JSON。 */
    private String requestHeaders;
    /** 原始请求体 SHA-256。 */
    private String requestBodyHash;
    /** HTTP 状态码。 */
    private Integer httpStatusCode;
    /** 调用结果：1-成功，2-失败。 */
    private Integer resultStatus;
    /** Amazon 或应用错误码。 */
    private String errorCode;
    /** 脱敏后的错误信息。 */
    private String errorMessage;
    /** Amazon 请求编号。 */
    private String amazonRequestId;
    /** Amazon 返回的速率限制。 */
    private String rateLimit;
    /** 本次调用已重试次数。 */
    private Integer retryCount;
    /** 请求总耗时，单位毫秒。 */
    private Integer durationMs;
    /** 开始请求时间。 */
    private LocalDateTime requestedAt;
    /** 请求完成时间。 */
    private LocalDateTime completedAt;
}
