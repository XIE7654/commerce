package cn.iocoder.yudao.module.amazon.controller.admin.externalfulfillment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Amazon External Fulfillment 接口的通用请求参数。
 *
 * <p>具体请求体和查询字段随操作不同而变化，按 Amazon 2024-09-11 模型原样透传。</p>
 */
@Data
public class ExternalFulfillmentRequestVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "External Fulfillment 货件编号", example = "SHIPMENT-001")
    private String shipmentId;

    @Schema(description = "External Fulfillment 包裹编号", example = "PACKAGE-001")
    private String packageId;

    @Schema(description = "External Fulfillment 退货编号", example = "RETURN-001")
    private String returnId;

    @Schema(description = "查询参数，字段按 External Fulfillment 模型传入")
    private Map<String, String> query;

    @Schema(description = "Amazon 请求体，字段按 External Fulfillment 模型传入")
    private Map<String, Object> body;
}
