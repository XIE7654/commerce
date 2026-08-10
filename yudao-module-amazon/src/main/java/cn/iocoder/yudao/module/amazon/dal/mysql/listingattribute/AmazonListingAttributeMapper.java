package cn.iocoder.yudao.module.amazon.dal.mysql.listingattribute;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingattribute.AmazonListingAttributeDO;
import org.apache.ibatis.annotations.Mapper;

/** Amazon Listing 属性 Mapper。 */
@Mapper
public interface AmazonListingAttributeMapper extends BaseMapperX<AmazonListingAttributeDO> {

    /**
     * 按站点记录和属性名称查询，用于同步时幂等写入。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param attributeName Amazon 属性名称
     * @return 属性记录；不存在时返回 {@code null}
     */
    default AmazonListingAttributeDO selectByListingMarketplaceIdAndAttributeName(Long listingMarketplaceId,
                                                                                    String attributeName) {
        return selectOne(AmazonListingAttributeDO::getListingMarketplaceId, listingMarketplaceId,
                AmazonListingAttributeDO::getAttributeName, attributeName);
    }
}
