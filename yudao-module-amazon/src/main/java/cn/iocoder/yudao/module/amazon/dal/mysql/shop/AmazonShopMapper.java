package cn.iocoder.yudao.module.amazon.dal.mysql.shop;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * Amazon 店铺 Mapper。
 */
@Mapper
public interface AmazonShopMapper extends BaseMapperX<AmazonShopDO> {

    /** 按租户隔离查询 sellerId 对应的店铺。 */
    default AmazonShopDO selectBySellerId(String sellerId) {
        return selectOne(new LambdaQueryWrapperX<AmazonShopDO>().eq(AmazonShopDO::getSellerId, sellerId));
    }
}
