package cn.iocoder.yudao.module.temu.controller.admin.shippingcompany;

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

import cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shippingcompany.TemuShippingCompanyDO;
import cn.iocoder.yudao.module.temu.service.shippingcompany.TemuShippingCompanyService;

@Tag(name = "管理后台 - Temu 区域承运商目录")
@RestController
@RequestMapping("/temu/shipping-company")
@Validated
public class TemuShippingCompanyController {

    @Resource
    private TemuShippingCompanyService shippingCompanyService;

    @PostMapping("/create")
    @Operation(summary = "创建Temu 区域承运商目录")
    @PreAuthorize("@ss.hasPermission('temu:shipping-company:create')")
    public CommonResult<Long> createShippingCompany(@Valid @RequestBody TemuShippingCompanySaveReqVO createReqVO) {
        return success(shippingCompanyService.createShippingCompany(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Temu 区域承运商目录")
    @PreAuthorize("@ss.hasPermission('temu:shipping-company:update')")
    public CommonResult<Boolean> updateShippingCompany(@Valid @RequestBody TemuShippingCompanySaveReqVO updateReqVO) {
        shippingCompanyService.updateShippingCompany(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Temu 区域承运商目录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('temu:shipping-company:delete')")
    public CommonResult<Boolean> deleteShippingCompany(@RequestParam("id") Long id) {
        shippingCompanyService.deleteShippingCompany(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Temu 区域承运商目录")
                @PreAuthorize("@ss.hasPermission('temu:shipping-company:delete')")
    public CommonResult<Boolean> deleteShippingCompanyList(@RequestParam("ids") List<Long> ids) {
        shippingCompanyService.deleteShippingCompanyListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Temu 区域承运商目录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('temu:shipping-company:query')")
    public CommonResult<TemuShippingCompanyRespVO> getShippingCompany(@RequestParam("id") Long id) {
        TemuShippingCompanyDO shippingCompany = shippingCompanyService.getShippingCompany(id);
        return success(BeanUtils.toBean(shippingCompany, TemuShippingCompanyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Temu 区域承运商目录分页")
    @PreAuthorize("@ss.hasPermission('temu:shipping-company:query')")
    public CommonResult<PageResult<TemuShippingCompanyRespVO>> getShippingCompanyPage(@Valid TemuShippingCompanyPageReqVO pageReqVO) {
        PageResult<TemuShippingCompanyDO> pageResult = shippingCompanyService.getShippingCompanyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TemuShippingCompanyRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Temu 区域承运商目录 Excel")
    @PreAuthorize("@ss.hasPermission('temu:shipping-company:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingCompanyExcel(@Valid TemuShippingCompanyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemuShippingCompanyDO> list = shippingCompanyService.getShippingCompanyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Temu 区域承运商目录.xls", "数据", TemuShippingCompanyRespVO.class,
                        BeanUtils.toBean(list, TemuShippingCompanyRespVO.class));
    }

}