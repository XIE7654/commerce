package cn.iocoder.yudao.module.amazon.controller.admin.listings;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPatchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPutReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsRestrictionsReqVO;
import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService;
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
 * Amazon Listings Items 管理接口。
 */
@Tag(name = "管理后台 - Amazon Listings")
@RestController
@RequestMapping("/amazon/listings")
@Validated
public class AmazonListingsController {

    @Resource
    private AmazonListingsService amazonListingsService;

    /**
     * 查询指定店铺在目标国家站点的 Listings Items，不保存 Amazon 返回数据。
     *
     * @param request 店铺、国家代码及查询筛选条件
     * @return Amazon Listings Items 原始响应
     */
    @PostMapping("/search")
    @Operation(summary = "查询 Amazon Listings Items")
    @PreAuthorize("@ss.hasPermission('amazon:listings:query')")
    public CommonResult<Map<String, Object>> search(@Valid @RequestBody AmazonListingsSearchReqVO request) {
        return CommonResult.success(amazonListingsService.searchListingsItems(request));
    }

    /**
     * 根据 SKU 查询指定店铺在目标国家站点的单个 Listings Item，不保存业务数据。
     *
     * @param request 店铺、国家代码、SKU 及返回数据集
     * @return Amazon Listings Item 原始响应
     */
    @PostMapping("/item")
    @Operation(summary = "按 SKU 查询 Amazon Listings Item")
    @PreAuthorize("@ss.hasPermission('amazon:listings:query')")
    public CommonResult<Map<String, Object>> getItem(@Valid @RequestBody AmazonListingsItemGetReqVO request) {
        return CommonResult.success(amazonListingsService.getListingsItem(request));
    }

    /**
     * 创建或全量更新指定 SKU 的 Listings Item。
     *
     * @param request 店铺、站点、SKU 和完整商品属性
     * @return Amazon 提交结果
     */
    @PostMapping("/item/put")
    @Operation(summary = "创建或全量更新 Amazon Listings Item")
    @PreAuthorize("@ss.hasPermission('amazon:listings:update')")
    public CommonResult<Map<String, Object>> putItem(@Valid @RequestBody AmazonListingsItemPutReqVO request) {
        return CommonResult.success(amazonListingsService.putListingsItem(request));
    }

    /**
     * 局部更新指定 SKU 的 Listings Item。
     *
     * @param request 店铺、站点、SKU 和 JSON Patch 操作
     * @return Amazon 提交结果
     */
    @PostMapping("/item/patch")
    @Operation(summary = "局部更新 Amazon Listings Item")
    @PreAuthorize("@ss.hasPermission('amazon:listings:update')")
    public CommonResult<Map<String, Object>> patchItem(@Valid @RequestBody AmazonListingsItemPatchReqVO request) {
        return CommonResult.success(amazonListingsService.patchListingsItem(request));
    }

    /**
     * 删除指定 SKU 的 Listings Item。
     *
     * @param request 店铺、站点和 SKU
     * @return Amazon 删除结果
     */
    @PostMapping("/item/delete")
    @Operation(summary = "删除 Amazon Listings Item")
    @PreAuthorize("@ss.hasPermission('amazon:listings:delete')")
    public CommonResult<Map<String, Object>> deleteItem(@Valid @RequestBody AmazonListingsItemGetReqVO request) {
        return CommonResult.success(amazonListingsService.deleteListingsItem(request));
    }

    /**
     * 查询指定 ASIN 的上架限制。
     *
     * @param request 店铺、站点和 ASIN
     * @return Amazon 限制信息
     */
    @PostMapping("/restrictions")
    @Operation(summary = "查询 Amazon Listings 上架限制")
    @PreAuthorize("@ss.hasPermission('amazon:listings:query')")
    public CommonResult<Map<String, Object>> getRestrictions(@Valid @RequestBody AmazonListingsRestrictionsReqVO request) {
        return CommonResult.success(amazonListingsService.getListingsRestrictions(request));
    }
}
