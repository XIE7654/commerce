package cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Data Kiosk 文档标识参数。 */
@Data
public class DataKioskDocumentIdReqVO extends DataKioskBaseReqVO {
    @Schema(description = "Data Kiosk 文档编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "DocumentId1")
    @NotBlank(message = "文档编号不能为空") private String documentId;
}
