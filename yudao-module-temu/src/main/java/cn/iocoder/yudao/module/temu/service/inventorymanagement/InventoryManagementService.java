package cn.iocoder.yudao.module.temu.service.inventorymanagement;

import cn.iocoder.yudao.module.temu.controller.admin.inventorymanagement.vo.InventoryGoodsStockUpdateReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Temu Inventory Management 库存管理业务 Service。
 */
public interface InventoryManagementService {

    /**
     * 更新 Temu 商品下各 SKU 的目标库存或库存增量。
     *
     * @param request 包含站点、授权 Token、幂等键及库存明细的请求参数
     * @return Temu 官方库存更新响应
     */
    JsonNode updateGoodsStock(InventoryGoodsStockUpdateReqVO request);
}
