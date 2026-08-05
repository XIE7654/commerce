package cn.iocoder.yudao.module.temu.controller.admin.shop;

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

import cn.iocoder.yudao.module.temu.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.ShopDO;
import cn.iocoder.yudao.module.temu.service.shop.ShopService;

@Tag(name = "管理后台 - Temu 店铺")
@RestController
@RequestMapping("/temu/shop")
@Validated
public class ShopController {

    @Resource
    private ShopService shopService;

    @PostMapping("/create")
    @Operation(summary = "创建Temu 店铺")
    @PreAuthorize("@ss.hasPermission('temu:shop:create')")
    public CommonResult<Long> createShop(@Valid @RequestBody ShopSaveReqVO createReqVO) {
        return success(shopService.createShop(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Temu 店铺")
    @PreAuthorize("@ss.hasPermission('temu:shop:update')")
    public CommonResult<Boolean> updateShop(@Valid @RequestBody ShopSaveReqVO updateReqVO) {
        shopService.updateShop(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Temu 店铺")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('temu:shop:delete')")
    public CommonResult<Boolean> deleteShop(@RequestParam("id") Long id) {
        shopService.deleteShop(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Temu 店铺")
                @PreAuthorize("@ss.hasPermission('temu:shop:delete')")
    public CommonResult<Boolean> deleteShopList(@RequestParam("ids") List<Long> ids) {
        shopService.deleteShopListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Temu 店铺")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('temu:shop:query')")
    public CommonResult<ShopRespVO> getShop(@RequestParam("id") Long id) {
        ShopDO shop = shopService.getShop(id);
        return success(BeanUtils.toBean(shop, ShopRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Temu 店铺分页")
    @PreAuthorize("@ss.hasPermission('temu:shop:query')")
    public CommonResult<PageResult<ShopRespVO>> getShopPage(@Valid ShopPageReqVO pageReqVO) {
        PageResult<ShopDO> pageResult = shopService.getShopPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShopRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Temu 店铺 Excel")
    @PreAuthorize("@ss.hasPermission('temu:shop:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShopExcel(@Valid ShopPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShopDO> list = shopService.getShopPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Temu 店铺.xls", "数据", ShopRespVO.class,
                        BeanUtils.toBean(list, ShopRespVO.class));
    }

}