package cn.iocoder.yudao.module.amazon.dal.mysql.listing;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listing.AmazonListingDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Amazon Listing 主表 Mapper。
 */
@Mapper
public interface AmazonListingMapper extends BaseMapperX<AmazonListingDO> {

    /**
     * 按店铺、卖家 SKU 和站点查询 Listing，受当前租户数据权限隔离。
     *
     * @param shopId 店铺编号
     * @param sku Amazon Seller SKU
     * @param marketplaceId Amazon Marketplace ID
     * @return Listing；不存在时返回 {@code null}
     */
    default AmazonListingDO selectByShopIdAndSkuAndMarketplaceId(Long shopId, String sku, String marketplaceId) {
        return selectOne(AmazonListingDO::getShopId, shopId, AmazonListingDO::getSku, sku,
                AmazonListingDO::getMarketplaceId, marketplaceId);
    }
}
