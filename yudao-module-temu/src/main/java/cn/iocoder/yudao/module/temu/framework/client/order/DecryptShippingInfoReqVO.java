package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** bg.order.decryptshippinginfo.get 请求参数。 */
@Data
public class DecryptShippingInfoReqVO {
    /** Temu 父订单编号。 */
    private String parentOrderSn;
    /** Temu 返回的加密收货信息。 */
    private String encryptedShippingInfo;
}
