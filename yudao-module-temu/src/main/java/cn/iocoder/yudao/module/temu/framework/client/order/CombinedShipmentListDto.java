package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** bg.order.combinedshipment.list.get 返回的合单发货订单列表。 */
@Data
public class CombinedShipmentListDto {
    /** 合单发货订单列表。 */
    private List<CombinedShipmentDto> pageItems;
    /** 总记录数。 */
    private Long total;
}
