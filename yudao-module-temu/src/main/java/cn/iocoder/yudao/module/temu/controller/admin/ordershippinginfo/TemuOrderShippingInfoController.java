package cn.iocoder.yudao.module.temu.controller.admin.ordershippinginfo;

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

import cn.iocoder.yudao.module.temu.controller.admin.ordershippinginfo.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.ordershippinginfo.TemuOrderShippingInfoDO;
import cn.iocoder.yudao.module.temu.service.ordershippinginfo.TemuOrderShippingInfoService;

@Tag(name = "管理后台 - Temu 父订单收货信息")
@RestController
@RequestMapping("/temu/order-shipping-info")
@Validated
public class TemuOrderShippingInfoController {

    @Resource
    private TemuOrderShippingInfoService orderShippingInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建Temu 父订单收货信息")
    @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:create')")
    public CommonResult<Long> createOrderShippingInfo(@Valid @RequestBody TemuOrderShippingInfoSaveReqVO createReqVO) {
        return success(orderShippingInfoService.createOrderShippingInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Temu 父订单收货信息")
    @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:update')")
    public CommonResult<Boolean> updateOrderShippingInfo(@Valid @RequestBody TemuOrderShippingInfoSaveReqVO updateReqVO) {
        orderShippingInfoService.updateOrderShippingInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Temu 父订单收货信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:delete')")
    public CommonResult<Boolean> deleteOrderShippingInfo(@RequestParam("id") Long id) {
        orderShippingInfoService.deleteOrderShippingInfo(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Temu 父订单收货信息")
                @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:delete')")
    public CommonResult<Boolean> deleteOrderShippingInfoList(@RequestParam("ids") List<Long> ids) {
        orderShippingInfoService.deleteOrderShippingInfoListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Temu 父订单收货信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:query')")
    public CommonResult<TemuOrderShippingInfoRespVO> getOrderShippingInfo(@RequestParam("id") Long id) {
        TemuOrderShippingInfoDO orderShippingInfo = orderShippingInfoService.getOrderShippingInfo(id);
        return success(BeanUtils.toBean(orderShippingInfo, TemuOrderShippingInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Temu 父订单收货信息分页")
    @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:query')")
    public CommonResult<PageResult<TemuOrderShippingInfoRespVO>> getOrderShippingInfoPage(@Valid TemuOrderShippingInfoPageReqVO pageReqVO) {
        PageResult<TemuOrderShippingInfoDO> pageResult = orderShippingInfoService.getOrderShippingInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TemuOrderShippingInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Temu 父订单收货信息 Excel")
    @PreAuthorize("@ss.hasPermission('temu:order-shipping-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderShippingInfoExcel(@Valid TemuOrderShippingInfoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemuOrderShippingInfoDO> list = orderShippingInfoService.getOrderShippingInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Temu 父订单收货信息.xls", "数据", TemuOrderShippingInfoRespVO.class,
                        BeanUtils.toBean(list, TemuOrderShippingInfoRespVO.class));
    }

}