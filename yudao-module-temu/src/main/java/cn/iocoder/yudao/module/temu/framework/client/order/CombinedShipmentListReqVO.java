package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** bg.order.combinedshipment.list.get 请求参数。 */
@Data
public class CombinedShipmentListReqVO {
    /** 订单所属区域编号。 */
    private Long regionId;
    /** 页码，从 1 开始。 */
    private Integer pageNumber;
    /** 每页记录数。 */
    private Integer pageSize;
}
