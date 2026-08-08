package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Amazon 订单发货确认请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonOrderShipmentConfirmationReqVO extends AmazonOrderGetReqVO {

    @Schema(description = "包裹明细，需符合 Amazon PackageDetail 结构", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "包裹明细不能为空")
    private Map<String, Object> packageDetail;

    @Schema(description = "货到付款收款方式，仅日本站支持", example = "DirectPayment")
    private String codCollectionMethod;

}
