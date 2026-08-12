package cn.iocoder.yudao.module.temu.framework.client.order;

import lombok.Data;

/** Temu 合单发货订单信息。 */
@Data
public class CombinedShipmentDto {
    private String parentOrderSn;
    private String shipmentId;
    private Integer shipmentStatus;
}
