package cn.iocoder.yudao.module.amazon.service.listingmarketplace;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.amazon.dal.mysql.listingmarketplace.AmazonListingMarketplaceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.amazon.enums.ErrorCodeConstants.*;

/**
 * Listing信息表 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class AmazonListingMarketplaceServiceImpl implements AmazonListingMarketplaceService {

    @Resource
    private AmazonListingMarketplaceMapper listingMarketplaceMapper;

    @Override
    public Long createListingMarketplace(AmazonListingMarketplaceSaveReqVO createReqVO) {
        // 插入
        AmazonListingMarketplaceDO listingMarketplace = BeanUtils.toBean(createReqVO, AmazonListingMarketplaceDO.class);
        listingMarketplaceMapper.insert(listingMarketplace);

        // 返回
        return listingMarketplace.getId();
    }

    @Override
    public void updateListingMarketplace(AmazonListingMarketplaceSaveReqVO updateReqVO) {
        // 校验存在
        validateListingMarketplaceExists(updateReqVO.getId());
        // 更新
        AmazonListingMarketplaceDO updateObj = BeanUtils.toBean(updateReqVO, AmazonListingMarketplaceDO.class);
        listingMarketplaceMapper.updateById(updateObj);
    }

    @Override
    public void deleteListingMarketplace(Long id) {
        // 校验存在
        validateListingMarketplaceExists(id);
        // 删除
        listingMarketplaceMapper.deleteById(id);
    }

    @Override
        public void deleteListingMarketplaceListByIds(List<Long> ids) {
        // 删除
        listingMarketplaceMapper.deleteByIds(ids);
        }


    private void validateListingMarketplaceExists(Long id) {
        if (listingMarketplaceMapper.selectById(id) == null) {
            throw exception(LISTING_MARKETPLACE_NOT_EXISTS);
        }
    }

    @Override
    public AmazonListingMarketplaceDO getListingMarketplace(Long id) {
        return listingMarketplaceMapper.selectById(id);
    }

    @Override
    public PageResult<AmazonListingMarketplaceDO> getListingMarketplacePage(AmazonListingMarketplacePageReqVO pageReqVO) {
        return listingMarketplaceMapper.selectPage(pageReqVO);
    }

}