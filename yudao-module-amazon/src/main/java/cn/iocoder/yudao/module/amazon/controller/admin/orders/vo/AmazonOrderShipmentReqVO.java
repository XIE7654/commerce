package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Amazon Easy Ship 订单发货状态更新请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonOrderShipmentReqVO extends AmazonOrderGetReqVO {

    @Schema(description = "写入目标站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "发货状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "PickedUp")
    @NotBlank(message = "发货状态不能为空")
    private String shipmentStatus;

    @Schema(description = "可选的订单商品状态明细")
    private List<Map<String, Object>> orderItems;

}
