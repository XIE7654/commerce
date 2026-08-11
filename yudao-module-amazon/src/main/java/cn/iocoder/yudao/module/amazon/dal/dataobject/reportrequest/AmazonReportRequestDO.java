package cn.iocoder.yudao.module.amazon.dal.dataobject.reportrequest;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Amazon 报表请求及异步处理任务 DO
 *
 * @author 自达源码
 */
@TableName("amazon_report_request")
@KeySequence("amazon_report_request_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonReportRequestDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 本地报表请求唯一编号，用于任务幂等与关联日志
     */
    private String requestNo;
    /**
     * 关联 amazon_shop.id
     */
    private Long shopId;
    /**
     * 请求站点国家代码，例如 US
     */
    private String countryCode;
    /**
     * Amazon Marketplace ID 列表，Reports API 最多支持 25 个
     */
    private String marketplaceIds;
    /**
     * Amazon 报表类型，例如 GET_MERCHANT_LISTINGS_ALL_DATA
     */
    private String reportType;
    /**
     * 报表附加选项，随报表类型变化
     */
    private String reportOptions;
    /**
     * 报表数据开始时间
     */
    private LocalDateTime dataStartTime;
    /**
     * 报表数据结束时间
     */
    private LocalDateTime dataEndTime;
    /**
     * Amazon reportId；在店铺维度唯一
     */
    private String amazonReportId;
    /**
     * 创建该报表的 Amazon 计划编号；手工请求为空
     */
    private String amazonReportScheduleId;
    /**
     * Amazon 状态：IN_QUEUE、IN_PROGRESS、DONE、CANCELLED、FATAL
     */
    private String amazonProcessingStatus;
    /**
     * Amazon 创建报表时间
     */
    private LocalDateTime amazonCreatedTime;
    /**
     * Amazon 开始处理时间
     */
    private LocalDateTime processingStartTime;
    /**
     * Amazon 完成处理时间
     */
    private LocalDateTime processingEndTime;
    /**
     * Amazon reportDocumentId
     */
    private String reportDocumentId;
    /**
     * 下载文件压缩算法，例如 GZIP
     */
    private String compressionAlgorithm;
    /**
     * 已下载并归档的文件编号，对应 infra_file.id
     */
    private Long fileId;
    /**
     * 报表文件下载完成时间
     */
    private LocalDateTime downloadTime;
    /**
     * 任务状态：0-待提交，1-等待Amazon处理，2-待下载，3-成功，4-重试等待，5-失败，6-已取消
     */
    private Integer taskStatus;
    /**
     * 当前执行阶段：0-提交，1-查询状态，2-下载文件
     */
    private Integer executeStage;
    /**
     * 当前阶段已重试次数
     */
    private Integer retryCount;
    /**
     * 当前阶段最大重试次数
     */
    private Integer maxRetryCount;
    /**
     * 下次可执行时间；用于退避重试和轮询调度
     */
    private LocalDateTime nextRetryTime;
    /**
     * 最近一次调用 Amazon 时间
     */
    private LocalDateTime lastRequestTime;
    /**
     * 最近一次失败错误码
     */
    private String lastErrorCode;
    /**
     * 最近一次失败原因
     */
    private String lastErrorMessage;
    /**
     * 任务成功、失败或取消的最终完成时间
     */
    private LocalDateTime completedTime;
    /**
     * 乐观锁版本，防止多个任务执行器重复处理
     */
    private Integer lockVersion;
    /**
     * 任务执行租约到期时间；超时后允许其他执行器接管
     */
    private LocalDateTime lockExpireTime;


}