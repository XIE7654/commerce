package cn.iocoder.yudao.module.amazon.controller.admin.uploads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/** Amazon Uploads 上传目的地创建参数。 */
@Data
public class UploadsCreateDestinationReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "调用端点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "上传资源路径，不包含 uploads 前缀", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "aplus/2020-11-01/contentDocuments")
    @NotBlank(message = "资源路径不能为空")
    private String resource;

    @Schema(description = "上传内容的 Base64 MD5 摘要", requiredMode = Schema.RequiredMode.REQUIRED, example = "1B2M2Y8AsgTpgAmY7PhCfg==")
    @NotBlank(message = "contentMD5 不能为空")
    private String contentMD5;

    @Schema(description = "资源所属 Marketplace ID，Amazon 限制最多一个", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "[\"ATVPDKIKX0DER\"]")
    @NotNull(message = "marketplaceIds 不能为空")
    @Size(min = 1, max = 1, message = "marketplaceIds 必须且只能包含一个站点")
    private List<@NotBlank(message = "Marketplace ID 不能为空") String> marketplaceIds;

    @Schema(description = "上传文件的 Content-Type；A+ 内容必填", example = "application/json")
    private String contentType;
}
