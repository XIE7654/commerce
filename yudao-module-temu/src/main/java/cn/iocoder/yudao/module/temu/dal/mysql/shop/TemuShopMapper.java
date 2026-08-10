package cn.iocoder.yudao.module.temu.dal.mysql.shop;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;

/**
 * Temu 店铺 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface TemuShopMapper extends BaseMapperX<TemuShopDO> {

    default PageResult<TemuShopDO> selectPage(TemuShopPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuShopDO>()
                .eqIfPresent(TemuShopDO::getShopType, reqVO.getShopType())
                .eqIfPresent(TemuShopDO::getSite, reqVO.getSite())
                .likeIfPresent(TemuShopDO::getShopName, reqVO.getShopName())
                .eqIfPresent(TemuShopDO::getAuthToken, reqVO.getAuthToken())
                .eqIfPresent(TemuShopDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(TemuShopDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemuShopDO::getId));
    }

    default List<TemuShopDO> selectListByStatus(Integer status) {
        return selectList(TemuShopDO::getStatus, status);
    }

}
