package cn.iocoder.yudao.module.temu.controller.admin.ads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** Temu Ads 广告创建请求参数。 */
@Schema(description = "管理后台 - Temu Ads 广告创建 Request VO")
@Data
public class AdsCreateReqVO extends AdsBaseReqVO {
    /** 待创建的广告配置。 */
    @Schema(description = "待创建的广告配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "广告配置不能为空")
    @Valid
    private List<CreateAdReq> createAdReqs;

    /** 单个广告创建配置。 */
    @Data
    public static class CreateAdReq {
        /** Temu 商品 ID。 */
        @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
        @NotNull(message = "商品 ID 不能为空")
        private Long goodsId;
        /** 广告 ROAS 出价。 */
        @Schema(description = "广告 ROAS 出价", requiredMode = Schema.RequiredMode.REQUIRED, example = "3.5")
        @NotNull(message = "ROAS 不能为空")
        private BigDecimal roas;
        /** ROAS 类型，取值以 Temu 平台定义为准。 */
        @Schema(description = "ROAS 类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "ROAS 类型不能为空")
        private Integer roasType;
        /** 广告每日预算。 */
        @Schema(description = "广告每日预算", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
        @NotNull(message = "广告预算不能为空")
        private BigDecimal budget;
    }
}
