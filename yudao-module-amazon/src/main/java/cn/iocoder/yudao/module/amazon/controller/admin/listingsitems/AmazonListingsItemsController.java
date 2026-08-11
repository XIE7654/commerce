package cn.iocoder.yudao.module.amazon.controller.admin.listingsitems;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPatchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPutReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.service.listingsitems.AmazonListingsItemsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Amazon Listings Items 管理接口。 */
@Tag(name = "管理后台 - Amazon Listings Items")
@RestController
@RequestMapping("/amazon/listings-items")
public class AmazonListingsItemsController {

    @Resource
    private AmazonListingsItemsService service;

    /** 搜索商品 Listing。 */
    @PostMapping("/search")
    @PreAuthorize("@ss.hasPermission('amazon:listings-items:query')")
    public CommonResult<AmazonApiResponse<Map<String, Object>>> search(
            @Valid @RequestBody AmazonListingsSearchReqVO request) {
        return CommonResult.success(service.search(request));
    }

    /** 查询单个商品 Listing。 */
    @PostMapping("/get")
    @PreAuthorize("@ss.hasPermission('amazon:listings-items:query')")
    public CommonResult<AmazonApiResponse<Map<String, Object>>> get(
            @Valid @RequestBody AmazonListingsItemGetReqVO request) {
        return CommonResult.success(service.get(request));
    }

    /** 创建或完整更新商品 Listing。 */
    @PostMapping("/put")
    @PreAuthorize("@ss.hasPermission('amazon:listings-items:update')")
    public CommonResult<AmazonApiResponse<Map<String, Object>>> put(
            @Valid @RequestBody AmazonListingsItemPutReqVO request) {
        return CommonResult.success(service.put(request));
    }

    /** 部分更新商品 Listing。 */
    @PostMapping("/patch")
    @PreAuthorize("@ss.hasPermission('amazon:listings-items:update')")
    public CommonResult<AmazonApiResponse<Map<String, Object>>> patch(
            @Valid @RequestBody AmazonListingsItemPatchReqVO request) {
        return CommonResult.success(service.patch(request));
    }

    /** 删除商品 Listing。 */
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('amazon:listings-items:update')")
    public CommonResult<AmazonApiResponse<Map<String, Object>>> delete(
            @Valid @RequestBody AmazonListingsItemGetReqVO request) {
        return CommonResult.success(service.delete(request));
    }
}
