package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tools.jackson.databind.JsonNode;

import java.util.List;

/**
 * Products Management 商品部分更新请求参数。
 */
@Schema(description = "管理后台 - Products Management 商品部分更新 Request VO")
@Data
public class ProductsManagementPartialUpdateReqVO extends ProductsManagementGoodsIdReqVO {

    /** 商品卖点列表。 */
    @Schema(description = "商品卖点列表", example = "[\"Update\"]")
    private List<String> bulletPoints;

    /** 商品分类 ID。 */
    @Schema(description = "商品分类 ID", example = "1179")
    private Long catId;

    /** 商品详情描述。 */
    @Schema(description = "商品详情描述", example = "Update")
    private String goodsDesc;

    /** 商品名称。 */
    @Schema(description = "商品名称", example = "Esellerpro Executive Pen")
    private String goodsName;

    /** 商品属性列表，按 Temu goodsProperties 结构传入。 */
    @Schema(description = "商品属性列表")
    private JsonNode goodsProperties;

    /** 商品履约服务承诺，按 Temu goodsServicePromise 结构传入。 */
    @Schema(description = "商品履约服务承诺")
    private JsonNode goodsServicePromise;
}
