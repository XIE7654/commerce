package cn.iocoder.yudao.module.temu.controller.admin.apirequestlog;

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

import cn.iocoder.yudao.module.temu.controller.admin.apirequestlog.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.apirequestlog.TemuApiRequestLogDO;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;

@Tag(name = "管理后台 - Temu OpenAPI 请求调用日志")
@RestController
@RequestMapping("/temu/api-request-log")
@Validated
public class TemuApiRequestLogController {

    @Resource
    private TemuApiRequestLogService apiRequestLogService;

    @PostMapping("/create")
    @Operation(summary = "创建Temu OpenAPI 请求调用日志")
    @PreAuthorize("@ss.hasPermission('temu:api-request-log:create')")
    public CommonResult<Long> createApiRequestLog(@Valid @RequestBody TemuApiRequestLogSaveReqVO createReqVO) {
        return success(apiRequestLogService.createApiRequestLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Temu OpenAPI 请求调用日志")
    @PreAuthorize("@ss.hasPermission('temu:api-request-log:update')")
    public CommonResult<Boolean> updateApiRequestLog(@Valid @RequestBody TemuApiRequestLogSaveReqVO updateReqVO) {
        apiRequestLogService.updateApiRequestLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Temu OpenAPI 请求调用日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('temu:api-request-log:delete')")
    public CommonResult<Boolean> deleteApiRequestLog(@RequestParam("id") Long id) {
        apiRequestLogService.deleteApiRequestLog(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Temu OpenAPI 请求调用日志")
                @PreAuthorize("@ss.hasPermission('temu:api-request-log:delete')")
    public CommonResult<Boolean> deleteApiRequestLogList(@RequestParam("ids") List<Long> ids) {
        apiRequestLogService.deleteApiRequestLogListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Temu OpenAPI 请求调用日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('temu:api-request-log:query')")
    public CommonResult<TemuApiRequestLogRespVO> getApiRequestLog(@RequestParam("id") Long id) {
        TemuApiRequestLogDO apiRequestLog = apiRequestLogService.getApiRequestLog(id);
        return success(BeanUtils.toBean(apiRequestLog, TemuApiRequestLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Temu OpenAPI 请求调用日志分页")
    @PreAuthorize("@ss.hasPermission('temu:api-request-log:query')")
    public CommonResult<PageResult<TemuApiRequestLogRespVO>> getApiRequestLogPage(@Valid TemuApiRequestLogPageReqVO pageReqVO) {
        PageResult<TemuApiRequestLogDO> pageResult = apiRequestLogService.getApiRequestLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TemuApiRequestLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Temu OpenAPI 请求调用日志 Excel")
    @PreAuthorize("@ss.hasPermission('temu:api-request-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportApiRequestLogExcel(@Valid TemuApiRequestLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemuApiRequestLogDO> list = apiRequestLogService.getApiRequestLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Temu OpenAPI 请求调用日志.xls", "数据", TemuApiRequestLogRespVO.class,
                        BeanUtils.toBean(list, TemuApiRequestLogRespVO.class));
    }

}