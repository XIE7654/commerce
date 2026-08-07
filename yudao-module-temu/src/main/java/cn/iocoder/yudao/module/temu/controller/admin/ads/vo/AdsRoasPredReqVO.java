package cn.iocoder.yudao.module.temu.controller.admin.ads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/** Temu Ads ROAS 预测请求参数。 */
@Schema(description = "管理后台 - Temu Ads ROAS 预测 Request VO")
@Data
public class AdsRoasPredReqVO extends AdsBaseReqVO {

    /** 待预测商品信息列表。 */
    @Schema(description = "待预测商品信息列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商品信息列表不能为空")
    @Valid
    private List<GoodsInfo> goodsInfoList;

    /** ROAS 预测商品信息。 */
    @Data
    public static class GoodsInfo {
        /** Temu 商品 ID。 */
        @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
        @NotNull(message = "商品 ID 不能为空")
        private Long goodsId;
    }
}
