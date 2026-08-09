package cn.iocoder.yudao.module.temu.service.buyshipping;

import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingLabelReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingServicesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipLaterConfirmReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipLaterPackagesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipmentCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipmentUpdateReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;

/**
 * Temu Buy Shipping 购单发货业务 Service 实现。
 */
@Service
@Validated
public class BuyShippingServiceImpl implements BuyShippingService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;

    /** {@inheritDoc} */
    @Override
    public JsonNode getShippingServices(BuyShippingServicesReqVO request) {
        return createClient(request).getLogistics().shippingServices(Map.of(
                "warehouseId", request.getWarehouseId(), "length", request.getLength(), "width", request.getWidth(),
                "height", request.getHeight(), "weight", request.getWeight(), "dimensionUnit", request.getDimensionUnit(),
                "weightUnit", request.getWeightUnit(), "orderSnList", request.getOrderSnList()));
    }

    /** {@inheritDoc} */
    @Override
    public JsonNode getWarehouseList(BuyShippingBaseReqVO request) {
        return createClient(request).getLogistics().warehouseList(Map.of());
    }

    /** {@inheritDoc} */
    @Override
    public JsonNode createShipment(BuyShippingShipmentCreateReqVO request) {
        // 货件内字段与可用物流服务、卖家履约模式有关，保持原始 JSON 以兼容 Temu 的字段扩展。
        return createClient(request).getFulfillment().shipmentCreate(Map.of(
                "sendType", request.getSendType(), "sendRequestList", request.getSendRequestList()));
    }

    /** {@inheritDoc} */
    @Override
    public JsonNode updateShipment(BuyShippingShipmentUpdateReqVO request) {
        return createClient(request).getFulfillment().shipmentUpdate(Map.of(
                "retrySendPackageRequestList", request.getRetrySendPackageRequestList()));
    }

    /** {@inheritDoc} */
    @Override
    public JsonNode getShippingLabel(BuyShippingLabelReqVO request) {
        return createClient(request).getFulfillment().shipmentDocumentGet(Map.of("packageSnList", request.getPackageSnList()));
    }

    /** {@inheritDoc} */
    @Override
    public JsonNode getShipLaterPackages(BuyShippingShipLaterPackagesReqVO request) {
        return createClient(request).getFulfillment().unshippedPackageGet(Map.of(
                "pageNumber", request.getPageNumber(), "pageSize", request.getPageSize()));
    }

    /** {@inheritDoc} */
    @Override
    public JsonNode confirmShipLaterPackagesShipped(BuyShippingShipLaterConfirmReqVO request) {
        return createClient(request).getFulfillment().shippedPackageConfirm(Map.of(
                "packageSendInfoList", request.getPackageSendInfoList()));
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求泄露敏感应用配置。</p>
     *
     * @param request 包含站点和授权 Token 的购单发货请求
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(BuyShippingBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(),
                temuJsonStorageService, site.name(), temuApiRequestLogService, request.getShopId());
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断配置值
     * @return 值为空或只包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
