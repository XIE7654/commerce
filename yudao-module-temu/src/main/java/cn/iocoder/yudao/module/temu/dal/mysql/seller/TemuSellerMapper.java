package cn.iocoder.yudao.module.temu.dal.mysql.seller;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.seller.vo.*;

/**
 * Temu 卖家商城授权信息 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface TemuSellerMapper extends BaseMapperX<TemuSellerDO> {

    default PageResult<TemuSellerDO> selectPage(TemuSellerPageReqVO reqVO) {
        MPJLambdaWrapperX<TemuSellerDO> query = new MPJLambdaWrapperX<TemuSellerDO>()
                .selectAll(TemuSellerDO.class)
                .selectAs(TemuShopDO::getShopName, TemuSellerDO::getShopName)
                .leftJoin(TemuShopDO.class, TemuShopDO::getId, TemuSellerDO::getShopId)
                .eqIfPresent(TemuSellerDO::getShopId, reqVO.getShopId())
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
        return selectJoinPage(reqVO, TemuSellerDO.class, query);
    }

}
