package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

import java.util.List;

/** bg.order.customization.get 请求参数。 */
@Data
public class CustomizationOrderReqVO {
    /** 需要查询定制信息的子订单编号列表。 */
    private List<String> orderSnList;
}
