package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** Temu 订单分页中的父子订单聚合项。 */
@Data
public class OrderPageItemDto {
    /** 父订单信息。 */
    private ParentOrderDto parentOrderMap;
    /** 子订单列表。 */
    private List<ChildOrderDto> orderList;
}
