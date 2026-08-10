package cn.iocoder.yudao.module.temu.service.ordermanagement;

import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementCustomOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementParentOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementShippingCompaniesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.TemuOrderPageReqVO;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
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
     * 拉取 Temu 订单列表并同步当前页数据到本地订单表。
     *
     * @param request 订单状态、区域和分页同步参数
     * @return Temu 官方订单列表响应
     */
    JsonNode syncOrderList(OrderManagementOrderListReqVO request);

    /**
     * 同步全部可用 Temu 店铺的订单。
     *
     * <p>服务会为每个可用店铺投递同步消息，由消费者读取本地最后一次 Temu 更新时间并循环拉取所有分页数据。</p>
     */
    void syncAllAvailableShopOrders();

    /**
     * 同步指定店铺的 Temu 订单。
     *
     * <p>该方法由 RabbitMQ 消费者调用，异常会交由消息队列重试和死信处理。</p>
     *
     * @param shopId Temu 店铺编号
     */
    void syncShopOrders(Long shopId);

    /**
     * 分页查询已同步到本地的 Temu 子订单。
     *
     * @param request 店铺、卖家及订单筛选条件
     * @return 本地订单分页结果
     */
    PageResult<TemuOrderDO> getLocalOrderPage(TemuOrderPageReqVO request);

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
