package cn.iocoder.yudao.module.temu.dal.mysql.seller;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.seller.vo.*;

/**
 * Temu 卖家商城授权信息 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface TemuSellerMapper extends BaseMapperX<TemuSellerDO> {

    /**
     * 查询店铺当前的卖家授权记录。
     *
     * @param shopId 本地 Temu 店铺编号
     * @return 卖家授权记录；不存在时返回 null
     */
    default TemuSellerDO selectByShopId(Long shopId) {
        return selectById(shopId);
    }

    default PageResult<TemuSellerDO> selectPage(TemuSellerPageReqVO reqVO) {
        LambdaQueryWrapperX<TemuSellerDO> query = new LambdaQueryWrapperX<TemuSellerDO>()
                .eqIfPresent(TemuSellerDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(TemuSellerDO::getMallId, reqVO.getMallId())
                .eqIfPresent(TemuSellerDO::getAppSubscribeStatus, reqVO.getAppSubscribeStatus())
                .betweenIfPresent(TemuSellerDO::getExpiredTime, reqVO.getExpiredTime())
                .eqIfPresent(TemuSellerDO::getExpiredAt, reqVO.getExpiredAt())
                .eqIfPresent(TemuSellerDO::getApiScopeList, reqVO.getApiScopeList())
                .eqIfPresent(TemuSellerDO::getResponseJson, reqVO.getResponseJson())
                .betweenIfPresent(TemuSellerDO::getLastSyncTime, reqVO.getLastSyncTime())
                .betweenIfPresent(TemuSellerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemuSellerDO::getId);
        return selectPage(reqVO, query);
    }

}
