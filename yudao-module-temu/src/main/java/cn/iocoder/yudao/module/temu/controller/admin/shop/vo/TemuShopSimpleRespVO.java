package cn.iocoder.yudao.module.temu.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - Temu 店铺精简 Response VO")
@Data
public class TemuShopSimpleRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16844")
    private Long id;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Temu 美国店")
    private String shopName;

}
