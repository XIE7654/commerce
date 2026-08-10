package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Temu 本地订单分页响应参数。
 */
@Schema(description = "管理后台 - Temu 本地订单 Response VO")
@Data
public class TemuOrderRespVO {

    /** 主键编号。 */
    private Long id;
    /** 关联店铺编号。 */
    private Long shopId;
    /** 关联卖家编号。 */
    private Long sellerId;
    /** 父订单号。 */
    private String parentOrderSn;
    /** 子订单号。 */
    private String orderSn;
    /** 父订单状态。 */
    private Integer parentOrderStatus;
    /** 子订单状态。 */
    private Integer orderStatus;
    /** 商品名称。 */
    private String goodsName;
    /** 商品规格。 */
    private String spec;
    /** 商品缩略图。 */
    private String thumbUrl;
    /** 下单数量。 */
    private Integer quantity;
    /** 履约方式。 */
    private String fulfillmentType;
    /** 下单时间。 */
    private LocalDateTime orderCreateTime;
    /** 最晚预计发货时间。 */
    private LocalDateTime expectShipLatestTime;
    /** 最近同步时间。 */
    private LocalDateTime lastSyncTime;
}
