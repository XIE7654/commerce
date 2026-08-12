package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** Temu 子订单信息。 */
@Data
public class ChildOrderDto {
    private String orderSn;
    private Integer orderStatus;
    private String orderPaymentType;
    private String fulfillmentType;
    private Long goodsId;
    private Long skuId;
    private String goodsName;
    private String originalGoodsName;
    private String spec;
    private String originalSpecName;
    private String thumbUrl;
    private Integer quantity;
    private Integer canceledQuantityBeforeShipment;
    private Integer originalOrderQuantity;
    private Long orderCreateTime;
    private Long orderShippingTime;
    /** 子订单标签。 */
    private List<Object> orderLabel;
    /** 子订单履约预警。 */
    private List<Object> fulfillmentWarning;
    /** 包裹异常类型。 */
    private List<Object> packageAbnormalTypeList;
    /** 商品明细。 */
    private List<Object> productList;
}
