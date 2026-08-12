package cn.iocoder.yudao.module.temu.controller.admin.addproducts;

import cn.iocoder.yudao.module.temu.controller.admin.addproducts.vo.AddProductsCatsReqVO;
import cn.iocoder.yudao.module.temu.service.addproducts.AddProductsService;
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
 * 管理后台 Add Products 商品发布接口。
 */
@Tag(name = "管理后台 - Add Products")
@RestController
@RequestMapping("/temu/add-products")
@Validated
public class AddProductsController {

    @Resource
    private AddProductsService addProductsService;

    /**
     * 查询 Temu 商品分类。
     *
     * @param request 分类查询参数
     * @return Temu 官方分类查询响应
     */
    @PostMapping("/categories")
    @Operation(summary = "查询 Temu 商品分类")
    @PreAuthorize("@ss.hasPermission('temu:add-products:query')")
    public TemuApiResponse<List<CatsGetCategoryResult>> getCategories(@Valid @RequestBody AddProductsCatsReqVO request) {
        return addProductsService.getCategories(request);
    }
}
