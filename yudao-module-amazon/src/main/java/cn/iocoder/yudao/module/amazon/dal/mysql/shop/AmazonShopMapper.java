package cn.iocoder.yudao.module.amazon.dal.mysql.shop;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.amazon.controller.admin.shop.vo.*;

/**
 * Amazon店铺授权 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface AmazonShopMapper extends BaseMapperX<AmazonShopDO> {

    /**
     * 查询当前租户下所有启用的 Amazon 店铺。
     *
     * @return 启用的店铺列表
     */
    default List<AmazonShopDO> selectEnabledList() {
        return selectList(new LambdaQueryWrapperX<AmazonShopDO>()
                .eq(AmazonShopDO::getStatus, 0)
                .orderByAsc(AmazonShopDO::getId));
    }

    default PageResult<AmazonShopDO> selectPage(AmazonShopPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AmazonShopDO>()
                .likeIfPresent(AmazonShopDO::getShopName, reqVO.getShopName())
                .eqIfPresent(AmazonShopDO::getRegion, reqVO.getRegion())
                .eqIfPresent(AmazonShopDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AmazonShopDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AmazonShopDO::getId));
    }

    /** 按租户隔离查询 sellerId 对应的店铺。 */
    default AmazonShopDO selectBySellerId(String sellerId) {
        return selectOne(new LambdaQueryWrapperX<AmazonShopDO>().eq(AmazonShopDO::getSellerId, sellerId));
    }
}
