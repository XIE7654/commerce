package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** bg.order.list.v2.get 请求参数。 */
@Data
public class OrderListReqVO {
    /** 父订单状态。 */
    private Integer parentOrderStatus;
    /** 订单所属区域编号。 */
    private Long regionId;
    /** 页码，从 1 开始。 */
    private Integer pageNumber;
    /** 每页记录数。 */
    private Integer pageSize;
    /** 仅返回该时间戳之后更新的订单。 */
    private Long updateTime;
}
