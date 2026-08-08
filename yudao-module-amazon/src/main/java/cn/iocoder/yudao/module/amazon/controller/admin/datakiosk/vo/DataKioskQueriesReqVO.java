package cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.util.List;

/** Data Kiosk 查询任务列表参数。 */
@Data
public class DataKioskQueriesReqVO extends DataKioskBaseReqVO {
    @Schema(description = "用于筛选的处理状态", example = "DONE") private List<String> processingStatuses;
    @Schema(description = "每页最大任务数", example = "20") @Min(value = 1, message = "pageSize 不能小于 1") @Max(value = 100, message = "pageSize 不能大于 100") private Integer pageSize;
    @Schema(description = "最早创建时间，ISO 8601 日期时间") private String createdSince;
    @Schema(description = "最晚创建时间，ISO 8601 日期时间") private String createdUntil;
    @Schema(description = "分页令牌") private String paginationToken;
}
