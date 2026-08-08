package cn.iocoder.yudao.module.amazon.controller.admin.productpricing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/** Amazon Product Pricing V0 请求参数。 */
@Data
public class AmazonProductPricingReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "目标站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "ASIN 列表，价格与竞争价格查询最多 20 个", example = "B000000000,B000000001")
    @Size(max = 20, message = "ASIN 最多 20 个")
    private List<String> asins;

    @Schema(description = "卖家 SKU 列表，价格与竞争价格查询最多 20 个", example = "SKU-001,SKU-002")
    @Size(max = 20, message = "卖家 SKU 最多 20 个")
    private List<String> skus;

    @Schema(description = "单个报价查询使用的 ASIN", example = "B000000000")
    private String asin;

    @Schema(description = "单个报价查询使用的卖家 SKU", example = "SKU-001")
    private String sellerSku;

    @Schema(description = "价格标识类型；价格和竞争价格接口必填，取值 Asin 或 Sku", example = "Asin")
    private String itemType;

    @Schema(description = "商品成色；报价接口必填，取值 New、Used、Collectible、Refurbished 或 Club", example = "New")
    private String itemCondition;

    @Schema(description = "买家类型；竞争价格和报价接口可选，取值 Consumer 或 Business", example = "Consumer")
    private String customerType;

    @Schema(description = "报价类型；价格接口可选，取值 B2C 或 B2B", example = "B2C")
    private String offerType;

    @Schema(description = "批量报价的 Amazon 官方请求体", example = "{\"requests\":[{\"uri\":\"/products/pricing/v0/items/B000000000/offers\",\"method\":\"GET\",\"MarketplaceId\":\"ATVPDKIKX0DER\",\"ItemCondition\":\"New\"}]}")
    private Object body;
}
