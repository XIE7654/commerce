package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;

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
    Map<String, Object> getOrderItems(AmazonOrderGetReqVO request);

    /**
     * 查询 Amazon 订单商品买家信息。
     *
     * @param request 店铺、站点和 Amazon 订单编号
     * @return Amazon 订单商品买家信息原始响应
     */
    Map<String, Object> getOrderItemsBuyerInfo(AmazonOrderGetReqVO request);

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

}
