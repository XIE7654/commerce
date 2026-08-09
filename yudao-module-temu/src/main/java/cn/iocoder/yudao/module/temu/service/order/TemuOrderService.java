package cn.iocoder.yudao.module.temu.service.order;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.temu.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Temu 订单 Service 接口
 *
 * @author 自达源码
 */
public interface TemuOrderService {

    /**
     * 创建Temu 订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrder(@Valid TemuOrderSaveReqVO createReqVO);

    /**
     * 更新Temu 订单
     *
     * @param updateReqVO 更新信息
     */
    void updateOrder(@Valid TemuOrderSaveReqVO updateReqVO);

    /**
     * 删除Temu 订单
     *
     * @param id 编号
     */
    void deleteOrder(Long id);

    /**
    * 批量删除Temu 订单
    *
    * @param ids 编号
    */
    void deleteOrderListByIds(List<Long> ids);

    /**
     * 获得Temu 订单
     *
     * @param id 编号
     * @return Temu 订单
     */
    TemuOrderDO getOrder(Long id);

    /**
     * 获得Temu 订单分页
     *
     * @param pageReqVO 分页查询
     * @return Temu 订单分页
     */
    PageResult<TemuOrderDO> getOrderPage(TemuOrderPageReqVO pageReqVO);

}