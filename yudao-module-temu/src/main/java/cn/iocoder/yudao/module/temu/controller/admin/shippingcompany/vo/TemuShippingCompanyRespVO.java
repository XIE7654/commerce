package cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Temu 区域承运商目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemuShippingCompanyRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19996")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Temu 站点代码，例如 US、DE、JP")
    private String site;

    @Schema(description = "Temu 区域编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1004")
    @ExcelProperty("Temu 区域编号")
    private Long regionId;

    @Schema(description = "Temu 物流服务商编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6863")
    @ExcelProperty("Temu 物流服务商编号")
    private Long logisticsServiceProviderId;

    @Schema(description = "物流服务商名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("物流服务商名称")
    private String logisticsServiceProviderName;

    @Schema(description = "物流品牌名称", example = "赵六")
    @ExcelProperty("物流品牌名称")
    private String logisticsBrandName;

    @Schema(description = "最近一次从 Temu 同步的时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("最近一次从 Temu 同步的时间")
    private LocalDateTime lastSyncTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}