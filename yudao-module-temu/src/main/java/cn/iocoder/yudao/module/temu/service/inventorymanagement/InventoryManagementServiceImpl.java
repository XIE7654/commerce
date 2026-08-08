package cn.iocoder.yudao.module.temu.service.inventorymanagement;

import cn.iocoder.yudao.module.temu.controller.admin.inventorymanagement.vo.InventoryGoodsStockUpdateReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Temu Inventory Management 库存管理业务 Service 实现。
 */
@Service
@Validated
public class InventoryManagementServiceImpl implements InventoryManagementService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;

    /**
     * 调用 Temu 库存更新接口。
     *
     * <p>库存目标值与库存变更量按 Temu 原始字段名透传；使用 {@link LinkedHashMap}
     * 保留可选字段的 null 值，由 SDK 在签名前统一过滤，避免 {@code Map.of} 拒绝空值。</p>
     *
     * @param request 包含站点、授权 Token、幂等键及库存明细的请求参数
     * @return Temu 官方库存更新响应
     */
    @Override
    public JsonNode updateGoodsStock(InventoryGoodsStockUpdateReqVO request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("requestUniqueKey", request.getRequestUniqueKey());
        params.put("goodsId", request.getGoodsId());
        params.put("skuStockTargetList", request.getSkuStockTargetList());
        params.put("skuStockChangeList", request.getSkuStockChangeList());
        return createClient(request).getProduct().stockEdit(params);
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求泄露敏感应用配置。</p>
     *
     * @param request 包含站点与授权 Token 的库存更新请求
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(InventoryGoodsStockUpdateReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(), temuJsonStorageService);
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断配置值
     * @return 值为空或仅包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
