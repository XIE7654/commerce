package cn.iocoder.yudao.module.amazon.dal.mysql.seller;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonSellerAccountDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Amazon 卖家账户 Mapper。
 */
@Mapper
public interface AmazonSellerAccountMapper extends BaseMapperX<AmazonSellerAccountDO> {

    /**
     * 查询当前租户下指定店铺的账户档案。
     *
     * @param shopId 店铺编号
     * @return 账户档案，不存在时返回 {@code null}
     */
    default AmazonSellerAccountDO selectByShopId(Long shopId) {
        return selectOne(AmazonSellerAccountDO::getShopId, shopId);
    }
}
