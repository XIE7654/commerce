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
    /** 父订单地址一级行政区名称。 */
    private String regionName1;
    /** 父订单地址二级行政区名称。 */
    private String regionName2;
    /** 父订单地址三级行政区名称。 */
    private String regionName3;
    private String orderPaymentType;
    private Integer shippingMethod;
    private Boolean isShipmentConsolidatedByMainMall;
    private Boolean hasShippingFee;
    private Long parentOrderTime;
    private Long parentConfirmTime;
    /** 父订单待处理完成时间。 */
    private Long parentOrderPendingFinishTime;
    private Long expectShipLatestTime;
    private Long latestDeliveryTime;
    /** 父订单发货时间。 */
    private Long parentShippingTime;
    private Long updateTime;
    /** 父订单标签。 */
    private List<Object> parentOrderLabel;
    /** 父订单履约预警。 */
    private List<Object> fulfillmentWarning;
    /** 协作仓批次订单号。 */
    private List<String> batchOrderNumberList;
}
