package cn.iocoder.yudao.module.temu.service.ordershippinginfo;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.ordershippinginfo.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.ordershippinginfo.TemuOrderShippingInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.ordershippinginfo.TemuOrderShippingInfoMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

/**
 * Temu 父订单收货信息 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class TemuOrderShippingInfoServiceImpl implements TemuOrderShippingInfoService {

    @Resource
    private TemuOrderShippingInfoMapper orderShippingInfoMapper;

    @Override
    public Long createOrderShippingInfo(TemuOrderShippingInfoSaveReqVO createReqVO) {
        // 插入
        TemuOrderShippingInfoDO orderShippingInfo = BeanUtils.toBean(createReqVO, TemuOrderShippingInfoDO.class);
        orderShippingInfoMapper.insert(orderShippingInfo);

        // 返回
        return orderShippingInfo.getId();
    }

    @Override
    public void updateOrderShippingInfo(TemuOrderShippingInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderShippingInfoExists(updateReqVO.getId());
        // 更新
        TemuOrderShippingInfoDO updateObj = BeanUtils.toBean(updateReqVO, TemuOrderShippingInfoDO.class);
        orderShippingInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderShippingInfo(Long id) {
        // 校验存在
        validateOrderShippingInfoExists(id);
        // 删除
        orderShippingInfoMapper.deleteById(id);
    }

    @Override
        public void deleteOrderShippingInfoListByIds(List<Long> ids) {
        // 删除
        orderShippingInfoMapper.deleteByIds(ids);
        }


    private void validateOrderShippingInfoExists(Long id) {
        if (orderShippingInfoMapper.selectById(id) == null) {
            throw exception(ORDER_SHIPPING_INFO_NOT_EXISTS);
        }
    }

    @Override
    public TemuOrderShippingInfoDO getOrderShippingInfo(Long id) {
        return orderShippingInfoMapper.selectById(id);
    }

    @Override
    public PageResult<TemuOrderShippingInfoDO> getOrderShippingInfoPage(TemuOrderShippingInfoPageReqVO pageReqVO) {
        return orderShippingInfoMapper.selectPage(pageReqVO);
    }

}