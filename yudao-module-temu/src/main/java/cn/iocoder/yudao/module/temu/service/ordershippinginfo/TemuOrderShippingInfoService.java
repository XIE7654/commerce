package cn.iocoder.yudao.module.temu.service.ordershippinginfo;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.temu.controller.admin.ordershippinginfo.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.ordershippinginfo.TemuOrderShippingInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Temu 父订单收货信息 Service 接口
 *
 * @author 自达源码
 */
public interface TemuOrderShippingInfoService {

    /**
     * 创建Temu 父订单收货信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrderShippingInfo(@Valid TemuOrderShippingInfoSaveReqVO createReqVO);

    /**
     * 更新Temu 父订单收货信息
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderShippingInfo(@Valid TemuOrderShippingInfoSaveReqVO updateReqVO);

    /**
     * 删除Temu 父订单收货信息
     *
     * @param id 编号
     */
    void deleteOrderShippingInfo(Long id);

    /**
    * 批量删除Temu 父订单收货信息
    *
    * @param ids 编号
    */
    void deleteOrderShippingInfoListByIds(List<Long> ids);

    /**
     * 获得Temu 父订单收货信息
     *
     * @param id 编号
     * @return Temu 父订单收货信息
     */
    TemuOrderShippingInfoDO getOrderShippingInfo(Long id);

    /**
     * 获得Temu 父订单收货信息分页
     *
     * @param pageReqVO 分页查询
     * @return Temu 父订单收货信息分页
     */
    PageResult<TemuOrderShippingInfoDO> getOrderShippingInfoPage(TemuOrderShippingInfoPageReqVO pageReqVO);

}