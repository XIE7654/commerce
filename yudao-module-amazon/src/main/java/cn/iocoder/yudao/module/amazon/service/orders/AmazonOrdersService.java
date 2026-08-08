package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentConfirmationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderRegulatedInfoUpdateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrder2026GetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrders2026ListReqVO;

import java.util.Map;

/**
 * Amazon Orders 服务。
 */
public interface AmazonOrdersService {

    /**
     * 查询 Amazon 订单列表。
     *
     * @param request 店铺、站点和订单筛选条件
     * @return Amazon 订单列表原始响应
     */
    Map<String, Object> getOrders(AmazonOrdersListReqVO request);

    /**
     * 查询 Amazon 订单详情。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 订单详情原始响应
     */
    Map<String, Object> getOrder(AmazonOrderGetReqVO request);

    /**
     * 查询 Amazon 订单商品。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 订单商品原始响应
     */
    Map<String, Object> getOrderItems(AmazonOrderItemsReqVO request);

    /**
     * 查询 Amazon 订单商品买家信息。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 订单商品买家信息原始响应
     */
    Map<String, Object> getOrderItemsBuyerInfo(AmazonOrderItemsReqVO request);

    /**
     * 查询 Amazon 订单买家信息。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 订单买家信息原始响应
     */
    Map<String, Object> getOrderBuyerInfo(AmazonOrderGetReqVO request);

    /**
     * 查询 Amazon 订单收货地址。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 订单收货地址原始响应
     */
    Map<String, Object> getOrderAddress(AmazonOrderGetReqVO request);

    /**
     * 查询 Amazon 受监管订单信息。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 受监管订单信息原始响应
     */
    Map<String, Object> getOrderRegulatedInfo(AmazonOrderGetReqVO request);

    /**
     * 更新 Easy Ship 订单的发货状态。
     *
     * @param request 店铺、站点、订单和发货状态
     * @return Amazon 原始响应
     */
    Map<String, Object> updateShipmentStatus(AmazonOrderShipmentReqVO request);

    /**
     * 确认卖家自配送订单已发货。
     *
     * @param request 店铺、站点、订单和包裹明细
     * @return Amazon 原始响应
     */
    Map<String, Object> confirmShipment(AmazonOrderShipmentConfirmationReqVO request);

    /**
     * 更新受监管订单的验证状态。
     *
     * @param request 店铺、站点、订单和验证状态
     * @return Amazon 原始响应
     */
    Map<String, Object> updateOrderRegulatedInfo(AmazonOrderRegulatedInfoUpdateReqVO request);

    /**
     * 查询 Orders 2026-01-01 版本的订单列表。
     *
     * @param request 店铺、站点和新版筛选条件
     * @return Amazon 原始响应
     */
    Map<String, Object> getOrders2026(AmazonOrders2026ListReqVO request);

    /**
     * 查询 Orders 2026-01-01 版本的单个订单。
     *
     * @param request 店铺、站点、订单编号和返回数据集
     * @return Amazon 原始响应
     */
    Map<String, Object> getOrder2026(AmazonOrder2026GetReqVO request);

}
