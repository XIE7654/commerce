package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** bg.order.decryptshippinginfo.get 返回的解密收货信息。 */
@Data
public class DecryptShippingInfoDto {
    private String receiverName;
    private String receiverPhone;
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    private String postalCode;
}
