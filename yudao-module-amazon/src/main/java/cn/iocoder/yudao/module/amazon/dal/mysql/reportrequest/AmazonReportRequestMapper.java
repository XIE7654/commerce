package cn.iocoder.yudao.module.amazon.dal.mysql.reportrequest;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.amazon.dal.dataobject.reportrequest.AmazonReportRequestDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.amazon.controller.admin.reportrequest.vo.*;

/**
 * Amazon 报表请求及异步处理任务 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface AmazonReportRequestMapper extends BaseMapperX<AmazonReportRequestDO> {

    default PageResult<AmazonReportRequestDO> selectPage(AmazonReportRequestPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AmazonReportRequestDO>()
                .eqIfPresent(AmazonReportRequestDO::getShopId, reqVO.getShopId())
                .eqIfPresent(AmazonReportRequestDO::getCountryCode, reqVO.getCountryCode())
                .eqIfPresent(AmazonReportRequestDO::getReportType, reqVO.getReportType())
                .betweenIfPresent(AmazonReportRequestDO::getDataStartTime, reqVO.getDataStartTime())
                .eqIfPresent(AmazonReportRequestDO::getAmazonProcessingStatus, reqVO.getAmazonProcessingStatus())
                .eqIfPresent(AmazonReportRequestDO::getTaskStatus, reqVO.getTaskStatus())
                .betweenIfPresent(AmazonReportRequestDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AmazonReportRequestDO::getId));
    }

}