package cn.iocoder.yudao.module.temu.service.ordermanagement;

import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementCustomOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementParentOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementShippingCompaniesReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;

/**
 * Temu Order Management 订单管理业务 Service 实现。
 */
@Service
@Validated
public class OrderManagementServiceImpl implements OrderManagementService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;

    /**
     * 调用 Temu 订单列表查询接口。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应
     */
    @Override
    public JsonNode getOrderList(OrderManagementOrderListReqVO request) {
        return createClient(request).getOrder().listOrdersV2(Map.of(
                "parentOrderStatus", request.getParentOrderStatus(), "regionId", request.getRegionId(),
                "pageNumber", request.getPageNumber(), "pageSize", request.getPageSize()));
    }

    /**
     * 调用 Temu 父订单详情查询接口。
     *
     * @param request 父订单查询参数
     * @return Temu 官方订单详情响应
     */
    @Override
    public JsonNode getOrderDetail(OrderManagementParentOrderReqVO request) {
        return createClient(request).getOrder().detailOrderV2(Map.of("parentOrderSn", request.getParentOrderSn()));
    }

    /**
     * 调用 Temu 定制订单详情查询接口。
     *
     * @param request 子订单编号列表查询参数
     * @return Temu 官方定制订单详情响应
     */
    @Override
    public JsonNode getCustomOrderDetail(OrderManagementCustomOrderReqVO request) {
        return createClient(request).getOrder().customizationOrder(Map.of("orderSnList", request.getOrderSnList()));
    }

    /**
     * 调用 Temu 父订单收货信息查询接口。
     *
     * @param request 父订单查询参数
     * @return Temu 官方收货信息响应
     */
    @Override
    public JsonNode getOrderShippingInfo(OrderManagementParentOrderReqVO request) {
        return createClient(request).getOrder().shippinginfoOrderV2(Map.of("parentOrderSn", request.getParentOrderSn()));
    }

    /**
     * 调用 Temu 区域承运商查询接口。
     *
     * @param request 区域查询参数
     * @return Temu 官方承运商列表响应
     */
    @Override
    public JsonNode getOrderShippingCompanies(OrderManagementShippingCompaniesReqVO request) {
        return createClient(request).getLogistics().companies(Map.of("regionId", request.getRegionId()));
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求泄露敏感应用配置。</p>
     *
     * @param request 包含站点与授权 Token 的订单管理请求
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(OrderManagementBaseReqVO request) {
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
