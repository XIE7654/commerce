package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Products Listing 图片上传请求参数。
 */
@Schema(description = "管理后台 - Products Listing 图片上传 Request VO")
@Data
public class ProductsListingImageUploadReqVO extends ProductsListingBaseReqVO {

    /** 可被 Temu 服务端访问的图片地址。 */
    @Schema(description = "图片 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "图片 URL 不能为空")
    private String fileUrl;

    /** 图片格式转换方式，取值以 Temu 平台定义为准。 */
    @Schema(description = "格式转换方式", example = "1")
    private Integer formatConversionType;

    /** 图片缩放方式，取值以 Temu 平台定义为准。 */
    @Schema(description = "缩放方式", example = "1")
    private Integer scalingType;

    /** 图片压缩方式，取值以 Temu 平台定义为准。 */
    @Schema(description = "压缩方式", example = "0")
    private Integer compressionType;
}
