package cn.iocoder.yudao.module.temu.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - Temu 店铺新增/修改 Request VO")
@Data
public class ShopSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2275")
    private Long id;

    @Schema(description = "店铺类型：1-全托管，2-半托管，3-本土店铺", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺类型：1-全托管，2-半托管，3-本土店铺不能为空")
    private Integer shopType;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Temu 站点代码，例如 US、DE、JP不能为空")
    private String site;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "店铺名称不能为空")
    private String shopName;

    @Schema(description = "Temu 授权 Token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Temu 授权 Token不能为空")
    private String authToken;

    @Schema(description = "Temu 授权时间", example = "2026-08-07T12:00:00")
    private LocalDateTime authorizeTime;

    @Schema(description = "Temu 授权过期时间", example = "2027-08-07T12:00:00")
    private LocalDateTime authorizeExpireTime;

}
