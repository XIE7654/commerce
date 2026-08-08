package cn.iocoder.yudao.module.temu.service.orderfulfillment;

import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentConfirmReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentInfoReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentQueryReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Temu Order Fulfillment 订单履约业务 Service。
 */
public interface OrderFulfillmentService {

    /**
     * 确认 Temu 订单发货。
     *
     * @param request 发货方式及包裹明细
     * @return Temu 官方发货确认响应
     */
    JsonNode confirmShipment(OrderFulfillmentShipmentConfirmReqVO request);

    /**
     * 查询 Temu 指定父订单和子订单的发货信息。
     *
     * @param request 父订单与子订单查询参数
     * @return Temu 官方发货信息响应
     */
    JsonNode getShipmentInfo(OrderFulfillmentShipmentInfoReqVO request);

    /**
     * 查询 Temu 指定包裹的物流信息。
     *
     * @param request 包裹、物流公司和运单查询参数
     * @return Temu 官方包裹物流信息响应
     */
    JsonNode getShipment(OrderFulfillmentShipmentQueryReqVO request);
}
