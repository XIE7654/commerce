package cn.iocoder.yudao.module.temu.dal.dataobject.orderdetail;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Temu 父订单详情 DO。
 *
 * <p>子订单、包裹和商品等嵌套信息保留为 JSON，避免同步时丢失 Temu 详情接口中的字段。</p>
 */
@TableName("temu_order_detail")
@KeySequence("temu_order_detail_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuOrderDetailDO extends BaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联 temu_shop.id。 */
    private Long shopId;
    /** Temu 父订单号。 */
    private String parentOrderSn;
    /** 父订单状态。 */
    private Integer parentOrderStatus;
    /** 父订单地址一级行政区名称。 */
    @TableField("region_name_1")
    private String regionName1;
    /** 父订单地址二级行政区名称。 */
    @TableField("region_name_2")
    private String regionName2;
    /** 父订单地址三级行政区名称。 */
    @TableField("region_name_3")
    private String regionName3;
    /** Temu 站点编号。 */
    private Long siteId;
    /** Temu 区域编号。 */
    private Long regionId;
    /** 父订单支付类型。 */
    private String orderPaymentType;
    /** 配送方式。 */
    private Integer shippingMethod;
    /** 是否由主商城合单发货。 */
    private Boolean shipmentConsolidatedByMainMall;
    /** 用户是否支付零运费。 */
    private Boolean hasShippingFee;
    /** 父订单创建时间。 */
    private LocalDateTime parentOrderTime;
    /** 最晚发货时间。 */
    private LocalDateTime expectShipLatestTime;
    /** 待处理完成时间。 */
    private LocalDateTime parentOrderPendingFinishTime;
    /** 最晚送达时间。 */
    private LocalDateTime latestDeliveryTime;
    /** 父订单发货时间。 */
    private LocalDateTime parentShippingTime;
    /** 父订单确认时间。 */
    private LocalDateTime parentConfirmTime;
    /** 父订单标签 JSON。 */
    private String parentOrderLabels;
    /** 父订单履约提醒 JSON。 */
    private String fulfillmentWarnings;
    /** 协作仓批次订单号 JSON。 */
    private String batchOrderNumberList;
    /** Temu 原始子订单详情 JSON。 */
    private String orderList;
    /** 最近从 Temu 同步时间。 */
    private LocalDateTime lastSyncTime;
}
