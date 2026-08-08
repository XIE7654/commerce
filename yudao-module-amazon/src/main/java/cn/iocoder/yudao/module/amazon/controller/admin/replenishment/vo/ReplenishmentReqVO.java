package cn.iocoder.yudao.module.amazon.controller.admin.replenishment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Replenishment API 请求参数。 */
@Data
public class ReplenishmentReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
    @Schema(description = "符合 Amazon Replenishment 官方模型的请求体", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请求体不能为空")
    private Map<String, Object> body;
}
