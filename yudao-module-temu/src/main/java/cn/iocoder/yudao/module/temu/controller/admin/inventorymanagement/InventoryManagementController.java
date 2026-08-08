package cn.iocoder.yudao.module.temu.controller.admin.inventorymanagement;

import cn.iocoder.yudao.module.temu.controller.admin.inventorymanagement.vo.InventoryGoodsStockUpdateReqVO;
import cn.iocoder.yudao.module.temu.service.inventorymanagement.InventoryManagementService;
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
 * 管理后台 Inventory Management 库存接口。
 */
@Tag(name = "管理后台 - Inventory Management")
@RestController
@RequestMapping("/temu/inventory-management")
@Validated
public class InventoryManagementController {

    @Resource
    private InventoryManagementService inventoryManagementService;

    /**
     * 更新 Temu 商品 SKU 库存。
     *
     * @param request 库存更新参数
     * @return Temu 官方库存更新响应
     */
    @PostMapping("/goods/stock")
    @Operation(summary = "更新 Temu 商品 SKU 库存")
    @PreAuthorize("@ss.hasPermission('temu:inventory-management:update')")
    public JsonNode updateGoodsStock(@Valid @RequestBody InventoryGoodsStockUpdateReqVO request) {
        return inventoryManagementService.updateGoodsStock(request);
    }
}
