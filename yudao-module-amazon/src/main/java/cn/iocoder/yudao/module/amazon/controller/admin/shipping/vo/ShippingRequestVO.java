package cn.iocoder.yudao.module.amazon.controller.admin.shipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

/** Shipping V1/V2 接口通用请求参数。 */
@Data
public class ShippingRequestVO {
    @NotNull(message = "店铺编号不能为空") @Schema(description = "Amazon 店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") private Long shopId;
    @NotBlank(message = "国家代码不能为空") @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US") private String countryCode;
    @Schema(description = "货件、承运商或集合单据编号") private String resourceId;
    @Schema(description = "货件容器或追踪编号") private String secondaryResourceId;
    @Schema(description = "查询参数，字段按 Shipping API 模型传入") private Map<String, String> query;
    @Schema(description = "按 Shipping API 模型传入的请求体") private Map<String, Object> body;
    @Schema(description = "Shipping V2 必填业务编号，对应 x-amzn-shipping-business-id", example = "AmazonShipping_US") private String shippingBusinessId;
    @Schema(description = "Shipping V2 幂等键；创建类接口建议传入", example = "a6fa0e22-09b3-4e98-bc14-1e0cd4c3ec70") private String idempotencyKey;
}
