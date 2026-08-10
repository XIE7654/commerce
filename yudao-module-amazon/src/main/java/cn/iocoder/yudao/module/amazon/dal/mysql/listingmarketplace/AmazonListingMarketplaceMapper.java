package cn.iocoder.yudao.module.amazon.dal.mysql.listingmarketplace;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo.*;

/**
 * Listing信息表 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface AmazonListingMarketplaceMapper extends BaseMapperX<AmazonListingMarketplaceDO> {

    default PageResult<AmazonListingMarketplaceDO> selectPage(AmazonListingMarketplacePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AmazonListingMarketplaceDO>()
                .eqIfPresent(AmazonListingMarketplaceDO::getListingId, reqVO.getListingId())
                .eqIfPresent(AmazonListingMarketplaceDO::getMarketplaceId, reqVO.getMarketplaceId())
                .eqIfPresent(AmazonListingMarketplaceDO::getAsin, reqVO.getAsin())
                .eqIfPresent(AmazonListingMarketplaceDO::getProductType, reqVO.getProductType())
                .eqIfPresent(AmazonListingMarketplaceDO::getConditionType, reqVO.getConditionType())
                .likeIfPresent(AmazonListingMarketplaceDO::getItemName, reqVO.getItemName())
                .betweenIfPresent(AmazonListingMarketplaceDO::getAmazonCreatedTime, reqVO.getAmazonCreatedTime())
                .betweenIfPresent(AmazonListingMarketplaceDO::getAmazonUpdatedTime, reqVO.getAmazonUpdatedTime())
                .betweenIfPresent(AmazonListingMarketplaceDO::getLastSyncTime, reqVO.getLastSyncTime())
                .betweenIfPresent(AmazonListingMarketplaceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AmazonListingMarketplaceDO::getId));
    }

}