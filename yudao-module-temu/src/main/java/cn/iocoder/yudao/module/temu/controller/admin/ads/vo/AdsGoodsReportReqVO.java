package cn.iocoder.yudao.module.temu.controller.admin.ads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Temu Ads 商品维度报表查询请求参数。 */
@Schema(description = "管理后台 - Temu Ads 商品报表查询 Request VO")
@Data
public class AdsGoodsReportReqVO extends AdsMallReportReqVO {
    /** Temu 商品 ID。 */
    @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
    @NotNull(message = "商品 ID 不能为空")
    private Long goodsId;
}
