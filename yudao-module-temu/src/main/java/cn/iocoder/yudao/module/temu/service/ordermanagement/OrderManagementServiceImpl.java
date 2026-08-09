package cn.iocoder.yudao.module.temu.service.ordermanagement;

import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementCustomOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementParentOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementShippingCompaniesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.TemuOrderPageReqVO;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.module.temu.dal.dataobject.seller.TemuSellerDO;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import cn.iocoder.yudao.module.temu.dal.mysql.order.TemuOrderMapper;
import cn.iocoder.yudao.module.temu.dal.mysql.seller.TemuSellerMapper;
import cn.iocoder.yudao.module.temu.dal.mysql.shop.TemuShopMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.SELLER_NOT_EXISTS;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;

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
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;
    @Resource
    private TemuShopMapper shopMapper;
    @Resource
    private TemuSellerMapper sellerMapper;
    @Resource
    private TemuOrderMapper orderMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 调用 Temu 订单列表查询接口。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应
     */
    @Override
    public JsonNode getOrderList(OrderManagementOrderListReqVO request) {
        return queryOrderList(request);
    }

    /**
     * 拉取 Temu 订单并在调用成功后同步当前页子订单到本地。
     *
     * @param request 订单状态、区域和分页同步参数
     * @return Temu 官方订单列表响应
     */
    @Override
    public JsonNode syncOrderList(OrderManagementOrderListReqVO request) {
        TemuSellerDO seller = validateOrderOwner(request.getShopId());
        JsonNode response = queryOrderList(request);
        if (response != null && response.path("success").asBoolean(false)) {
            // 外部接口调用完成后才开启事务，避免网络耗时长期占用数据库连接和事务资源。
            transactionTemplate.executeWithoutResult(status ->
                    syncOrderList(response.path("result").path("pageItems"), request.getShopId(), seller.getId()));
        }
        return response;
    }

    /**
     * 调用 Temu 订单列表查询接口。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应
     */
    private JsonNode queryOrderList(OrderManagementOrderListReqVO request) {
        return createClient(request).getOrder().listOrdersV2(Map.of(
                "parentOrderStatus", request.getParentOrderStatus(), "regionId", request.getRegionId(),
                "pageNumber", request.getPageNumber(), "pageSize", request.getPageSize()));
    }

    /**
     * 分页查询本地已同步订单。
     *
     * @param request 店铺、卖家及订单筛选条件
     * @return 本地订单分页数据
     */
    @Override
    public PageResult<TemuOrderDO> getLocalOrderPage(TemuOrderPageReqVO request) {
        return orderMapper.selectPage(request);
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
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(),
                temuJsonStorageService, site.name(), temuApiRequestLogService, request.getShopId());
    }

    /**
     * 校验店铺存在，并从受控的店铺授权关系取得卖家编号。
     *
     * <p>卖家编号不从请求参数接收，防止订单被关联到其他店铺的卖家。</p>
     *
     * @param shopId 本地 Temu 店铺编号
     * @return 该店铺的卖家授权信息
     */
    private TemuSellerDO validateOrderOwner(Long shopId) {
        TemuShopDO shop = shopMapper.selectById(shopId);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        TemuSellerDO seller = sellerMapper.selectByShopId(shopId);
        if (seller == null) {
            throw exception(SELLER_NOT_EXISTS);
        }
        return seller;
    }

    /**
     * 将 Temu 父订单聚合响应转换并幂等保存为子订单记录。
     *
     * @param pageItems Temu 返回的父订单数组
     * @param shopId 归属店铺编号
     * @param sellerId 由店铺授权关系确定的卖家编号
     */
    private void syncOrderList(JsonNode pageItems, Long shopId, Long sellerId) {
        if (!pageItems.isArray()) {
            return;
        }
        for (JsonNode pageItem : pageItems) {
            JsonNode parent = pageItem.path("parentOrderMap");
            JsonNode orderList = pageItem.path("orderList");
            if (!orderList.isArray()) {
                continue;
            }
            for (JsonNode child : orderList) {
                String orderSn = text(child, "orderSn");
                if (isBlank(orderSn)) {
                    continue;
                }
                TemuOrderDO order = buildOrder(parent, child, shopId, sellerId);
                TemuOrderDO existing = orderMapper.selectByShopIdAndOrderSn(shopId, orderSn);
                if (existing == null) {
                    orderMapper.insert(order);
                } else {
                    order.setId(existing.getId());
                    orderMapper.updateById(order);
                }
            }
        }
    }

    /**
     * 合并父订单和子订单字段，生成本地子订单记录。
     *
     * @param parent Temu 父订单节点
     * @param child Temu 子订单节点
     * @param shopId 归属店铺编号
     * @param sellerId 归属卖家编号
     * @return 可持久化的订单记录
     */
    private TemuOrderDO buildOrder(JsonNode parent, JsonNode child, Long shopId, Long sellerId) {
        TemuOrderDO order = new TemuOrderDO();
        order.setShopId(shopId);
        order.setSellerId(sellerId);
        order.setParentOrderSn(text(parent, "parentOrderSn"));
        order.setOrderSn(text(child, "orderSn"));
        order.setSiteId(integer(parent, "siteId"));
        order.setRegionId(longValue(parent, "regionId"));
        order.setParentOrderStatus(integer(parent, "parentOrderStatus"));
        order.setOrderStatus(integer(child, "orderStatus"));
        order.setParentOrderPaymentType(text(parent, "orderPaymentType"));
        order.setOrderPaymentType(text(child, "orderPaymentType"));
        order.setFulfillmentType(text(child, "fulfillmentType"));
        order.setGoodsId(longValue(child, "goodsId"));
        order.setSkuId(longValue(child, "skuId"));
        order.setGoodsName(text(child, "goodsName"));
        order.setOriginalGoodsName(text(child, "originalGoodsName"));
        order.setSpec(text(child, "spec"));
        order.setOriginalSpecName(text(child, "originalSpecName"));
        order.setThumbUrl(text(child, "thumbUrl"));
        order.setQuantity(integer(child, "quantity"));
        order.setCanceledQuantityBeforeShipment(integer(child, "canceledQuantityBeforeShipment"));
        order.setOriginalOrderQuantity(integer(child, "originalOrderQuantity"));
        order.setShippingMethod(integer(parent, "shippingMethod"));
        order.setShipmentConsolidatedByMainMall(bool(parent, "isShipmentConsolidatedByMainMall"));
        order.setHasShippingFee(bool(parent, "hasShippingFee"));
        order.setParentOrderTime(epochSecond(parent, "parentOrderTime"));
        order.setParentConfirmTime(epochSecond(parent, "parentConfirmTime"));
        order.setOrderCreateTime(epochSecond(child, "orderCreateTime"));
        order.setOrderShippingTime(epochSecond(child, "orderShippingTime"));
        order.setExpectShipLatestTime(epochSecond(parent, "expectShipLatestTime"));
        order.setLatestDeliveryTime(epochSecond(parent, "latestDeliveryTime"));
        order.setTemuUpdateTime(epochSecond(parent, "updateTime"));
        order.setParentOrderLabels(json(parent, "parentOrderLabel"));
        order.setOrderLabels(json(child, "orderLabel"));
        order.setParentFulfillmentWarnings(json(parent, "fulfillmentWarning"));
        order.setFulfillmentWarnings(json(child, "fulfillmentWarning"));
        order.setPackageAbnormalTypes(json(child, "packageAbnormalTypeList"));
        order.setProductList(json(child, "productList"));
        order.setLastSyncTime(LocalDateTime.now());
        return order;
    }

    /** 从响应节点读取可空文本字段。 */
    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    /** 从响应节点读取可空整数型字段。 */
    private Integer integer(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asInt();
    }

    /** 从响应节点读取可空长整数型字段。 */
    private Long longValue(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asLong();
    }

    /** 从响应节点读取可空布尔字段。 */
    private Boolean bool(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asBoolean();
    }

    /** 将响应中的数组或对象字段序列化为数据库 JSON 文本。 */
    private String json(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.toString();
    }

    /** 将 Temu Unix 秒级时间戳转换为本地时间。 */
    private LocalDateTime epochSecond(JsonNode node, String field) {
        Long value = longValue(node, field);
        return value == null ? null : Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
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
