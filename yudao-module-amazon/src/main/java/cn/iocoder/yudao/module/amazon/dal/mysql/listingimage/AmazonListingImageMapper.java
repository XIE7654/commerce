package cn.iocoder.yudao.module.amazon.dal.mysql.listingimage;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingimage.AmazonListingImageDO;
import org.apache.ibatis.annotations.Mapper;

/** Amazon Listing 图片 Mapper。 */
@Mapper
public interface AmazonListingImageMapper extends BaseMapperX<AmazonListingImageDO> {

    /**
     * 按站点记录、图片类型和排序号查询，用于同步时幂等写入。
     *
     * @param listingMarketplaceId Listing 站点记录编号
     * @param imageType 图片类型
     * @param sortOrder 同类型排序号
     * @return 图片记录；不存在时返回 {@code null}
     */
    default AmazonListingImageDO selectByListingMarketplaceIdAndTypeAndSortOrder(Long listingMarketplaceId,
                                                                                   String imageType, Integer sortOrder) {
        return selectOne(AmazonListingImageDO::getListingMarketplaceId, listingMarketplaceId,
                AmazonListingImageDO::getImageType, imageType,
                AmazonListingImageDO::getSortOrder, sortOrder);
    }
}
