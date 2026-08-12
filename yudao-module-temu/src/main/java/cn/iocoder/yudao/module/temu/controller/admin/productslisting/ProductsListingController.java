package cn.iocoder.yudao.module.temu.controller.admin.productslisting;

import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingBrandTrademarkReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCatIdReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryRecommendReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingGoodsCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingImageUploadReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingPropertyRecommendReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingSpecIdReqVO;
import cn.iocoder.yudao.module.temu.service.productslisting.ProductsListingService;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;
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
import tools.jackson.databind.JsonNode;

import java.util.List;

/**
 * 管理后台 Products Listing 商品刊登接口。
 */
@Tag(name = "管理后台 - Products Listing")
@RestController
@RequestMapping("/temu/products-listing")
@Validated
public class ProductsListingController {

    @Resource
    private ProductsListingService productsListingService;

    /**
     * 查询 Temu 商品分类。
     *
     * @param request 分类查询参数
     * @return Temu 官方分类响应
     */
    @PostMapping("/goods/categories")
    @Operation(summary = "查询 Temu 商品分类")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public TemuApiResponse<List<CatsGetCategoryResult>> getGoodsCategories(@Valid @RequestBody ProductsListingCategoryReqVO request) {
        return productsListingService.getGoodsCategories(request);
    }

    /**
     * 查询 Temu 分类商品模板。
     *
     * @param request 分类模板查询参数
     * @return Temu 官方模板响应
     */
    @PostMapping("/goods/template")
    @Operation(summary = "查询 Temu 分类商品模板")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode getGoodsTemplate(@Valid @RequestBody ProductsListingCatIdReqVO request) {
        return productsListingService.getGoodsTemplate(request);
    }

    /**
     * 查询 Temu 可用品牌与商标。
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方品牌与商标响应
     */
    @PostMapping("/goods/brand-trademarks")
    @Operation(summary = "查询 Temu 可用品牌与商标")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode getAvailableBrandAndTrademark(@Valid @RequestBody ProductsListingBrandTrademarkReqVO request) {
        return productsListingService.getAvailableBrandAndTrademark(request);
    }

    /**
     * 按 Postman 集合定义查询 Temu 签名信息。
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方响应
     */
    @PostMapping("/goods/signature")
    @Operation(summary = "查询 Temu 签名信息")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode getSignature(@Valid @RequestBody ProductsListingBrandTrademarkReqVO request) {
        return productsListingService.getSignature(request);
    }

    /**
     * 查询 Temu 运费模板。
     *
     * @param request 认证参数
     * @return Temu 官方运费模板响应
     */
    @PostMapping("/shipping-templates")
    @Operation(summary = "查询 Temu 运费模板")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode getShippingTemplates(@Valid @RequestBody ProductsListingBaseReqVO request) {
        return productsListingService.getShippingTemplates(request);
    }

    /**
     * 查询 Temu 尺码元素。
     *
     * @param request 分类参数
     * @return Temu 官方尺码元素响应
     */
    @PostMapping("/goods/size-elements")
    @Operation(summary = "查询 Temu 尺码元素")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode getSizeElements(@Valid @RequestBody ProductsListingCatIdReqVO request) {
        return productsListingService.getSizeElements(request);
    }

    /**
     * 上传并自动处理 Temu 商品图片。
     *
     * @param request 图片地址和处理参数
     * @return Temu 官方上传响应
     */
    @PostMapping("/images/auto-transform")
    @Operation(summary = "上传并自动处理 Temu 商品图片")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:create')")
    public JsonNode uploadImageWithAutoTransformer(@Valid @RequestBody ProductsListingImageUploadReqVO request) {
        return productsListingService.uploadImageWithAutoTransformer(request);
    }

    /**
     * 创建 Temu 商品规格 ID。
     *
     * @param request 规格创建参数
     * @return Temu 官方规格响应
     */
    @PostMapping("/specs")
    @Operation(summary = "创建 Temu 商品规格 ID")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:create')")
    public JsonNode createSpecId(@Valid @RequestBody ProductsListingSpecIdReqVO request) {
        return productsListingService.createSpecId(request);
    }

    /**
     * 创建 Temu 商品。
     *
     * @param request 商品创建参数
     * @return Temu 官方商品创建响应
     */
    @PostMapping("/goods")
    @Operation(summary = "创建 Temu 商品")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:create')")
    public JsonNode createGoods(@Valid @RequestBody ProductsListingGoodsCreateReqVO request) {
        return productsListingService.createGoods(request);
    }

    /**
     * 推荐 Temu 商品分类。
     *
     * @param request 商品名称
     * @return Temu 官方分类推荐响应
     */
    @PostMapping("/goods/category-recommendations")
    @Operation(summary = "推荐 Temu 商品分类")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode recommendCategories(@Valid @RequestBody ProductsListingCategoryRecommendReqVO request) {
        return productsListingService.recommendCategories(request);
    }

    /**
     * 推荐 Temu 商品属性。
     *
     * @param request 分类、商品名称和已有属性
     * @return Temu 官方属性推荐响应
     */
    @PostMapping("/goods/property-recommendations")
    @Operation(summary = "推荐 Temu 商品属性")
    @PreAuthorize("@ss.hasPermission('temu:products-listing:query')")
    public JsonNode recommendProperties(@Valid @RequestBody ProductsListingPropertyRecommendReqVO request) {
        return productsListingService.recommendProperties(request);
    }
}
