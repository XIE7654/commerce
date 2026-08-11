package cn.iocoder.yudao.module.amazon.service.reportrequest;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.amazon.controller.admin.reportrequest.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.reportrequest.AmazonReportRequestDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * Amazon 报表请求及异步处理任务 Service 接口
 *
 * @author 自达源码
 */
public interface AmazonReportRequestService {

    /**
     * 创建Amazon 报表请求及异步处理任务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReportRequest(@Valid AmazonReportRequestSaveReqVO createReqVO);

    /**
     * 更新Amazon 报表请求及异步处理任务
     *
     * @param updateReqVO 更新信息
     */
    void updateReportRequest(@Valid AmazonReportRequestSaveReqVO updateReqVO);

    /**
     * 删除Amazon 报表请求及异步处理任务
     *
     * @param id 编号
     */
    void deleteReportRequest(Long id);

    /**
    * 批量删除Amazon 报表请求及异步处理任务
    *
    * @param ids 编号
    */
    void deleteReportRequestListByIds(List<Long> ids);

    /**
     * 获得Amazon 报表请求及异步处理任务
     *
     * @param id 编号
     * @return Amazon 报表请求及异步处理任务
     */
    AmazonReportRequestDO getReportRequest(Long id);

    /**
     * 获得Amazon 报表请求及异步处理任务分页
     *
     * @param pageReqVO 分页查询
     * @return Amazon 报表请求及异步处理任务分页
     */
    PageResult<AmazonReportRequestDO> getReportRequestPage(AmazonReportRequestPageReqVO pageReqVO);

}