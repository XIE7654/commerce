package cn.iocoder.yudao.module.temu.service.order;

import cn.iocoder.yudao.module.temu.controller.admin.order.vo.TemuOrderPageReqVO;
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
