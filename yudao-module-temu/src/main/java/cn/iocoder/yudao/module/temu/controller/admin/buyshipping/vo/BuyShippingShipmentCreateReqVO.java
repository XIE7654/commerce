package cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Buy Shipping 创建货件请求参数。
 */
@Schema(description = "管理后台 - Buy Shipping 创建货件 Request VO")
@Data
public class BuyShippingShipmentCreateReqVO extends BuyShippingBaseReqVO {

    /** 发货方式，具体取值以 Temu 平台定义为准。 */
    @Schema(description = "发货方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "发货方式不能为空")
    private Integer sendType;

    /** 货件明细，按 Temu sendRequestList 结构传入。 */
    @Schema(description = "货件明细", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "货件明细不能为空")
    private JsonNode sendRequestList;
}
