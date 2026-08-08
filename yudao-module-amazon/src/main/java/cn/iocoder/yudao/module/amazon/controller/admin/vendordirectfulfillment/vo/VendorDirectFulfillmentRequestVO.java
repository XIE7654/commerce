package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Vendor Direct Fulfillment 接口通用请求参数。 */
@Data
public class VendorDirectFulfillmentRequestVO {

    @NotNull(message = "店铺编号不能为空")
    @Schema(description = "Amazon 店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @NotBlank(message = "国家代码不能为空")
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    private String countryCode;

    @Schema(description = "路径参数，例如 warehouseId、purchaseOrderNumber 或 transactionId")
    private Map<String, String> pathParams;

    @Schema(description = "查询参数，字段按 Vendor Direct Fulfillment API 模型传入")
    private Map<String, String> query;

    @Schema(description = "请求体，字段按 Vendor Direct Fulfillment API 模型传入")
    private Map<String, Object> body;
}
