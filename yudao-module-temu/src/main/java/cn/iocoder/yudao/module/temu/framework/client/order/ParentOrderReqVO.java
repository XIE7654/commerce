package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** 按父订单号查询 Temu 订单信息的请求参数。 */
@Data
public class ParentOrderReqVO {
    /** Temu 父订单编号。 */
    private String parentOrderSn;
}
