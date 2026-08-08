package cn.iocoder.yudao.module.amazon.service.fbainventory;

import cn.iocoder.yudao.module.amazon.controller.admin.fbainventory.vo.FbaInventorySummariesReqVO;

import java.util.Map;

/**
 * Amazon FBA Inventory 服务。
 */
public interface FbaInventoryService {

    /**
     * 查询店铺在指定站点的 FBA 库存摘要。
     *
     * @param request 店铺、国家代码及库存筛选条件
     * @return Amazon FBA Inventory 原始响应
     */
    Map<String, Object> getInventorySummaries(FbaInventorySummariesReqVO request);
}
