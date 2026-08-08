package cn.iocoder.yudao.module.amazon.controller.admin.productpricing;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.productpricing.vo.AmazonProductPricingReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.service.productpricing.AmazonProductPricingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Amazon Product Pricing 管理接口。 */
@Tag(name = "管理后台 - Amazon Product Pricing")
@RestController
@RequestMapping("/amazon/product-pricing")
public class AmazonProductPricingController {

    @Resource
    private AmazonProductPricingService service;

    /**
     * 批量查询 Featured Offer 预期价格。
     *
     * @param request 店铺、站点和 2022 版本官方批量请求体
     * @return Amazon 原始响应
     */
    @PostMapping("/featured-offer-expected-price")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> featured(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(service.featured(request));
    }

    /**
     * 批量查询 2022 版本商品竞争摘要。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 原始响应
     */
    @PostMapping("/competitive-summary")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> competitive(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(service.competitive(request));
    }

    /**
     * 查询卖家商品价格。
     *
     * @param request 店铺、站点和价格查询条件
     * @return Amazon 原始响应
     */
    @PostMapping("/price")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> getPricing(@Valid @RequestBody AmazonProductPricingReqVO request) {
        return CommonResult.success(service.getPricing(request));
    }

    /**
     * 查询卖家商品竞争价格。
     *
     * @param request 店铺、站点和竞争价格查询条件
     * @return Amazon 原始响应
     */
    @PostMapping("/competitive-price")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> getCompetitivePricing(@Valid @RequestBody AmazonProductPricingReqVO request) {
        return CommonResult.success(service.getCompetitivePricing(request));
    }

    /**
     * 按卖家 SKU 查询最低报价。
     *
     * @param request 店铺、站点、SKU 和商品成色
     * @return Amazon 原始响应
     */
    @PostMapping("/listing-offers")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> getListingOffers(@Valid @RequestBody AmazonProductPricingReqVO request) {
        return CommonResult.success(service.getListingOffers(request));
    }

    /**
     * 按 ASIN 查询最低报价。
     *
     * @param request 店铺、站点、ASIN 和商品成色
     * @return Amazon 原始响应
     */
    @PostMapping("/item-offers")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> getItemOffers(@Valid @RequestBody AmazonProductPricingReqVO request) {
        return CommonResult.success(service.getItemOffers(request));
    }

    /**
     * 按 ASIN 批量查询最低报价。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 原始响应
     */
    @PostMapping("/item-offers-batch")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> getItemOffersBatch(@Valid @RequestBody AmazonProductPricingReqVO request) {
        return CommonResult.success(service.getItemOffersBatch(request));
    }

    /**
     * 按卖家 SKU 批量查询最低报价。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 原始响应
     */
    @PostMapping("/listing-offers-batch")
    @PreAuthorize("@ss.hasPermission('amazon:product-pricing:query')")
    public CommonResult<Map<String, Object>> getListingOffersBatch(@Valid @RequestBody AmazonProductPricingReqVO request) {
        return CommonResult.success(service.getListingOffersBatch(request));
    }
}
