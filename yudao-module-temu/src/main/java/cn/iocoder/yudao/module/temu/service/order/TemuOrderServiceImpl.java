package cn.iocoder.yudao.module.temu.service.order;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.temu.controller.admin.order.vo.TemuOrderPageReqVO;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.temu.dal.mysql.order.TemuOrderMapper;

/**
 * Temu 订单 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class TemuOrderServiceImpl implements TemuOrderService {

    @Resource
    private TemuOrderMapper orderMapper;

    @Override
    public TemuOrderDO getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public PageResult<TemuOrderDO> getOrderPage(TemuOrderPageReqVO pageReqVO) {
        return orderMapper.selectPage(pageReqVO);
    }

}
