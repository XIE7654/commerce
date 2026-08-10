package cn.iocoder.yudao.module.temu.dal.mysql.order;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.order.vo.*;

/**
 * Temu 订单 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface TemuOrderMapper extends BaseMapperX<TemuOrderDO> {

    /**
     * 按店铺和子订单号查询本地订单，用于同步时判断是新增还是更新。
     *
     * @param shopId 店铺编号
     * @param orderSn Temu 子订单号
     * @return 已同步的订单；不存在时返回 {@code null}
     */
    default TemuOrderDO selectByShopIdAndOrderSn(Long shopId, String orderSn) {
        return selectOne(TemuOrderDO::getShopId, shopId, TemuOrderDO::getOrderSn, orderSn);
    }

    /**
     * 分页查询订单管理模块的本地同步订单。
     *
     * @param reqVO 订单管理接口的筛选和分页参数
     * @return 符合筛选条件的本地订单分页结果
     */
    default PageResult<TemuOrderDO> selectPage(
            cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.TemuOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuOrderDO>()
                .eqIfPresent(TemuOrderDO::getShopId, reqVO.getShopId())
                .eqIfPresent(TemuOrderDO::getSellerId, reqVO.getSellerId())
                .eqIfPresent(TemuOrderDO::getParentOrderSn, reqVO.getParentOrderSn())
                .eqIfPresent(TemuOrderDO::getOrderSn, reqVO.getOrderSn())
                .eqIfPresent(TemuOrderDO::getParentOrderStatus, reqVO.getParentOrderStatus())
                .eqIfPresent(TemuOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .betweenIfPresent(TemuOrderDO::getOrderCreateTime, reqVO.getOrderCreateTime())
                .orderByDesc(TemuOrderDO::getId));
    }

    /**
     * 分页查询订单模块的订单。
     *
     * @param reqVO 订单模块的筛选和分页参数
     * @return 符合筛选条件的订单分页结果
     */
    default PageResult<TemuOrderDO> selectPage(TemuOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuOrderDO>()
                .eqIfPresent(TemuOrderDO::getShopId, reqVO.getShopId())
                .eqIfPresent(TemuOrderDO::getSellerId, reqVO.getSellerId())
                .eqIfPresent(TemuOrderDO::getParentOrderSn, reqVO.getParentOrderSn())
                .eqIfPresent(TemuOrderDO::getOrderSn, reqVO.getOrderSn())
                .eqIfPresent(TemuOrderDO::getSiteId, reqVO.getSiteId())
                .eqIfPresent(TemuOrderDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(TemuOrderDO::getParentOrderStatus, reqVO.getParentOrderStatus())
                .eqIfPresent(TemuOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .likeIfPresent(TemuOrderDO::getGoodsName, reqVO.getGoodsName())
                .betweenIfPresent(TemuOrderDO::getLastSyncTime, reqVO.getLastSyncTime())
                .betweenIfPresent(TemuOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemuOrderDO::getId));
    }

}
