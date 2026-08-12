package cn.iocoder.yudao.module.amazon.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Amazon 受监管订单验证状态更新请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonOrderRegulatedInfoUpdateReqVO extends AmazonOrderGetReqVO {

    @Schema(description = "写入目标站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "验证状态内容，需包含 externalReviewerId 及 Amazon 要求的状态字段", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "验证状态内容不能为空")
    private Map<String, Object> regulatedOrderVerificationStatus;

}
