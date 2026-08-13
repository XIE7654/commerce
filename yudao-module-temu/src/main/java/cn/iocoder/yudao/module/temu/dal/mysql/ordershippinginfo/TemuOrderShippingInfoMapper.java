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

    /**
     * 按店铺和父订单号查询收货信息，用于同步时幂等写入。
     *
     * @param shopId 店铺编号
     * @param parentOrderSn Temu 父订单号
     * @return 已保存的收货信息；不存在时返回 {@code null}
     */
    default TemuOrderShippingInfoDO selectByShopIdAndParentOrderSn(Long shopId, String parentOrderSn) {
        return selectOne(TemuOrderShippingInfoDO::getShopId, shopId,
                TemuOrderShippingInfoDO::getParentOrderSn, parentOrderSn);
    }

    default PageResult<TemuOrderShippingInfoDO> selectPage(TemuOrderShippingInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuOrderShippingInfoDO>()
                .eqIfPresent(TemuOrderShippingInfoDO::getParentOrderSn, reqVO.getParentOrderSn())
                .betweenIfPresent(TemuOrderShippingInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemuOrderShippingInfoDO::getId));
    }

}
