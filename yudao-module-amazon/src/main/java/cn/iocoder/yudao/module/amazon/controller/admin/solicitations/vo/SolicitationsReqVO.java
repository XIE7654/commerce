package cn.iocoder.yudao.module.amazon.controller.admin.solicitations.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** Solicitations API 请求参数。 */
@Data
public class SolicitationsReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US") @NotBlank(message = "国家代码不能为空") private String countryCode;
    @Schema(description = "Amazon 订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "123-1234567-1234567") @NotBlank(message = "Amazon 订单编号不能为空") private String amazonOrderId;
    @Schema(description = "Marketplace ID 列表；未传时使用国家默认站点") private List<String> marketplaceIds;
}
