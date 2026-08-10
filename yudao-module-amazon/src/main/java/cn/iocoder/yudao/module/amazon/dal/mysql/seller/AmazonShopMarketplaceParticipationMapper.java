package cn.iocoder.yudao.module.amazon.dal.mysql.seller;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Amazon 店铺 Marketplace 参与状态 Mapper。
 */
@Mapper
public interface AmazonShopMarketplaceParticipationMapper extends BaseMapperX<AmazonShopMarketplaceDO> {

    /**
     * 查询店铺当前参与销售的全部 Marketplace。
     *
     * @param shopId 店铺编号
     * @return 可同步的 Marketplace 参与状态列表
     */
    default List<AmazonShopMarketplaceDO> selectParticipatingByShopId(Long shopId) {
        return selectList(AmazonShopMarketplaceDO::getShopId, shopId,
                AmazonShopMarketplaceDO::getIsParticipating, true);
    }

    /**
     * 查询当前租户下店铺指定站点的参与状态。
     *
     * @param shopId 店铺编号
     * @param marketplaceId Marketplace ID
     * @return 参与状态，不存在时返回 {@code null}
     */
    default AmazonShopMarketplaceDO selectByShopIdAndMarketplaceId(Long shopId, String marketplaceId) {
        return selectOne(AmazonShopMarketplaceDO::getShopId, shopId,
                AmazonShopMarketplaceDO::getMarketplaceId, marketplaceId);
    }
}
