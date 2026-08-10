package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * Temu 本地订单分页查询请求参数。
 */
@Schema(description = "管理后台 - Temu 本地订单分页 Request VO")
@Data
public class TemuOrderPageReqVO extends PageParam {

    /** 店铺编号。 */
    @Schema(description = "关联 temu_shop.id", example = "1")
    private Long shopId;
    /** 卖家编号。 */
    @Schema(description = "关联 temu_seller.id", example = "1")
    private Long sellerId;
    /** 父订单号。 */
    @Schema(description = "Temu 父订单号", example = "PO-211-18671748222072338")
    private String parentOrderSn;
    /** 子订单号。 */
    @Schema(description = "Temu 子订单号", example = "211-18671816379512338")
    private String orderSn;
    /** 父订单状态。 */
    @Schema(description = "父订单状态", example = "4")
    private Integer parentOrderStatus;
    /** 子订单状态。 */
    @Schema(description = "子订单状态", example = "4")
    private Integer orderStatus;
    /** 下单时间范围。 */
    @Schema(description = "子订单下单时间范围")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] orderCreateTime;
}
