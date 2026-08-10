package cn.iocoder.yudao.module.temu.service.shippingcompany;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shippingcompany.TemuShippingCompanyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Temu 区域承运商目录 Service 接口
 *
 * @author 自达源码
 */
public interface TemuShippingCompanyService {

    /**
     * 创建Temu 区域承运商目录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShippingCompany(@Valid TemuShippingCompanySaveReqVO createReqVO);

    /**
     * 更新Temu 区域承运商目录
     *
     * @param updateReqVO 更新信息
     */
    void updateShippingCompany(@Valid TemuShippingCompanySaveReqVO updateReqVO);

    /**
     * 删除Temu 区域承运商目录
     *
     * @param id 编号
     */
    void deleteShippingCompany(Long id);

    /**
    * 批量删除Temu 区域承运商目录
    *
    * @param ids 编号
    */
    void deleteShippingCompanyListByIds(List<Long> ids);

    /**
     * 获得Temu 区域承运商目录
     *
     * @param id 编号
     * @return Temu 区域承运商目录
     */
    TemuShippingCompanyDO getShippingCompany(Long id);

    /**
     * 获得Temu 区域承运商目录分页
     *
     * @param pageReqVO 分页查询
     * @return Temu 区域承运商目录分页
     */
    PageResult<TemuShippingCompanyDO> getShippingCompanyPage(TemuShippingCompanyPageReqVO pageReqVO);

}