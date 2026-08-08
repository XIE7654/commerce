package cn.iocoder.yudao.module.temu.controller.admin.ads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/** Temu Ads 广告修改请求参数。 */
@Schema(description = "管理后台 - Temu Ads 广告修改 Request VO")
@Data
public class AdsModifyReqVO extends AdsBaseReqVO {
    /** 修改类型：1 删除、2 暂停、3 开启、4 修改预算、5 修改 ROAS。 */
    @Schema(description = "修改类型：1 删除、2 暂停、3 开启、4 修改预算、5 修改 ROAS", requiredMode = Schema.RequiredMode.REQUIRED, example = "4")
    @NotNull(message = "修改类型不能为空")
    private Integer status;
    /** 待修改广告配置。 */
    @Schema(description = "待修改广告配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "广告配置不能为空")
    @Valid
    private ModifyAdDTO modifyAdDTO;

    /** 广告修改参数，具体需要的出价或预算随修改类型确定。 */
    @Data
    public static class ModifyAdDTO {
        /** Temu 商品 ID。 */
        @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
        @NotNull(message = "商品 ID 不能为空")
        private Long goodsId;
        /** 修改 ROAS 时传入的目标出价。 */
        @Schema(description = "目标 ROAS", example = "3.5")
        private BigDecimal roas;
        /** 修改预算时传入的目标每日预算。 */
        @Schema(description = "目标每日预算", example = "20")
        private BigDecimal budget;
    }
}
