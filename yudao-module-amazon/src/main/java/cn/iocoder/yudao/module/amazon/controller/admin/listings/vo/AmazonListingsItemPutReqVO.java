package cn.iocoder.yudao.module.amazon.controller.admin.listings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Amazon Listings Item 创建或全量更新请求参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonListingsItemPutReqVO extends AmazonListingsItemGetReqVO {

    @Schema(description = "Amazon 商品类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "PRODUCT")
    @NotBlank(message = "商品类型不能为空")
    private String productType;
    @Schema(description = "商品属性，键为 Amazon 属性名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商品属性不能为空")
    private Map<String, Object> attributes;
    @Schema(description = "数据要求集", example = "LISTING")
    private String requirements;
    @Schema(description = "提交模式", example = "VALIDATION_PREVIEW")
    private String mode;
}
