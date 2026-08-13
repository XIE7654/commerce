package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import com.amazon.SellingPartnerAPIAA.LWAException;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.models.orders.v0.GetOrderItemsResponse;
import software.amazon.spapi.models.orders.v0.GetOrderResponse;
import software.amazon.spapi.models.orders.v0.GetOrdersResponse;

/** Amazon Orders v0 只读服务。 */
public interface AmazonOrdersService {
    /**
     * 查询订单列表。
     *
     * @param request 查询参数
     * @return Amazon 订单列表响应
     */
    GetOrdersResponse getOrders(AmazonOrdersListReqVO request) throws ApiException, LWAException;
    /**
     * 查询订单详情。
     *
     * @param request 查询参数
     * @return Amazon 订单详情响应
     */
    GetOrderResponse getOrder(AmazonOrderGetReqVO request) throws ApiException, LWAException;
    /**
     * 查询订单商品。
     *
     * @param request 查询参数
     * @return Amazon 订单商品响应
     */
    GetOrderItemsResponse getOrderItems(AmazonOrderItemsReqVO request) throws ApiException, LWAException;

    /**
     * 同步全部启用店铺的订单，并逐页写入本地订单表。
     *
     * @return 成功同步的订单数量
     */
    int syncAllOrders();
}
