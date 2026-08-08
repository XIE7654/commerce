package cn.iocoder.yudao.module.amazon.controller.admin.listings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Amazon Listings Restrictions 查询请求参数。
 */
@Data
public class AmazonListingsRestrictionsReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;
    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
    @Schema(description = "Amazon Standard Identification Number", requiredMode = Schema.RequiredMode.REQUIRED, example = "B000000000")
    @NotBlank(message = "ASIN 不能为空")
    private String asin;
    @Schema(description = "商品状况", example = "new_new")
    private String conditionType;
    @Schema(description = "限制原因本地化语言", example = "en_US")
    private String reasonLocale;
    @Schema(description = "Amazon 商品类型", example = "PRODUCT")
    private String productType;

}
