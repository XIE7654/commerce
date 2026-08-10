package cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace;

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

import cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo.*;
import cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace.AmazonListingMarketplaceDO;
import cn.iocoder.yudao.module.amazon.service.listingmarketplace.AmazonListingMarketplaceService;

@Tag(name = "管理后台 - Listing信息表")
@RestController
@RequestMapping("/amazon/listing-marketplace")
@Validated
public class AmazonListingMarketplaceController {

    @Resource
    private AmazonListingMarketplaceService listingMarketplaceService;

    /**
     * 同步所有启用店铺的参与站点 Listings，并保存到本地 Listing 表。
     *
     * @return 同步汇总结果
     */
    @PostMapping("/sync")
    @Operation(summary = "同步全部可用店铺 Amazon Listings")
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:update')")
    public CommonResult<AmazonListingMarketplaceSyncRespVO> syncAllAvailableListings() {
        return success(listingMarketplaceService.syncAllAvailableListings());
    }

    @PostMapping("/create")
    @Operation(summary = "创建Listing信息表")
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:create')")
    public CommonResult<Long> createListingMarketplace(@Valid @RequestBody AmazonListingMarketplaceSaveReqVO createReqVO) {
        return success(listingMarketplaceService.createListingMarketplace(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新Listing信息表")
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:update')")
    public CommonResult<Boolean> updateListingMarketplace(@Valid @RequestBody AmazonListingMarketplaceSaveReqVO updateReqVO) {
        listingMarketplaceService.updateListingMarketplace(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除Listing信息表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:delete')")
    public CommonResult<Boolean> deleteListingMarketplace(@RequestParam("id") Long id) {
        listingMarketplaceService.deleteListingMarketplace(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除Listing信息表")
                @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:delete')")
    public CommonResult<Boolean> deleteListingMarketplaceList(@RequestParam("ids") List<Long> ids) {
        listingMarketplaceService.deleteListingMarketplaceListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Listing信息表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:query')")
    public CommonResult<AmazonListingMarketplaceRespVO> getListingMarketplace(@RequestParam("id") Long id) {
        AmazonListingMarketplaceDO listingMarketplace = listingMarketplaceService.getListingMarketplace(id);
        return success(BeanUtils.toBean(listingMarketplace, AmazonListingMarketplaceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Listing信息表分页")
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:query')")
    public CommonResult<PageResult<AmazonListingMarketplaceRespVO>> getListingMarketplacePage(@Valid AmazonListingMarketplacePageReqVO pageReqVO) {
        PageResult<AmazonListingMarketplaceDO> pageResult = listingMarketplaceService.getListingMarketplacePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AmazonListingMarketplaceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Listing信息表 Excel")
    @PreAuthorize("@ss.hasPermission('amazon:listing-marketplace:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportListingMarketplaceExcel(@Valid AmazonListingMarketplacePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AmazonListingMarketplaceDO> list = listingMarketplaceService.getListingMarketplacePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Listing信息表.xls", "数据", AmazonListingMarketplaceRespVO.class,
                        BeanUtils.toBean(list, AmazonListingMarketplaceRespVO.class));
    }

}
