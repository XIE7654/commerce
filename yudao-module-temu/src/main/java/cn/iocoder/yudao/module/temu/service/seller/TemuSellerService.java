package cn.iocoder.yudao.module.temu.service.seller;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.temu.controller.admin.seller.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Temu 卖家商城授权信息 Service 接口
 *
 * @author 自达源码
 */
public interface TemuSellerService {

    /**
     * 创建Temu 卖家商城授权信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSeller(@Valid TemuSellerSaveReqVO createReqVO);

    /**
     * 更新Temu 卖家商城授权信息
     *
     * @param updateReqVO 更新信息
     */
    void updateSeller(@Valid TemuSellerSaveReqVO updateReqVO);

    /**
     * 删除Temu 卖家商城授权信息
     *
     * @param id 编号
     */
    void deleteSeller(Long id);

    /**
    * 批量删除Temu 卖家商城授权信息
    *
    * @param ids 编号
    */
    void deleteSellerListByIds(List<Long> ids);

    /**
     * 获得Temu 卖家商城授权信息
     *
     * @param id 编号
     * @return Temu 卖家商城授权信息
     */
    TemuSellerDO getSeller(Long id);

    /**
     * 获得Temu 卖家商城授权信息分页
     *
     * @param pageReqVO 分页查询
     * @return Temu 卖家商城授权信息分页
     */
    PageResult<TemuSellerDO> getSellerPage(TemuSellerPageReqVO pageReqVO);

}