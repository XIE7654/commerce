package cn.iocoder.yudao.module.temu.controller.admin.ads.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Temu Ads 广告日志查询请求参数。 */
@Schema(description = "管理后台 - Temu Ads 广告日志查询 Request VO")
@Data
public class AdsLogQueryReqVO extends AdsBaseReqVO {
    /** Temu 商品 ID。 */
    @Schema(description = "Temu 商品 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789")
    @NotNull(message = "商品 ID 不能为空")
    private Long goodsId;
    /** 本地时区当天零点的查询开始毫秒时间戳。 */
    @Schema(description = "查询开始毫秒时间戳", requiredMode = Schema.RequiredMode.REQUIRED, example = "1767225600000")
    @NotNull(message = "查询开始时间不能为空")
    private Long startTime;
    /** 本地时区当天 23:59:59.999 的查询结束毫秒时间戳。 */
    @Schema(description = "查询结束毫秒时间戳", requiredMode = Schema.RequiredMode.REQUIRED, example = "1767311999999")
    @NotNull(message = "查询结束时间不能为空")
    private Long endTime;
}
