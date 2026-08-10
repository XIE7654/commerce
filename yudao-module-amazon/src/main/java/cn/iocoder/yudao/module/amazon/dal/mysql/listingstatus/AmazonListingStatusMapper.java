package cn.iocoder.yudao.module.amazon.dal.mysql.listingstatus;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingstatus.AmazonListingStatusDO;
import org.apache.ibatis.annotations.Mapper;

/** Amazon Listing 状态 Mapper。 */
@Mapper
public interface AmazonListingStatusMapper extends BaseMapperX<AmazonListingStatusDO> {

    /**
     * 按站点记录和状态查询，用于同步时幂等写入。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param status Amazon Listing 状态
     * @return 状态记录；不存在时返回 {@code null}
     */
    default AmazonListingStatusDO selectByListingMarketplaceIdAndStatus(Long listingMarketplaceId, String status) {
        return selectOne(AmazonListingStatusDO::getListingMarketplaceId, listingMarketplaceId,
                AmazonListingStatusDO::getStatus, status);
    }
}
