package cn.iocoder.yudao.module.amazon.controller.admin.catalogitems;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.service.catalogitems.AmazonCatalogItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/** Amazon Catalog Items 管理接口。 */
@Tag(name = "管理后台 - Amazon Catalog Items")
@RestController
@RequestMapping("/amazon/catalog-items")
public class AmazonCatalogItemsController {

    @Resource
    private AmazonCatalogItemsService service;

    /**
     * 搜索 Amazon Catalog Items。
     *
     * @param request 店铺、站点和搜索条件
     * @return Amazon 返回的 Catalog Items 数据
     */
    @PostMapping("/search")
    @Operation(summary = "搜索 Amazon Catalog Items")
    @PreAuthorize("@ss.hasPermission('amazon:catalog-items:query')")
    public CommonResult<Map<String, Object>> search(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(service.search(request));
    }

    /**
     * 查询 Amazon Catalog Item。
     *
     * @param request 店铺、站点、ASIN 和返回数据集
     * @return Amazon 返回的 Catalog Item 数据
     */
    @PostMapping("/get")
    @Operation(summary = "查询 Amazon Catalog Item")
    @PreAuthorize("@ss.hasPermission('amazon:catalog-items:query')")
    public CommonResult<Map<String, Object>> get(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(service.get(request));
    }

    /**
     * 查询商品所属的 Amazon Catalog 分类层级。
     *
     * @param request 店铺、站点，以及二选一的 ASIN 或卖家 SKU
     * @return Amazon 返回的商品分类列表
     */
    @PostMapping("/categories")
    @Operation(summary = "查询 Amazon Catalog 分类")
    @PreAuthorize("@ss.hasPermission('amazon:catalog-items:query')")
    public CommonResult<Map<String, Object>> listCategories(@Valid @RequestBody AmazonProductsReqVO request) {
        return CommonResult.success(service.listCategories(request));
    }
}
