package cn.iocoder.yudao.module.amazon.controller.admin.reportrequest;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.amazon.controller.admin.reportrequest.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.reportrequest.AmazonReportRequestDO;
import cn.iocoder.yudao.module.amazon.service.reportrequest.AmazonReportRequestService;

@Tag(name = "管理后台 - Amazon 报表请求及异步处理任务")
@RestController
@RequestMapping("/amazon/report-request")
@Validated
public class AmazonReportRequestController {

    @Resource
    private AmazonReportRequestService reportRequestService;

    @PostMapping("/create")
    @Operation(summary = "创建Amazon 报表请求及异步处理任务")
    @PreAuthorize("@ss.hasPermission('amazon:report-request:create')")
    public CommonResult<Long> createReportRequest(@Valid @RequestBody AmazonReportRequestSaveReqVO createReqVO) {
        return success(reportRequestService.createReportRequest(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Amazon 报表请求及异步处理任务")
    @PreAuthorize("@ss.hasPermission('amazon:report-request:update')")
    public CommonResult<Boolean> updateReportRequest(@Valid @RequestBody AmazonReportRequestSaveReqVO updateReqVO) {
        reportRequestService.updateReportRequest(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Amazon 报表请求及异步处理任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('amazon:report-request:delete')")
    public CommonResult<Boolean> deleteReportRequest(@RequestParam("id") Long id) {
        reportRequestService.deleteReportRequest(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Amazon 报表请求及异步处理任务")
                @PreAuthorize("@ss.hasPermission('amazon:report-request:delete')")
    public CommonResult<Boolean> deleteReportRequestList(@RequestParam("ids") List<Long> ids) {
        reportRequestService.deleteReportRequestListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Amazon 报表请求及异步处理任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('amazon:report-request:query')")
    public CommonResult<AmazonReportRequestRespVO> getReportRequest(@RequestParam("id") Long id) {
        AmazonReportRequestDO reportRequest = reportRequestService.getReportRequest(id);
        return success(BeanUtils.toBean(reportRequest, AmazonReportRequestRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Amazon 报表请求及异步处理任务分页")
    @PreAuthorize("@ss.hasPermission('amazon:report-request:query')")
    public CommonResult<PageResult<AmazonReportRequestRespVO>> getReportRequestPage(@Valid AmazonReportRequestPageReqVO pageReqVO) {
        PageResult<AmazonReportRequestDO> pageResult = reportRequestService.getReportRequestPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AmazonReportRequestRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Amazon 报表请求及异步处理任务 Excel")
    @PreAuthorize("@ss.hasPermission('amazon:report-request:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReportRequestExcel(@Valid AmazonReportRequestPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AmazonReportRequestDO> list = reportRequestService.getReportRequestPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Amazon 报表请求及异步处理任务.xls", "数据", AmazonReportRequestRespVO.class,
                        BeanUtils.toBean(list, AmazonReportRequestRespVO.class));
    }

}