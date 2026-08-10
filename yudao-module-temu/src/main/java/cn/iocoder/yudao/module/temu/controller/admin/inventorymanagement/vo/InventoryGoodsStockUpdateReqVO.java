package cn.iocoder.yudao.module.temu.controller.admin.inventorymanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Temu 商品 SKU 库存更新请求参数。
 */
@Schema(description = "管理后台 - Inventory Management 商品库存更新 Request VO")
@Data
public class InventoryGoodsStockUpdateReqVO {
    private Long shopId;

    /** Temu 站点代码，决定服务端读取的区域应用配置。 */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /** 本次调用使用的 Temu 店铺授权 Token。 */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;

    /** 调用方生成的幂等键，重试同一库存操作时应保持不变。 */
    @Schema(description = "请求幂等键", requiredMode = Schema.RequiredMode.REQUIRED, example = "stock-update-20260808-001")
    @NotBlank(message = "requestUniqueKey 不能为空")
    private String requestUniqueKey;

    /** Temu 商品 ID。 */
    @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "602238349110173")
    @NotNull(message = "商品 ID 不能为空")
    private Long goodsId;

    /** 将 SKU 库存直接设置为目标值的明细；可与增量库存明细组合传入。 */
    @Schema(description = "SKU 目标库存明细")
    @Valid
    private List<SkuStockTarget> skuStockTargetList;

    /** 按增量调整 SKU 库存的明细；正数增加库存，负数扣减库存。 */
    @Schema(description = "SKU 库存增量明细")
    @Valid
    private List<SkuStockChange> skuStockChangeList;

    /**
     * 单个 SKU 的目标库存设置。
     */
    @Data
    public static class SkuStockTarget {

        /** Temu SKU ID。 */
        @Schema(description = "Temu SKU ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "43510166225830")
        @NotNull(message = "SKU ID 不能为空")
        private Long skuId;

        /** 更新后的 SKU 库存数量。 */
        @Schema(description = "目标库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @NotNull(message = "目标库存不能为空")
        private Integer stockTarget;
    }

    /**
     * 单个 SKU 的库存增量调整。
     */
    @Data
    public static class SkuStockChange {

        /** Temu SKU ID。 */
        @Schema(description = "Temu SKU ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "43510166225830")
        @NotNull(message = "SKU ID 不能为空")
        private Long skuId;

        /** 本次库存变更量，负数表示扣减库存。 */
        @Schema(description = "库存变更量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
        @NotNull(message = "库存变更量不能为空")
        private Integer stockDiff;
    }
}
