package cn.iocoder.yudao.module.temu.dal.dataobject.order;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Temu 订单 DO
 *
 * @author 自达源码
 */
@TableName("temu_order")
@KeySequence("temu_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuOrderDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 关联 temu_shop.id
     */
    private Long shopId;
    /**
     * 关联 temu_seller.id，由店铺授权关系确定
     */
    private Long sellerId;
    /**
     * Temu 父订单号
     */
    private String parentOrderSn;
    /**
     * Temu 子订单号
     */
    private String orderSn;
    /**
     * Temu 站点编号
     */
    private Integer siteId;
    /**
     * Temu 区域编号
     */
    private Long regionId;
    /**
     * 父订单状态
     */
    private Integer parentOrderStatus;
    /**
     * 子订单状态
     */
    private Integer orderStatus;
    /**
     * 父订单支付类型
     */
    private String parentOrderPaymentType;
    /**
     * 子订单支付类型
     */
    private String orderPaymentType;
    /**
     * 履约方式
     */
    private String fulfillmentType;
    /**
     * Temu 商品编号
     */
    private Long goodsId;
    /**
     * Temu SKU 编号
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 原始商品名称
     */
    private String originalGoodsName;
    /**
     * 商品规格
     */
    private String spec;
    /**
     * 原始商品规格
     */
    private String originalSpecName;
    /**
     * 商品缩略图
     */
    private String thumbUrl;
    /**
     * 下单数量
     */
    private Integer quantity;
    /**
     * 发货前取消数量
     */
    private Integer canceledQuantityBeforeShipment;
    /**
     * 原始下单数量
     */
    private Integer originalOrderQuantity;
    /**
     * 父订单发货方式
     */
    private Integer shippingMethod;
    /**
     * 是否由主商城合单发货
     */
    private Boolean shipmentConsolidatedByMainMall;
    /**
     * 是否含运费
     */
    private Boolean hasShippingFee;
    /**
     * 父订单创建时间
     */
    private LocalDateTime parentOrderTime;
    /**
     * 父订单确认时间
     */
    private LocalDateTime parentConfirmTime;
    /**
     * 子订单创建时间
     */
    private LocalDateTime orderCreateTime;
    /**
     * 要求发货时间
     */
    private LocalDateTime orderShippingTime;
    /**
     * 最晚预计发货时间
     */
    private LocalDateTime expectShipLatestTime;
    /**
     * 最晚送达时间
     */
    private LocalDateTime latestDeliveryTime;
    /**
     * Temu 订单最后更新时间
     */
    private LocalDateTime temuUpdateTime;
    /**
     * 父订单标签 JSON
     */
    private String parentOrderLabels;
    /**
     * 子订单标签 JSON
     */
    private String orderLabels;
    /**
     * 父订单履约预警 JSON
     */
    private String parentFulfillmentWarnings;
    /**
     * 子订单履约预警 JSON
     */
    private String fulfillmentWarnings;
    /**
     * 包裹异常类型 JSON
     */
    private String packageAbnormalTypes;
    /**
     * 商品映射列表 JSON
     */
    private String productList;
    /**
     * 最近同步时间
     */
    private LocalDateTime lastSyncTime;


}