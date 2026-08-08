package cn.iocoder.yudao.module.amazon.controller.admin.deliverybyamazon.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Delivery by Amazon 接口通用请求参数。 */
@Data
public class DeliveryByAmazonRequestVO {
    @NotNull(message = "店铺编号不能为空")
    @Schema(description = "Amazon 店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;
    @NotBlank(message = "国家代码不能为空")
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "IN")
    private String countryCode;
    @Schema(description = "查询参数，按 Delivery by Amazon 模型传入")
    private Map<String, String> query;
    @Schema(description = "Amazon 原始请求体")
    private Map<String, Object> body;
}
