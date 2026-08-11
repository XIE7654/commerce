package cn.iocoder.yudao.module.amazon.service.reportrequest;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.amazon.controller.admin.reportrequest.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.reportrequest.AmazonReportRequestDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.amazon.dal.mysql.reportrequest.AmazonReportRequestMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.amazon.enums.ErrorCodeConstants.*;

/**
 * Amazon 报表请求及异步处理任务 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
public class AmazonReportRequestServiceImpl implements AmazonReportRequestService {

    @Resource
    private AmazonReportRequestMapper reportRequestMapper;

    @Override
    public Long createReportRequest(AmazonReportRequestSaveReqVO createReqVO) {
        // 插入
        AmazonReportRequestDO reportRequest = BeanUtils.toBean(createReqVO, AmazonReportRequestDO.class);
        reportRequestMapper.insert(reportRequest);

        // 返回
        return reportRequest.getId();
    }

    @Override
    public void updateReportRequest(AmazonReportRequestSaveReqVO updateReqVO) {
        // 校验存在
        validateReportRequestExists(updateReqVO.getId());
        // 更新
        AmazonReportRequestDO updateObj = BeanUtils.toBean(updateReqVO, AmazonReportRequestDO.class);
        reportRequestMapper.updateById(updateObj);
    }

    @Override
    public void deleteReportRequest(Long id) {
        // 校验存在
        validateReportRequestExists(id);
        // 删除
        reportRequestMapper.deleteById(id);
    }

    @Override
        public void deleteReportRequestListByIds(List<Long> ids) {
        // 删除
        reportRequestMapper.deleteByIds(ids);
        }


    private void validateReportRequestExists(Long id) {
        if (reportRequestMapper.selectById(id) == null) {
            throw exception(REPORT_REQUEST_NOT_EXISTS);
        }
    }

    @Override
    public AmazonReportRequestDO getReportRequest(Long id) {
        return reportRequestMapper.selectById(id);
    }

    @Override
    public PageResult<AmazonReportRequestDO> getReportRequestPage(AmazonReportRequestPageReqVO pageReqVO) {
        return reportRequestMapper.selectPage(pageReqVO);
    }

}