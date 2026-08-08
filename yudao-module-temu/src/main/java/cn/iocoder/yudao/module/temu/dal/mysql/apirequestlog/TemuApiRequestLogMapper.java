package cn.iocoder.yudao.module.temu.dal.mysql.apirequestlog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.temu.dal.dataobject.apirequestlog.TemuApiRequestLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.temu.controller.admin.apirequestlog.vo.*;

/**
 * Temu OpenAPI 请求调用日志 Mapper
 *
 * @author 自达源码
 */
@Mapper
public interface TemuApiRequestLogMapper extends BaseMapperX<TemuApiRequestLogDO> {

    default PageResult<TemuApiRequestLogDO> selectPage(TemuApiRequestLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemuApiRequestLogDO>()
                .eqIfPresent(TemuApiRequestLogDO::getShopId, reqVO.getShopId())
                .eqIfPresent(TemuApiRequestLogDO::getSite, reqVO.getSite())
                .eqIfPresent(TemuApiRequestLogDO::getApiCategory, reqVO.getApiCategory())
                .likeIfPresent(TemuApiRequestLogDO::getOperationName, reqVO.getOperationName())
                .eqIfPresent(TemuApiRequestLogDO::getRequestMethod, reqVO.getRequestMethod())
                .eqIfPresent(TemuApiRequestLogDO::getRequestUrl, reqVO.getRequestUrl())
                .eqIfPresent(TemuApiRequestLogDO::getRequestPath, reqVO.getRequestPath())
                .betweenIfPresent(TemuApiRequestLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemuApiRequestLogDO::getId));
    }

}