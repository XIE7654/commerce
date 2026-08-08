package cn.iocoder.yudao.module.amazon.controller.admin.reports;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportScheduleCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportScheduleIdReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportSchedulesListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.reports.vo.AmazonReportsListReqVO;
import cn.iocoder.yudao.module.amazon.service.reports.AmazonReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Amazon Reports 管理接口。
 */
@Tag(name = "管理后台 - Amazon Reports")
@RestController
@RequestMapping("/amazon/reports")
@Validated
public class AmazonReportsController {

    @Resource
    private AmazonReportsService amazonReportsService;

    /**
     * 创建 Amazon 异步报表任务。
     *
     * @param request 店铺、站点和报表生成条件
     * @return Amazon 创建报表任务响应
     */
    @PostMapping("/create")
    @Operation(summary = "创建 Amazon 报表任务")
    @PreAuthorize("@ss.hasPermission('amazon:reports:create')")
    public CommonResult<Map<String, Object>> createReport(@Valid @RequestBody AmazonReportCreateReqVO request) {
        return CommonResult.success(amazonReportsService.createReport(request));
    }

    /**
     * 查询 Amazon 报表任务列表。
     *
     * @param request 店铺、站点和任务筛选条件
     * @return Amazon 报表任务列表响应
     */
    @PostMapping("/list")
    @Operation(summary = "查询 Amazon 报表任务列表")
    @PreAuthorize("@ss.hasPermission('amazon:reports:query')")
    public CommonResult<Map<String, Object>> getReports(@Valid @RequestBody AmazonReportsListReqVO request) {
        return CommonResult.success(amazonReportsService.getReports(request));
    }

    /**
     * 查询单个 Amazon 报表任务。
     *
     * @param request 店铺、站点和报表任务编号
     * @return Amazon 报表任务响应
     */
    @PostMapping("/detail")
    @Operation(summary = "查询 Amazon 报表任务详情")
    @PreAuthorize("@ss.hasPermission('amazon:reports:query')")
    public CommonResult<Map<String, Object>> getReport(@Valid @RequestBody AmazonReportIdReqVO request) {
        return CommonResult.success(amazonReportsService.getReport(request));
    }

    /**
     * 取消尚未开始处理的 Amazon 报表任务。
     *
     * @param request 店铺、站点和报表任务编号
     * @return 取消请求是否已被 Amazon 接收
     */
    @PostMapping("/cancel")
    @Operation(summary = "取消 Amazon 报表任务")
    @PreAuthorize("@ss.hasPermission('amazon:reports:update')")
    public CommonResult<Boolean> cancelReport(@Valid @RequestBody AmazonReportIdReqVO request) {
        amazonReportsService.cancelReport(request);
        return CommonResult.success(true);
    }

    /**
     * 查询 Amazon 报表文件的短时下载信息。
     *
     * @param request 店铺、站点和报表文件编号
     * @return Amazon 报表文件元数据
     */
    @PostMapping("/document")
    @Operation(summary = "查询 Amazon 报表文件")
    @PreAuthorize("@ss.hasPermission('amazon:reports:query')")
    public CommonResult<Map<String, Object>> getReportDocument(@Valid @RequestBody AmazonReportIdReqVO request) {
        return CommonResult.success(amazonReportsService.getReportDocument(request));
    }

    /**
     * 查询 Amazon 报表计划列表。
     *
     * @param request 店铺、站点和报表类型筛选条件
     * @return Amazon 报表计划列表响应
     */
    @PostMapping("/schedule/list")
    @Operation(summary = "查询 Amazon 报表计划列表")
    @PreAuthorize("@ss.hasPermission('amazon:reports:query')")
    public CommonResult<Map<String, Object>> getReportSchedules(@Valid @RequestBody AmazonReportSchedulesListReqVO request) {
        return CommonResult.success(amazonReportsService.getReportSchedules(request));
    }

    /**
     * 创建 Amazon 周期性报表计划。
     *
     * @param request 店铺、站点和计划生成条件
     * @return Amazon 创建报表计划响应
     */
    @PostMapping("/schedule/create")
    @Operation(summary = "创建 Amazon 报表计划")
    @PreAuthorize("@ss.hasPermission('amazon:reports:create')")
    public CommonResult<Map<String, Object>> createReportSchedule(@Valid @RequestBody AmazonReportScheduleCreateReqVO request) {
        return CommonResult.success(amazonReportsService.createReportSchedule(request));
    }

    /**
     * 查询单个 Amazon 报表计划。
     *
     * @param request 店铺、站点和报表计划编号
     * @return Amazon 报表计划响应
     */
    @PostMapping("/schedule/detail")
    @Operation(summary = "查询 Amazon 报表计划详情")
    @PreAuthorize("@ss.hasPermission('amazon:reports:query')")
    public CommonResult<Map<String, Object>> getReportSchedule(@Valid @RequestBody AmazonReportScheduleIdReqVO request) {
        return CommonResult.success(amazonReportsService.getReportSchedule(request));
    }

    /**
     * 取消 Amazon 周期性报表计划。
     *
     * @param request 店铺、站点和报表计划编号
     * @return 取消请求是否已被 Amazon 接收
     */
    @PostMapping("/schedule/cancel")
    @Operation(summary = "取消 Amazon 报表计划")
    @PreAuthorize("@ss.hasPermission('amazon:reports:update')")
    public CommonResult<Boolean> cancelReportSchedule(@Valid @RequestBody AmazonReportScheduleIdReqVO request) {
        amazonReportsService.cancelReportSchedule(request);
        return CommonResult.success(true);
    }
}
