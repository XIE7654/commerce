package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** Temu 子订单定制信息。 */
@Data
public class CustomizationOrderDto {
    /** 子订单编号。 */
    private String orderSn;
    /** 定制内容。 */
    private String customizationContent;
}
