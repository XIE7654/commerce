package cn.iocoder.yudao.module.amazon.dal.mysql.seller;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceParticipationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Amazon 店铺 Marketplace 参与状态 Mapper。
 */
@Mapper
public interface AmazonShopMarketplaceParticipationMapper extends BaseMapperX<AmazonShopMarketplaceParticipationDO> {

    /**
     * 查询当前租户下店铺指定站点的参与状态。
     *
     * @param shopId 店铺编号
     * @param marketplaceId Marketplace ID
     * @return 参与状态，不存在时返回 {@code null}
     */
    default AmazonShopMarketplaceParticipationDO selectByShopIdAndMarketplaceId(Long shopId, String marketplaceId) {
        return selectOne(AmazonShopMarketplaceParticipationDO::getShopId, shopId,
                AmazonShopMarketplaceParticipationDO::getMarketplaceId, marketplaceId);
    }
}
