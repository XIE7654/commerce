package cn.iocoder.yudao.module.temu.dal.mysql.shop;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.ShopDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;

/**
 * Temu 店铺 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ShopMapper extends BaseMapperX<ShopDO> {

    default PageResult<ShopDO> selectPage(ShopPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShopDO>()
                .eqIfPresent(ShopDO::getShopType, reqVO.getShopType())
                .eqIfPresent(ShopDO::getSite, reqVO.getSite())
                .likeIfPresent(ShopDO::getShopName, reqVO.getShopName())
                .eqIfPresent(ShopDO::getAuthToken, reqVO.getAuthToken())
                .betweenIfPresent(ShopDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShopDO::getId));
    }

}