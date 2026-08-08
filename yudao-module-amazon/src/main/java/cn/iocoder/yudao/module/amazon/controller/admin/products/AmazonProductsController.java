package cn.iocoder.yudao.module.amazon.controller.admin.products;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService;
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

/** Amazon Products 相关 API 管理接口。 */
@Tag(name = "管理后台 - Amazon Products")
@RestController
@RequestMapping("/amazon/products")
@Validated
public class AmazonProductsController {

    @Resource
    private AmazonProductsService amazonProductsService;

    /**
     * 查询 Amazon Catalog Items。
     *
     * @param request 店铺、站点和 Catalog 搜索条件
     * @return Amazon Catalog Items 原始响应
     */
    @PostMapping("/catalog-items/search")
    @Operation(summary = "查询 Amazon Catalog Items")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> searchCatalogItems(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.searchCatalogItems(request));
    }

    /**
     * 按 ASIN 查询 Amazon Catalog Item。
     *
     * @param request 店铺、站点、ASIN 和返回数据集
     * @return Amazon Catalog Item 原始响应
     */
    @PostMapping("/catalog-items/get")
    @Operation(summary = "按 ASIN 查询 Amazon Catalog Item")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getCatalogItem(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getCatalogItem(request));
    }

    /**
     * 批量查询 Featured Offer 预期价格。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 预期价格原始响应
     */
    @PostMapping("/pricing/featured-offer-expected-price")
    @Operation(summary = "批量查询 Amazon Featured Offer 预期价格")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getFeaturedOfferExpectedPriceBatch(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getFeaturedOfferExpectedPriceBatch(request));
    }

    /**
     * 批量查询商品竞争摘要。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 竞争摘要原始响应
     */
    @PostMapping("/pricing/competitive-summary")
    @Operation(summary = "批量查询 Amazon 商品竞争摘要")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getCompetitiveSummary(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getCompetitiveSummary(request));
    }

    /**
     * 按卖家 SKU 预估 Amazon 费用。
     *
     * @param request 店铺、站点、卖家 SKU 和费用请求体
     * @return Amazon 费用预估原始响应
     */
    @PostMapping("/fees/by-sku")
    @Operation(summary = "按 SKU 预估 Amazon 费用")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getMyFeesEstimateForSku(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getMyFeesEstimateForSku(request));
    }

    /**
     * 按 ASIN 预估 Amazon 费用。
     *
     * @param request 店铺、站点、ASIN 和费用请求体
     * @return Amazon 费用预估原始响应
     */
    @PostMapping("/fees/by-asin")
    @Operation(summary = "按 ASIN 预估 Amazon 费用")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getMyFeesEstimateForAsin(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getMyFeesEstimateForAsin(request));
    }

    /**
     * 批量预估 Amazon 费用。
     *
     * @param request 店铺、站点和官方费用请求数组
     * @return Amazon 批量费用预估原始响应
     */
    @PostMapping("/fees/batch")
    @Operation(summary = "批量预估 Amazon 费用")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getMyFeesEstimates(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getMyFeesEstimates(request));
    }

    /**
     * 搜索 Amazon 商品类型定义。
     *
     * @param request 店铺、站点和商品类型搜索条件
     * @return Amazon 商品类型列表原始响应
     */
    @PostMapping("/product-types/search")
    @Operation(summary = "搜索 Amazon 商品类型定义")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> searchDefinitionsProductTypes(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.searchDefinitionsProductTypes(request));
    }

    /**
     * 查询指定 Amazon 商品类型定义。
     *
     * @param request 店铺、站点、商品类型和定义筛选条件
     * @return Amazon 商品类型定义原始响应
     */
    @PostMapping("/product-types/get")
    @Operation(summary = "查询 Amazon 商品类型定义")
    @PreAuthorize("@ss.hasPermission('amazon:products:query')")
    public CommonResult<Map<String, Object>> getDefinitionsProductType(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(amazonProductsService.getDefinitionsProductType(request));
    }
}
