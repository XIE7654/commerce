package cn.iocoder.yudao.module.amazon.controller.admin.listings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Amazon Listings Item 局部更新请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonListingsItemPatchReqVO extends AmazonListingsItemGetReqVO {

    @Schema(description = "Amazon 商品类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "PRODUCT")
    @NotBlank(message = "商品类型不能为空")
    private String productType;
    @Schema(description = "JSON Patch 操作列表，支持 add、replace、merge、delete", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Patch 操作不能为空")
    private List<Map<String, Object>> patches;
    @Schema(description = "提交模式", example = "VALIDATION_PREVIEW")
    private String mode;
}
