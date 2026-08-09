package cn.iocoder.yudao.module.temu.dal.mysql.shippingcompany;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.shippingcompany.TemuShippingCompanyDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo.*;

/**
 * Temu 区域承运商目录 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface TemuShippingCompanyMapper extends BaseMapperX<TemuShippingCompanyDO> {

    default PageResult<TemuShippingCompanyDO> selectPage(TemuShippingCompanyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuShippingCompanyDO>()
                .eqIfPresent(TemuShippingCompanyDO::getSite, reqVO.getSite())
                .eqIfPresent(TemuShippingCompanyDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(TemuShippingCompanyDO::getLogisticsServiceProviderId, reqVO.getLogisticsServiceProviderId())
                .likeIfPresent(TemuShippingCompanyDO::getLogisticsServiceProviderName, reqVO.getLogisticsServiceProviderName())
                .likeIfPresent(TemuShippingCompanyDO::getLogisticsBrandName, reqVO.getLogisticsBrandName())
                .orderByDesc(TemuShippingCompanyDO::getId));
    }

}