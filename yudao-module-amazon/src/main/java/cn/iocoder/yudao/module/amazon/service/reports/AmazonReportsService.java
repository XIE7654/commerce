package cn.iocoder.yudao.module.amazon.service.reports;

import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportScheduleCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportScheduleIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportSchedulesListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportsListReqVO;

import java.util.Map;

/**
 * Amazon Reports 服务。
 */
public interface AmazonReportsService {

    /**
     * 创建 Amazon 异步报表任务。
     *
     * @param request 店铺、站点和报表生成条件
     * @return Amazon 创建报表任务响应
     */
    Map<String, Object> createReport(AmazonReportCreateReqVO request);

    /**
     * 查询 Amazon 报表任务列表。
     *
     * @param request 店铺、站点和任务筛选条件
     * @return Amazon 报表任务列表响应
     */
    Map<String, Object> getReports(AmazonReportsListReqVO request);

    /**
     * 查询单个 Amazon 报表任务状态与文件标识。
     *
     * @param request 店铺、站点和报表任务编号
     * @return Amazon 报表任务响应
     */
    Map<String, Object> getReport(AmazonReportIdReqVO request);

    /**
     * 取消尚未开始处理的 Amazon 报表任务。
     *
     * @param request 店铺、站点和报表任务编号
     */
    void cancelReport(AmazonReportIdReqVO request);

    /**
     * 查询 Amazon 报表文件下载元数据。
     *
     * @param request 店铺、站点和报表文件编号
     * @return 包含短时预签名下载地址及压缩算法的响应
     */
    Map<String, Object> getReportDocument(AmazonReportIdReqVO request);

    /**
     * 查询 Amazon 报表计划列表。
     *
     * @param request 店铺、站点和报表类型筛选条件
     * @return Amazon 报表计划列表响应
     */
    Map<String, Object> getReportSchedules(AmazonReportSchedulesListReqVO request);

    /**
     * 创建 Amazon 周期性报表计划。
     *
     * @param request 店铺、站点和计划生成条件
     * @return Amazon 创建报表计划响应
     */
    Map<String, Object> createReportSchedule(AmazonReportScheduleCreateReqVO request);

    /**
     * 查询单个 Amazon 报表计划。
     *
     * @param request 店铺、站点和报表计划编号
     * @return Amazon 报表计划响应
     */
    Map<String, Object> getReportSchedule(AmazonReportScheduleIdReqVO request);

    /**
     * 取消 Amazon 周期性报表计划。
     *
     * @param request 店铺、站点和报表计划编号
     */
    void cancelReportSchedule(AmazonReportScheduleIdReqVO request);
}
