package cn.iocoder.yudao.module.temu.dal.mysql.ordershippinginfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.ordershippinginfo.TemuOrderShippingInfoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.ordershippinginfo.vo.*;

/**
 * Temu 父订单收货信息 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface TemuOrderShippingInfoMapper extends BaseMapperX<TemuOrderShippingInfoDO> {

    default PageResult<TemuOrderShippingInfoDO> selectPage(TemuOrderShippingInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuOrderShippingInfoDO>()
                .eqIfPresent(TemuOrderShippingInfoDO::getParentOrderSn, reqVO.getParentOrderSn())
                .betweenIfPresent(TemuOrderShippingInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemuOrderShippingInfoDO::getId));
    }

}