package cn.iocoder.yudao.module.temu.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Temu 店铺 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemuShopRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16844")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "店铺类型：1-全托管，2-半托管，3-本土店铺", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("店铺类型：1-全托管，2-半托管，3-本土店铺")
    private Integer shopType;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Temu 站点代码，例如 US、DE、JP")
    private String site;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("店铺名称")
    private String shopName;

    @Schema(description = "Temu 授权 Token", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Temu 授权 Token")
    private String authToken;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}