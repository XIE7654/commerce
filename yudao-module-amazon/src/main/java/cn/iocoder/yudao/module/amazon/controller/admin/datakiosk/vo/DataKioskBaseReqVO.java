package cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Data Kiosk 通用店铺与站点参数。 */
@Data
public class DataKioskBaseReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "调用 SP-API 的站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空") private String countryCode;
}
