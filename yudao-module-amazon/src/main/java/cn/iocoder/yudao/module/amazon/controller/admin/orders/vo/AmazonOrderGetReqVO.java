package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Amazon 指定订单查询请求参数。
 */
@Data
public class AmazonOrderGetReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "Amazon 订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "902-3159896-1390916")
    @NotBlank(message = "Amazon 订单编号不能为空")
    private String orderId;

}
