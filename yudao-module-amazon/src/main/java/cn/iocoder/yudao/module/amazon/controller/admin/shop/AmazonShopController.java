package cn.iocoder.yudao.module.amazon.controller.admin.shop;

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

import cn.iocoder.yudao.module.amazon.controller.admin.shop.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.service.shop.AmazonShopService;

@Tag(name = "管理后台 - Amazon店铺授权")
@RestController
@RequestMapping("/amazon/shop")
@Validated
public class AmazonShopController {

    @Resource
    private AmazonShopService shopService;

    @PostMapping("/create")
    @Operation(summary = "创建Amazon店铺授权")
    @PreAuthorize("@ss.hasPermission('amazon:shop:create')")
    public CommonResult<Long> createShop(@Valid @RequestBody AmazonShopSaveReqVO createReqVO) {
        return success(shopService.createShop(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Amazon店铺授权")
    @PreAuthorize("@ss.hasPermission('amazon:shop:update')")
    public CommonResult<Boolean> updateShop(@Valid @RequestBody AmazonShopSaveReqVO updateReqVO) {
        shopService.updateShop(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Amazon店铺授权")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('amazon:shop:delete')")
    public CommonResult<Boolean> deleteShop(@RequestParam("id") Long id) {
        shopService.deleteShop(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Amazon店铺授权")
                @PreAuthorize("@ss.hasPermission('amazon:shop:delete')")
    public CommonResult<Boolean> deleteShopList(@RequestParam("ids") List<Long> ids) {
        shopService.deleteShopListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Amazon店铺授权")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('amazon:shop:query')")
    public CommonResult<AmazonShopRespVO> getShop(@RequestParam("id") Long id) {
        AmazonShopDO shop = shopService.getShop(id);
        return success(BeanUtils.toBean(shop, AmazonShopRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Amazon店铺授权分页")
    @PreAuthorize("@ss.hasPermission('amazon:shop:query')")
    public CommonResult<PageResult<AmazonShopRespVO>> getShopPage(@Valid AmazonShopPageReqVO pageReqVO) {
        PageResult<AmazonShopDO> pageResult = shopService.getShopPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AmazonShopRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Amazon店铺授权 Excel")
    @PreAuthorize("@ss.hasPermission('amazon:shop:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShopExcel(@Valid AmazonShopPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AmazonShopDO> list = shopService.getShopPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Amazon店铺授权.xls", "数据", AmazonShopRespVO.class,
                        BeanUtils.toBean(list, AmazonShopRespVO.class));
    }

}