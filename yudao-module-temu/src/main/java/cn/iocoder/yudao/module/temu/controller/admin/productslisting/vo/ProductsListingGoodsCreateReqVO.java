package cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Products Listing 商品创建请求参数。
 */
@Schema(description = "管理后台 - Products Listing 商品创建 Request VO")
@Data
public class ProductsListingGoodsCreateReqVO extends ProductsListingBaseReqVO {

    /** 商品基础信息，按 Temu goodsBasic 结构传入。 */
    @Schema(description = "商品基础信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品基础信息不能为空")
    private JsonNode goodsBasic;

    /** 商品详情描述。 */
    @Schema(description = "商品详情描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品详情描述不能为空")
    private String goodsDesc;

    /** 商品属性，按 Temu goodsProperty 结构传入。 */
    @Schema(description = "商品属性", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品属性不能为空")
    private JsonNode goodsProperty;

    /** 履约服务承诺，按 Temu goodsServicePromise 结构传入。 */
    @Schema(description = "商品履约服务承诺", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品履约服务承诺不能为空")
    private JsonNode goodsServicePromise;

    /** 商品商标信息，按 Temu goodsTrademark 结构传入。 */
    @Schema(description = "商品商标信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品商标信息不能为空")
    private JsonNode goodsTrademark;

    /** SKU 列表，按 Temu skuList 结构传入。 */
    @Schema(description = "SKU 列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "SKU 列表不能为空")
    private JsonNode skuList;
}
