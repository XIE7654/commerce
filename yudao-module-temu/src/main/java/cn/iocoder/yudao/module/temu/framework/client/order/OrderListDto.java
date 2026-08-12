package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** bg.order.list.v2.get 返回的分页订单结果。 */
@Data
public class OrderListDto {
    /** 当前页父订单及子订单聚合数据。 */
    private List<OrderPageItemDto> pageItems;
    /** 总记录数。 */
    private Long total;
    /** 总页数。 */
    private Integer totalPage;
}
