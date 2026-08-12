package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** bg.order.detail.v2.get 返回的父订单详情。 */
@Data
public class OrderDetailDto {
    /** 父订单信息。 */
    private ParentOrderDto parentOrderMap;
    /** 子订单列表。 */
    private List<ChildOrderDto> orderList;
}
