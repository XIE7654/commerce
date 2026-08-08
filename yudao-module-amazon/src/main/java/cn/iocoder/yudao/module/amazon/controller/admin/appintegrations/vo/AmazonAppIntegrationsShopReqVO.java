package cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Application Integrations 请求共用的店铺与站点参数。 */
@Data
public class AmazonAppIntegrationsShopReqVO {

    @NotNull
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @NotBlank
    @Schema(description = "用于选择 SP-API 区域端点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    private String countryCode;
}
