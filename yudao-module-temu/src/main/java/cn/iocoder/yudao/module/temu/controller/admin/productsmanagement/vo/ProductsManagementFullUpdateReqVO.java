package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.JsonNode;

/**
 * Products Management 商品完整更新请求参数。
 */
@Schema(description = "管理后台 - Products Management 商品完整更新 Request VO")
@Data
public class ProductsManagementFullUpdateReqVO extends ProductsManagementGoodsIdReqVO {

    /** 商品属性配置，按 Temu goodsProperty 结构传入。 */
    @Schema(description = "商品属性配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品属性配置不能为空")
    private JsonNode goodsProperty;

    /** 商品基础信息，按 Temu goodsBasic 结构传入。 */
    @Schema(description = "商品基础信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品基础信息不能为空")
    private JsonNode goodsBasic;

    /** 商品履约服务承诺，按 Temu goodsServicePromise 结构传入。 */
    @Schema(description = "商品履约服务承诺", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品履约服务承诺不能为空")
    private JsonNode goodsServicePromise;

    /** SKU 更新列表，按 Temu skuList 结构传入。 */
    @Schema(description = "SKU 更新列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "SKU 更新列表不能为空")
    private JsonNode skuList;
}
