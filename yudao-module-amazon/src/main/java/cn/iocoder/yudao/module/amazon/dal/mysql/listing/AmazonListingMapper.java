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
     * 按店铺和卖家 SKU 查询 Listing，受当前租户数据权限隔离。
     *
     * @param shopId 店铺编号
     * @param sku Amazon Seller SKU
     * @return Listing；不存在时返回 {@code null}
     */
    default AmazonListingDO selectByShopIdAndSku(Long shopId, String sku) {
        return selectOne(AmazonListingDO::getShopId, shopId, AmazonListingDO::getSku, sku);
    }
}
