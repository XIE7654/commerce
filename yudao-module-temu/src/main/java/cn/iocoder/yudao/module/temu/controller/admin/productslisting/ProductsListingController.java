package cn.iocoder.yudao.module.temu.controller.admin.productslisting;

import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryReqVO;
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

}
