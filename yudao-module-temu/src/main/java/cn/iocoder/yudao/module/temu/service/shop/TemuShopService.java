package cn.iocoder.yudao.module.temu.service.shop;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Temu 店铺 Service 接口
 *
 * @author 芋道源码
 */
public interface TemuShopService {

    /**
     * 创建Temu 店铺
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShop(@Valid TemuShopSaveReqVO createReqVO);

    /**
     * 更新Temu 店铺
     *
     * @param updateReqVO 更新信息
     */
    void updateShop(@Valid TemuShopSaveReqVO updateReqVO);

    /**
     * 删除Temu 店铺
     *
     * @param id 编号
     */
    void deleteShop(Long id);

    /**
    * 批量删除Temu 店铺
    *
    * @param ids 编号
    */
    void deleteShopListByIds(List<Long> ids);

    /**
     * 获得Temu 店铺
     *
     * @param id 编号
     * @return Temu 店铺
     */
    TemuShopDO getShop(Long id);

    /**
     * 获得Temu 店铺分页
     *
     * @param pageReqVO 分页查询
     * @return Temu 店铺分页
     */
    PageResult<TemuShopDO> getShopPage(TemuShopPageReqVO pageReqVO);

}