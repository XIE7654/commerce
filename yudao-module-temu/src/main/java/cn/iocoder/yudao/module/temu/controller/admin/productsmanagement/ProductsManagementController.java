package cn.iocoder.yudao.module.temu.controller.admin.productsmanagement;

import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementFullUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementGoodsIdReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementGoodsListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementPartialUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSaleStatusUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSkuListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSpecDetailReqVO;
import cn.iocoder.yudao.module.temu.service.productsmanagement.ProductsManagementService;
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

/**
 * 管理后台 Products Management 商品管理接口。
 */
@Tag(name = "管理后台 - Products Management")
@RestController
@RequestMapping("/temu/products-management")
@Validated
public class ProductsManagementController {

    @Resource
    private ProductsManagementService productsManagementService;

    /**
     * 查询 Temu 商品列表。
     *
     * @param request 商品列表查询参数
     * @return Temu 官方商品列表响应
     */
    @PostMapping("/goods/list")
    @Operation(summary = "查询 Temu 商品列表")
    @PreAuthorize("@ss.hasPermission('temu:products-management:query')")
    public JsonNode getGoodsList(@Valid @RequestBody ProductsManagementGoodsListReqVO request) {
        return productsManagementService.getGoodsList(request);
    }

    /**
     * 查询 Temu SKU 列表。
     *
     * @param request SKU 列表查询参数
     * @return Temu 官方 SKU 列表响应
     */
    @PostMapping("/skus/list")
    @Operation(summary = "查询 Temu SKU 列表")
    @PreAuthorize("@ss.hasPermission('temu:products-management:query')")
    public JsonNode getGoodsSkuList(@Valid @RequestBody ProductsManagementSkuListReqVO request) {
        return productsManagementService.getGoodsSkuList(request);
    }

    /**
     * 查询 Temu 商品详情。
     *
     * @param request 商品详情查询参数
     * @return Temu 官方商品详情响应
     */
    @PostMapping("/goods/detail")
    @Operation(summary = "查询 Temu 商品详情")
    @PreAuthorize("@ss.hasPermission('temu:products-management:query')")
    public JsonNode getGoodsDetail(@Valid @RequestBody ProductsManagementGoodsIdReqVO request) {
        return productsManagementService.getGoodsDetail(request);
    }

    /**
     * 查询 Temu 商品规格详情。
     *
     * @param request 商品规格详情查询参数
     * @return Temu 官方商品规格详情响应
     */
    @PostMapping("/goods/specs/detail")
    @Operation(summary = "查询 Temu 商品规格详情")
    @PreAuthorize("@ss.hasPermission('temu:products-management:query')")
    public JsonNode getGoodsSpecDetail(@Valid @RequestBody ProductsManagementSpecDetailReqVO request) {
        return productsManagementService.getGoodsSpecDetail(request);
    }

    /**
     * 完整更新 Temu 商品。
     *
     * @param request 商品完整更新参数
     * @return Temu 官方更新响应
     */
    @PostMapping("/goods/update")
    @Operation(summary = "完整更新 Temu 商品")
    @PreAuthorize("@ss.hasPermission('temu:products-management:update')")
    public JsonNode updateGoodsInfo(@Valid @RequestBody ProductsManagementFullUpdateReqVO request) {
        return productsManagementService.updateGoodsInfo(request);
    }

    /**
     * 部分更新 Temu 商品。
     *
     * @param request 商品部分更新参数
     * @return Temu 官方更新响应
     */
    @PostMapping("/goods/partial-update")
    @Operation(summary = "部分更新 Temu 商品")
    @PreAuthorize("@ss.hasPermission('temu:products-management:update')")
    public JsonNode partialUpdateGoodsInfo(@Valid @RequestBody ProductsManagementPartialUpdateReqVO request) {
        return productsManagementService.partialUpdateGoodsInfo(request);
    }

    /**
     * 查询 Temu 商品上架状态。
     *
     * @param request 商品上架状态查询参数
     * @return Temu 官方上架状态响应
     */
    @PostMapping("/goods/sale-status")
    @Operation(summary = "查询 Temu 商品上架状态")
    @PreAuthorize("@ss.hasPermission('temu:products-management:query')")
    public JsonNode getGoodsSaleStatus(@Valid @RequestBody ProductsManagementGoodsIdReqVO request) {
        return productsManagementService.getGoodsSaleStatus(request);
    }

    /**
     * 更新 Temu 商品上架状态。
     *
     * @param request 商品上架状态更新参数
     * @return Temu 官方更新响应
     */
    @PostMapping("/goods/sale-status/update")
    @Operation(summary = "更新 Temu 商品上架状态")
    @PreAuthorize("@ss.hasPermission('temu:products-management:update')")
    public JsonNode updateGoodsSaleStatus(@Valid @RequestBody ProductsManagementSaleStatusUpdateReqVO request) {
        return productsManagementService.updateGoodsSaleStatus(request);
    }
}
