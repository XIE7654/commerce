package cn.iocoder.yudao.module.amazon.controller.admin.listings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Amazon 单个 Listings Item 查询请求参数。
 */
@Data
public class AmazonListingsItemGetReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "卖家定义的商品 SKU", requiredMode = Schema.RequiredMode.REQUIRED, example = "SKU-001")
    @NotBlank(message = "SKU 不能为空")
    private String sku;

    @Schema(description = "需要返回的数据集；默认 summaries", example = "summaries,attributes,issues")
    private List<String> includedData;

    @Schema(description = "问题本地化语言", example = "en_US")
    private String issueLocale;
}
