package cn.iocoder.yudao.module.temu.dal.dataobject.apirequestlog;

import java.time.LocalDateTime;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Temu OpenAPI 请求调用日志 DO
 *
 * @author 自达源码
 */
@TableName("temu_api_request_log")
@KeySequence("temu_api_request_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuApiRequestLogDO extends TenantBaseDO {

    /**
     * 主键编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 本次调用唯一请求编号
     */
    private String requestId;
    /**
     * 链路追踪编号
     */
    private String traceId;
    /**
     * Temu 店铺编号
     */
    private Long shopId;
    /**
     * Temu 站点代码，例如 US、DE、JP
     */
    private String site;
    /**
     * API 分类，例如 product、order
     */
    private String apiCategory;
    /**
     * Temu OpenAPI 接口 type
     */
    private String operationName;
    /**
     * HTTP 请求方式
     */
    private String requestMethod;
    /**
     * 脱敏后的完整请求 URL
     */
    private String requestUrl;
    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 响应归档文件编号，对应 infra_file.id
     */
    private Long fileId;
    /**
     * 脱敏后的查询参数或请求体
     */
    private String requestParams;
    /**
     * 脱敏后的请求头
     */
    private String requestHeaders;
    /**
     * 原始请求体 SHA-256
     */
    private String requestBodyHash;
    /**
     * HTTP 状态码；网络异常时为空
     */
    private Integer httpStatusCode;
    /**
     * 调用结果：0-处理中，1-成功，2-失败
     */
    private Integer resultStatus;
    /**
     * Temu 或应用错误码
     */
    private String errorCode;
    /**
     * 脱敏后的错误信息
     */
    private String errorMessage;
    /**
     * Temu 返回的请求编号
     */
    private String temuRequestId;
    /**
     * Temu 返回的速率限制
     */
    private String rateLimit;
    /**
     * 本次调用已重试次数
     */
    private Integer retryCount;
    /**
     * 请求总耗时，单位毫秒
     */
    private Integer durationMs;
    /**
     * 开始请求时间
     */
    private LocalDateTime requestedAt;
    /**
     * 请求完成时间
     */
    private LocalDateTime completedAt;


}
