package cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Amazon Sellers 查询的店铺和区域参数。 */
@Data
public class AmazonSellersReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "用于选择 SP-API 区域端点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空") private String countryCode;
}
