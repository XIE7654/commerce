package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** Temu 父订单信息。 */
@Data
public class ParentOrderDto {
    private String parentOrderSn;
    private Integer siteId;
    private Long regionId;
    private Integer parentOrderStatus;
    private String orderPaymentType;
    private Integer shippingMethod;
    private Boolean isShipmentConsolidatedByMainMall;
    private Boolean hasShippingFee;
    private Long parentOrderTime;
    private Long parentConfirmTime;
    private Long expectShipLatestTime;
    private Long latestDeliveryTime;
    private Long updateTime;
    /** 父订单标签。 */
    private List<Object> parentOrderLabel;
    /** 父订单履约预警。 */
    private List<Object> fulfillmentWarning;
}
