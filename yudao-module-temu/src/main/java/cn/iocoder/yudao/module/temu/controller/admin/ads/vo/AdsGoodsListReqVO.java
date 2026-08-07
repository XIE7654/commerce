package cn.iocoder.yudao.module.temu.controller.admin.ads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/** Temu Ads 商品列表查询请求参数。 */
@Schema(description = "管理后台 - Temu Ads 商品列表查询 Request VO")
@Data
public class AdsGoodsListReqVO extends AdsBaseReqVO {
    /** Temu 商品 ID 列表。 */
    @Schema(description = "Temu 商品 ID 列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "商品 ID 列表不能为空")
    private List<Long> goodsIds;
}
