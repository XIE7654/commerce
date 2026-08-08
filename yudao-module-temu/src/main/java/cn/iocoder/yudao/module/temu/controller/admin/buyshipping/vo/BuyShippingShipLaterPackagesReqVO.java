package cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Buy Shipping 待发货包裹分页查询请求参数。
 */
@Schema(description = "管理后台 - Buy Shipping 待发货包裹查询 Request VO")
@Data
public class BuyShippingShipLaterPackagesReqVO extends BuyShippingBaseReqVO {

    /** 页码，从 1 开始。 */
    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于等于 1")
    private Integer pageNumber;

    /** 每页记录数。 */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "每页记录数不能为空")
    @Min(value = 1, message = "每页记录数必须大于等于 1")
    private Integer pageSize;
}
