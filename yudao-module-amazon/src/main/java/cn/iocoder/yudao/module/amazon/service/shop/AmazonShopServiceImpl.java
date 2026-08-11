package cn.iocoder.yudao.module.amazon.service.shop;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.sellers.AmazonSellersService;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.amazon.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.amazon.enums.ErrorCodeConstants.*;

/**
 * Amazon店铺授权 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class AmazonShopServiceImpl implements AmazonShopService {

    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellersService amazonSellersService;

    /**
     * 创建店铺后同步 Amazon Sellers 返回的账户档案和站点参与状态。
     *
     * @param createReqVO 创建信息
     * @return 新建店铺编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createShop(AmazonShopSaveReqVO createReqVO) {
        AmazonShopDO shop = BeanUtils.toBean(createReqVO, AmazonShopDO.class);
        amazonShopMapper.insert(shop);

        // 店铺主键生成后才能使用其授权信息调用 Sellers API，并建立两张从属同步记录。
        AmazonSellersReqVO sellersReqVO = new AmazonSellersReqVO();
        sellersReqVO.setShopId(shop.getId());
        amazonSellersService.syncAccount(sellersReqVO);
        return shop.getId();
    }

    @Override
    public void updateShop(AmazonShopSaveReqVO updateReqVO) {
        // 校验存在
        validateShopExists(updateReqVO.getId());
        // 更新
        AmazonShopDO updateObj = BeanUtils.toBean(updateReqVO, AmazonShopDO.class);
        amazonShopMapper.updateById(updateObj);
    }

    @Override
    public void deleteShop(Long id) {
        // 校验存在
        validateShopExists(id);
        // 删除
        amazonShopMapper.deleteById(id);
    }

    @Override
        public void deleteShopListByIds(List<Long> ids) {
        // 删除
        amazonShopMapper.deleteByIds(ids);
        }


    private void validateShopExists(Long id) {
        if (amazonShopMapper.selectById(id) == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
    }

    @Override
    public AmazonShopDO getShop(Long id) {
        return amazonShopMapper.selectById(id);
    }

    @Override
    public PageResult<AmazonShopDO> getShopPage(AmazonShopPageReqVO pageReqVO) {
        return amazonShopMapper.selectPage(pageReqVO);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** {@inheritDoc} */
    @Override
    public AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

}
