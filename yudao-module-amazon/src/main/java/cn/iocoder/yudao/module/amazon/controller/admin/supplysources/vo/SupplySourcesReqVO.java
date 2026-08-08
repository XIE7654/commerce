package cn.iocoder.yudao.module.amazon.controller.admin.supplysources.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Supply Sources API 请求参数。 */
@Data
public class SupplySourcesReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US") @NotBlank(message = "国家代码不能为空") private String countryCode;
    @Schema(description = "供货源编号") private String supplySourceId;
    @Schema(description = "分页令牌") private String nextPageToken;
    @Schema(description = "单页数量") private Integer pageSize;
    @Schema(description = "符合 Amazon Supply Sources 官方模型的请求体") private Map<String, Object> body;
}
