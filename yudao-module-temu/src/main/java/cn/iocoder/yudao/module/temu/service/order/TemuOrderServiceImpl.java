package cn.iocoder.yudao.module.temu.service.order;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.order.TemuOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

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
    public Long createOrder(TemuOrderSaveReqVO createReqVO) {
        // 插入
        TemuOrderDO order = BeanUtils.toBean(createReqVO, TemuOrderDO.class);
        orderMapper.insert(order);

        // 返回
        return order.getId();
    }

    @Override
    public void updateOrder(TemuOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderExists(updateReqVO.getId());
        // 更新
        TemuOrderDO updateObj = BeanUtils.toBean(updateReqVO, TemuOrderDO.class);
        orderMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrder(Long id) {
        // 校验存在
        validateOrderExists(id);
        // 删除
        orderMapper.deleteById(id);
    }

    @Override
        public void deleteOrderListByIds(List<Long> ids) {
        // 删除
        orderMapper.deleteByIds(ids);
        }


    private void validateOrderExists(Long id) {
        if (orderMapper.selectById(id) == null) {
            throw exception(ORDER_NOT_EXISTS);
        }
    }

    @Override
    public TemuOrderDO getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public PageResult<TemuOrderDO> getOrderPage(TemuOrderPageReqVO pageReqVO) {
        return orderMapper.selectPage(pageReqVO);
    }

}