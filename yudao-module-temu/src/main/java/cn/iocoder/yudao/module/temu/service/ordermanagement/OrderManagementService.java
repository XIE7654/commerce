package cn.iocoder.yudao.module.temu.service.ordermanagement;

import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementCustomOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementParentOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementShippingCompaniesReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Temu Order Management 订单管理业务 Service。
 */
public interface OrderManagementService {

    /**
     * 查询 Temu 订单列表。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应
     */
    JsonNode getOrderList(OrderManagementOrderListReqVO request);

    /**
     * 查询 Temu 父订单详情。
     *
     * @param request 父订单查询参数
     * @return Temu 官方订单详情响应
     */
    JsonNode getOrderDetail(OrderManagementParentOrderReqVO request);

    /**
     * 查询 Temu 子订单的定制信息。
     *
     * @param request 子订单编号列表查询参数
     * @return Temu 官方定制订单详情响应
     */
    JsonNode getCustomOrderDetail(OrderManagementCustomOrderReqVO request);

    /**
     * 查询 Temu 父订单的收货信息。
     *
     * @param request 父订单查询参数
     * @return Temu 官方收货信息响应
     */
    JsonNode getOrderShippingInfo(OrderManagementParentOrderReqVO request);

    /**
     * 查询 Temu 指定区域可用的承运商。
     *
     * @param request 区域查询参数
     * @return Temu 官方承运商列表响应
     */
    JsonNode getOrderShippingCompanies(OrderManagementShippingCompaniesReqVO request);
}
