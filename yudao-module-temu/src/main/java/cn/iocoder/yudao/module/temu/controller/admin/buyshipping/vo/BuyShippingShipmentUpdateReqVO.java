package cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Buy Shipping 更新重试发货包裹请求参数。
 */
@Schema(description = "管理后台 - Buy Shipping 更新货件 Request VO")
@Data
public class BuyShippingShipmentUpdateReqVO extends BuyShippingBaseReqVO {

    /** 重试发货包裹明细，按 Temu retrySendPackageRequestList 结构传入。 */
    @Schema(description = "重试发货包裹明细", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "重试发货包裹明细不能为空")
    private JsonNode retrySendPackageRequestList;
}
