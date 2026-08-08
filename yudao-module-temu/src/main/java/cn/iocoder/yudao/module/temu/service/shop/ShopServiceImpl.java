package cn.iocoder.yudao.module.temu.service.shop;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.ShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.shop.ShopMapper;

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
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    /**
     * 创建 Temu 店铺并保存授权有效期。
     *
     * @param createReqVO 店铺与授权信息
     * @return 新建店铺编号
     */
    @Override
    public Long createShop(ShopSaveReqVO createReqVO) {
        validateAuthorizationPeriod(createReqVO);
        // 插入
        ShopDO shop = BeanUtils.toBean(createReqVO, ShopDO.class);
        shopMapper.insert(shop);

        // 返回
        return shop.getId();
    }

    /**
     * 更新 Temu 店铺及其授权有效期。
     *
     * @param updateReqVO 店铺与授权信息
     */
    @Override
    public void updateShop(ShopSaveReqVO updateReqVO) {
        // 校验存在
        validateShopExists(updateReqVO.getId());
        validateAuthorizationPeriod(updateReqVO);
        // 更新
        ShopDO updateObj = BeanUtils.toBean(updateReqVO, ShopDO.class);
        shopMapper.updateById(updateObj);
    }

    /**
     * 校验授权时间区间，防止保存无效的授权生命周期。
     *
     * @param request 店铺授权信息
     * @throws IllegalArgumentException 授权过期时间早于授权时间时抛出
     */
    private void validateAuthorizationPeriod(ShopSaveReqVO request) {
        if (request.getAuthorizeTime() != null && request.getAuthorizeExpireTime() != null
                && request.getAuthorizeExpireTime().isBefore(request.getAuthorizeTime())) {
            throw new IllegalArgumentException("授权过期时间不能早于授权时间");
        }
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
    public ShopDO getShop(Long id) {
        return shopMapper.selectById(id);
    }

    @Override
    public PageResult<ShopDO> getShopPage(ShopPageReqVO pageReqVO) {
        return shopMapper.selectPage(pageReqVO);
    }

}
