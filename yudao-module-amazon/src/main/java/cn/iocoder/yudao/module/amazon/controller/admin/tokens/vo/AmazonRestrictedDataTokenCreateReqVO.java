package cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/** 创建 Amazon Restricted Data Token 的请求参数。 */
@Data
public class AmazonRestrictedDataTokenCreateReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "用于选择 SP-API 区域端点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "被委托访问的目标应用 ID")
    private String targetApplication;

    @Schema(description = "请求 RDT 的受限资源，最多 50 项", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "受限资源不能为空")
    @Size(max = 50, message = "受限资源不能超过 50 项")
    @Valid
    private List<AmazonRestrictedResourceReqVO> restrictedResources;

}
