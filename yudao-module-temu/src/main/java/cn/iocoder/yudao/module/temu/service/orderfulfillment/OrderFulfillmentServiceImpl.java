package cn.iocoder.yudao.module.temu.service.orderfulfillment;

import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentConfirmReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentInfoReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentQueryReqVO;
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
 * Temu Order Fulfillment 订单履约业务 Service 实现。
 */
@Service
@Validated
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;

    /**
     * 调用 Temu 订单发货确认接口。
     *
     * @param request 发货方式及包裹明细
     * @return Temu 官方发货确认响应
     */
    @Override
    public JsonNode confirmShipment(OrderFulfillmentShipmentConfirmReqVO request) {
        return createClient(request).getFulfillment().shipmentV2Confirm(Map.of(
                "sendRequestList", request.getSendRequestList(), "sendType", request.getSendType()));
    }

    /**
     * 调用 Temu 订单发货信息查询接口。
     *
     * @param request 父订单与子订单查询参数
     * @return Temu 官方发货信息响应
     */
    @Override
    public JsonNode getShipmentInfo(OrderFulfillmentShipmentInfoReqVO request) {
        return createClient(request).getFulfillment().shipmentV2Get(Map.of(
                "parentOrderSn", request.getParentOrderSn(), "orderSn", request.getOrderSn()));
    }

    /**
     * 调用 Temu 包裹物流信息查询接口。
     *
     * <p>Postman 集合中该请求名称含有 update，但实际 type 为 {@code bg.logistics.shipment.get}，
     * 因此按查询语义调用，避免误发起修改操作。</p>
     *
     * @param request 包裹、物流公司和运单查询参数
     * @return Temu 官方包裹物流信息响应
     */
    @Override
    public JsonNode getShipment(OrderFulfillmentShipmentQueryReqVO request) {
        return createClient(request).getFulfillment().shipmentGet(Map.of(
                "packageSn", request.getPackageSn(), "shipCompanyId", request.getShipCompanyId(),
                "trackingNumber", request.getTrackingNumber()));
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求泄露敏感应用配置。</p>
     *
     * @param request 包含站点与授权 Token 的订单履约请求
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(OrderFulfillmentBaseReqVO request) {
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
     * @return 值为空或仅包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
