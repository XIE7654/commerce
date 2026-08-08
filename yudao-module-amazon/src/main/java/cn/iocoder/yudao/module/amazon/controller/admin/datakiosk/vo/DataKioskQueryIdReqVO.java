package cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Data Kiosk 查询任务标识参数。 */
@Data
public class DataKioskQueryIdReqVO extends DataKioskBaseReqVO {
    @Schema(description = "Data Kiosk 查询任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "QueryId1")
    @NotBlank(message = "查询任务编号不能为空") private String queryId;
}
