package cn.iocoder.yudao.module.amazon.dal.dataobject.order;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Amazon 订单 DO。
 *
 * <p>支付明细、收货地址和买家信息的字段结构会随 Amazon API 演进，统一保留为 JSON 字符串。</p>
 */
@TableName("amazon_order")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonOrderDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 关联的 Amazon 店铺编号。 */
    private Long shopId;
    /** Amazon 订单编号。 */
    private String amazonOrderId;
    /** Amazon Marketplace ID。 */
    private String marketplaceId;
    /** 买家下单时间。 */
    private LocalDateTime purchaseDate;
    /** Amazon 最后更新时间。 */
    private LocalDateTime lastUpdateDate;
    /** 订单状态。 */
    private String orderStatus;
    /** 履约渠道。 */
    private String fulfillmentChannel;
    /** 已发货商品数量。 */
    private Integer numberOfItemsShipped;
    /** 未发货商品数量。 */
    private Integer numberOfItemsUnshipped;
    /** 支付方式。 */
    private String paymentMethod;
    /** 配送服务级别分类。 */
    private String shipmentServiceLevelCategory;
    /** 订单类型。 */
    private String orderType;
    /** 最早发货时间。 */
    private LocalDateTime earliestShipDate;
    /** 最晚发货时间。 */
    private LocalDateTime latestShipDate;
    /** 是否为企业订单。 */
    private Boolean isBusinessOrder;
    /** 是否为 Prime 订单。 */
    private Boolean isPrime;
    /** 是否为自提点订单。 */
    private Boolean isAccessPointOrder;
    /** 是否启用全球快递。 */
    private Boolean isGlobalExpressEnabled;
    /** 是否为高级配送订单。 */
    private Boolean isPremiumOrder;
    /** 是否由 Amazon Business 销售。 */
    private Boolean isSoldByAB;
    /** 是否为 IBA 订单。 */
    private Boolean isIBA;
    /** 支付方式明细数组 JSON。 */
    private String paymentMethodDetails;
    /** 收货地址对象 JSON。 */
    private String shippingAddress;
    /** 买家信息对象 JSON。 */
    private String buyerInfo;
    /** 最近同步时间。 */
    private LocalDateTime lastSyncTime;
}
