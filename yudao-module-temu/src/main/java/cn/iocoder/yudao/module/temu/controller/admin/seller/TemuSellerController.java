package cn.iocoder.yudao.module.temu.controller.admin.seller;

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

import cn.iocoder.yudao.module.temu.controller.admin.seller.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import cn.iocoder.yudao.module.temu.service.seller.TemuSellerService;

@Tag(name = "管理后台 - Temu 卖家商城授权信息")
@RestController
@RequestMapping("/temu/seller")
@Validated
public class TemuSellerController {

    @Resource
    private TemuSellerService sellerService;
//
//    @PostMapping("/create")
//    @Operation(summary = "创建Temu 卖家商城授权信息")
//    @PreAuthorize("@ss.hasPermission('temu:seller:create')")
//    public CommonResult<Long> createSeller(@Valid @RequestBody TemuSellerSaveReqVO createReqVO) {
//        return success(sellerService.createSeller(createReqVO));
//    }

//    @PutMapping("/update")
//    @Operation(summary = "更新Temu 卖家商城授权信息")
//    @PreAuthorize("@ss.hasPermission('temu:seller:update')")
//    public CommonResult<Boolean> updateSeller(@Valid @RequestBody TemuSellerSaveReqVO updateReqVO) {
//        sellerService.updateSeller(updateReqVO);
//        return success(true);
//    }
//
//    @DeleteMapping("/delete")
//    @Operation(summary = "删除Temu 卖家商城授权信息")
//    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('temu:seller:delete')")
//    public CommonResult<Boolean> deleteSeller(@RequestParam("id") Long id) {
//        sellerService.deleteSeller(id);
//        return success(true);
//    }
//
//    @DeleteMapping("/delete-list")
//    @Parameter(name = "ids", description = "编号", required = true)
//    @Operation(summary = "批量删除Temu 卖家商城授权信息")
//                @PreAuthorize("@ss.hasPermission('temu:seller:delete')")
//    public CommonResult<Boolean> deleteSellerList(@RequestParam("ids") List<Long> ids) {
//        sellerService.deleteSellerListByIds(ids);
//        return success(true);
//    }

    @GetMapping("/get")
    @Operation(summary = "获得Temu 卖家商城授权信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('temu:seller:query')")
    public CommonResult<TemuSellerRespVO> getSeller(@RequestParam("id") Long id) {
        TemuSellerDO seller = sellerService.getSeller(id);
        return success(BeanUtils.toBean(seller, TemuSellerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Temu 卖家商城授权信息分页")
    @PreAuthorize("@ss.hasPermission('temu:seller:query')")
    public CommonResult<PageResult<TemuSellerRespVO>> getSellerPage(@Valid TemuSellerPageReqVO pageReqVO) {
        PageResult<TemuSellerDO> pageResult = sellerService.getSellerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TemuSellerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Temu 卖家商城授权信息 Excel")
    @PreAuthorize("@ss.hasPermission('temu:seller:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSellerExcel(@Valid TemuSellerPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemuSellerDO> list = sellerService.getSellerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Temu 卖家商城授权信息.xls", "数据", TemuSellerRespVO.class,
                        BeanUtils.toBean(list, TemuSellerRespVO.class));
    }

}