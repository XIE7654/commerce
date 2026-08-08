package cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Buy Shipping 可用物流服务查询请求参数。
 */
@Schema(description = "管理后台 - Buy Shipping 可用物流服务查询 Request VO")
@Data
public class BuyShippingServicesReqVO extends BuyShippingBaseReqVO {

    /** 发货仓库编号。 */
    @Schema(description = "仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "WH-03283345879192414")
    @NotBlank(message = "仓库编号不能为空")
    private String warehouseId;

    /** 包裹长度。 */
    @Schema(description = "包裹长度", requiredMode = Schema.RequiredMode.REQUIRED, example = "12.5")
    @NotBlank(message = "包裹长度不能为空")
    private String length;

    /** 包裹宽度。 */
    @Schema(description = "包裹宽度", requiredMode = Schema.RequiredMode.REQUIRED, example = "8.0")
    @NotBlank(message = "包裹宽度不能为空")
    private String width;

    /** 包裹高度。 */
    @Schema(description = "包裹高度", requiredMode = Schema.RequiredMode.REQUIRED, example = "4.0")
    @NotBlank(message = "包裹高度不能为空")
    private String height;

    /** 包裹重量。 */
    @Schema(description = "包裹重量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2.5")
    @NotBlank(message = "包裹重量不能为空")
    private String weight;

    /** 尺寸单位，使用 Temu 支持的单位值。 */
    @Schema(description = "尺寸单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "in")
    @NotBlank(message = "尺寸单位不能为空")
    private String dimensionUnit;

    /** 重量单位，使用 Temu 支持的单位值。 */
    @Schema(description = "重量单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "lb")
    @NotBlank(message = "重量单位不能为空")
    private String weightUnit;

    /** 待购买物流服务的子订单编号。 */
    @Schema(description = "子订单编号列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "子订单编号不能为空")
    private List<String> orderSnList;
}
