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

    /**
     * 按 Listing 和 Marketplace 查询站点信息，用于同步时幂等更新。
     *
     * @param listingId Listing 主表编号
     * @param marketplaceId Amazon Marketplace ID
     * @return 站点信息；不存在时返回 {@code null}
     */
    default AmazonListingMarketplaceDO selectByListingIdAndMarketplaceId(Long listingId, String marketplaceId) {
        return selectOne(AmazonListingMarketplaceDO::getListingId, listingId,
                AmazonListingMarketplaceDO::getMarketplaceId, marketplaceId);
    }

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
