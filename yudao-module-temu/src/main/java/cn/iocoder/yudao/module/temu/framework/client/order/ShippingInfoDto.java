package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** Temu 订单收货信息。 */
@Data
public class ShippingInfoDto {
    private String parentOrderSn;
    private String receiverName;
    private String receiverPhone;
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    private String postalCode;
}
