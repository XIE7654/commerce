package cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** 创建 Seller Central 通知的请求参数。 */
@Data
public class AmazonCreateNotificationReqVO extends AmazonAppIntegrationsShopReqVO {

    @NotBlank
    @Schema(description = "应用入驻时配置的通知模板标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "PRICE_CHANGE")
    private String templateId;

    // Amazon 允许无动态参数的通知模板传入空对象，但该字段本身必须存在。
    @NotNull
    @Schema(description = "模板要求的动态通知参数", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"priceValue\":\"200\"}")
    private Map<String, Object> notificationParameters;

    @Schema(description = "通知对应的加密 Marketplace 标识", example = "ATVPDKIKX0DER")
    private String marketplaceId;
}
