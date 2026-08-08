package cn.iocoder.yudao.module.temu.service.shop;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.shop.TemuShopMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

/**
 * Temu 店铺 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TemuShopServiceImpl implements TemuShopService {

    @Resource
    private TemuShopMapper shopMapper;

    @Override
    public Long createShop(TemuShopSaveReqVO createReqVO) {
        // 插入
        TemuShopDO shop = BeanUtils.toBean(createReqVO, TemuShopDO.class);
        shopMapper.insert(shop);

        // 返回
        return shop.getId();
    }

    @Override
    public void updateShop(TemuShopSaveReqVO updateReqVO) {
        // 校验存在
        validateShopExists(updateReqVO.getId());
        // 更新
        TemuShopDO updateObj = BeanUtils.toBean(updateReqVO, TemuShopDO.class);
        shopMapper.updateById(updateObj);
    }

    @Override
    public void deleteShop(Long id) {
        // 校验存在
        validateShopExists(id);
        // 删除
        shopMapper.deleteById(id);
    }

    @Override
        public void deleteShopListByIds(List<Long> ids) {
        // 删除
        shopMapper.deleteByIds(ids);
        }


    private void validateShopExists(Long id) {
        if (shopMapper.selectById(id) == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
    }

    @Override
    public TemuShopDO getShop(Long id) {
        return shopMapper.selectById(id);
    }

    @Override
    public PageResult<TemuShopDO> getShopPage(TemuShopPageReqVO pageReqVO) {
        return shopMapper.selectPage(pageReqVO);
    }

}