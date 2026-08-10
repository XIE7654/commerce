package cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Temu 区域承运商目录新增/修改 Request VO")
@Data
public class TemuShippingCompanySaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19996")
    private Long id;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Temu 站点代码，例如 US、DE、JP不能为空")
    private String site;

    @Schema(description = "Temu 区域编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1004")
    @NotNull(message = "Temu 区域编号不能为空")
    private Long regionId;

    @Schema(description = "Temu 物流服务商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6863")
    @NotNull(message = "Temu 物流服务商编号不能为空")
    private Long logisticsServiceProviderId;

    @Schema(description = "物流服务商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "物流服务商名称不能为空")
    private String logisticsServiceProviderName;

    @Schema(description = "物流品牌名称", example = "赵六")
    private String logisticsBrandName;

    @Schema(description = "最近一次从 Temu 同步的时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最近一次从 Temu 同步的时间不能为空")
    private LocalDateTime lastSyncTime;

}