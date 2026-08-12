package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** bg.order.customization.get 返回的定制订单信息。 */
@Data
public class CustomizationOrderListDto {
    /** 定制订单信息列表。 */
    private List<CustomizationOrderDto> orderList;
}
