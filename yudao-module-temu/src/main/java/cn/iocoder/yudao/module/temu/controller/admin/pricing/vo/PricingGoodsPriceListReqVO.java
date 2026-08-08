package cn.iocoder.yudao.module.temu.controller.admin.pricing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** 查询 Temu 商品 SKU 供货价请求参数。 */
@Data
public class PricingGoodsPriceListReqVO extends PricingBaseReqVO {
    /** 待查询的商品及 SKU。 */
    @NotEmpty(message = "商品价格查询条件不能为空") @Valid
    private List<QuerySupplierPriceBase> querySupplierPriceBaseList;

    /** 单个商品的 SKU 价格查询条件。 */
    @Data public static class QuerySupplierPriceBase {
        /** Temu 商品 ID。 */ @NotNull(message = "商品 ID 不能为空") private Long goodsId;
        /** 待查询的 SKU ID 列表。 */ @NotEmpty(message = "SKU ID 列表不能为空") private List<Long> skuIdList;
    }
}
