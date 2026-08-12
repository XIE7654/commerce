package cn.iocoder.yudao.module.amazon.dal.mysql.listingmarketplace;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Amazon Listing 站点信息 Mapper。
 */
@Mapper
public interface AmazonListingMarketplaceMapper extends BaseMapperX<AmazonListingMarketplaceDO> {

    /**
     * 批量查询 Listing 已同步的站点信息。
     *
     * @param listingIds Listing 编号集合
     * @return 当前租户下的站点信息列表
     */
    default List<AmazonListingMarketplaceDO> selectListByListingIds(Collection<Long> listingIds) {
        return selectList(AmazonListingMarketplaceDO::getListingId, listingIds);
    }
}
