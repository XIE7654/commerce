package cn.iocoder.yudao.module.temu.service.shippingcompany;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shippingcompany.TemuShippingCompanyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.temu.dal.mysql.shippingcompany.TemuShippingCompanyMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

/**
 * Temu 区域承运商目录 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class TemuShippingCompanyServiceImpl implements TemuShippingCompanyService {

    @Resource
    private TemuShippingCompanyMapper shippingCompanyMapper;

    @Override
    public Long createShippingCompany(TemuShippingCompanySaveReqVO createReqVO) {
        // 插入
        TemuShippingCompanyDO shippingCompany = BeanUtils.toBean(createReqVO, TemuShippingCompanyDO.class);
        shippingCompanyMapper.insert(shippingCompany);

        // 返回
        return shippingCompany.getId();
    }

    @Override
    public void updateShippingCompany(TemuShippingCompanySaveReqVO updateReqVO) {
        // 校验存在
        validateShippingCompanyExists(updateReqVO.getId());
        // 更新
        TemuShippingCompanyDO updateObj = BeanUtils.toBean(updateReqVO, TemuShippingCompanyDO.class);
        shippingCompanyMapper.updateById(updateObj);
    }

    @Override
    public void deleteShippingCompany(Long id) {
        // 校验存在
        validateShippingCompanyExists(id);
        // 删除
        shippingCompanyMapper.deleteById(id);
    }

    @Override
        public void deleteShippingCompanyListByIds(List<Long> ids) {
        // 删除
        shippingCompanyMapper.deleteByIds(ids);
        }


    private void validateShippingCompanyExists(Long id) {
        if (shippingCompanyMapper.selectById(id) == null) {
            throw exception(SHIPPING_COMPANY_NOT_EXISTS);
        }
    }

    @Override
    public TemuShippingCompanyDO getShippingCompany(Long id) {
        return shippingCompanyMapper.selectById(id);
    }

    @Override
    public PageResult<TemuShippingCompanyDO> getShippingCompanyPage(TemuShippingCompanyPageReqVO pageReqVO) {
        return shippingCompanyMapper.selectPage(pageReqVO);
    }

}