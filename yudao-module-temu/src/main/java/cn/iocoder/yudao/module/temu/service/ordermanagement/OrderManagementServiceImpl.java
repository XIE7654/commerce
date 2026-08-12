package cn.iocoder.yudao.module.temu.service.ordermanagement;

import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementCustomOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementParentOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementShippingCompaniesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.TemuOrderPageReqVO;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.module.temu.dal.dataobject.shop.TemuShopDO;
import cn.iocoder.yudao.module.temu.dal.mysql.order.TemuOrderMapper;
import cn.iocoder.yudao.module.temu.dal.mysql.shop.TemuShopMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.order.ChildOrderDto;
import cn.iocoder.yudao.module.temu.framework.client.order.CustomizationOrderReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.CustomizationOrderListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderDetailDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderListReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderPageItemDto;
import cn.iocoder.yudao.module.temu.framework.client.order.ParentOrderDto;
import cn.iocoder.yudao.module.temu.framework.client.order.ParentOrderReqVO;
import cn.iocoder.yudao.module.temu.framework.client.order.ShippingInfoDto;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import cn.iocoder.yudao.module.temu.mq.TemuOrderSyncMessage;
import cn.iocoder.yudao.module.temu.mq.TemuOrderSyncRabbitMQConfig;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;
import java.util.List;
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
@Slf4j
public class OrderManagementServiceImpl implements OrderManagementService {

    /** 自动同步时采用 Temu 全部父订单状态。 */
    private static final int ALL_PARENT_ORDER_STATUS = 4;
    /** 单页最大拉取数量，减少远端请求次数。 */
    private static final int SYNC_PAGE_SIZE = 100;

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;
    @Resource
    private TemuShopMapper shopMapper;
    @Resource
    private TemuOrderMapper orderMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 调用 Temu 订单列表查询接口。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应
     */
    @Override
    public TemuApiResponse<OrderListDto> getOrderList(OrderManagementOrderListReqVO request) {
        return queryOrderList(request);
    }

    /**
     * 拉取 Temu 订单并在调用成功后同步当前页子订单到本地。
     *
     * @param request 订单状态、区域和分页同步参数
     * @return Temu 官方订单列表响应
     */
    @Override
    public TemuApiResponse<OrderListDto> syncOrderList(OrderManagementOrderListReqVO request) {
        validateOrderOwner(request.getShopId());
        TemuApiResponse<OrderListDto> response = queryOrderList(request);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            // 外部接口调用完成后才开启事务，避免网络耗时长期占用数据库连接和事务资源。
            transactionTemplate.executeWithoutResult(status ->
                    syncOrderList(response.getResult().getPageItems(), request.getShopId()));
        }
        return response;
    }

    /**
     * 同步所有启用店铺的 Temu 订单。
     *
     * <p>接口仅投递店铺同步消息，避免全量分页拉取占用管理端请求；消息消费者会独立处理每个店铺。</p>
     */
    @Override
    public void syncAllAvailableShopOrders() {
        for (TemuShopDO shop : shopMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus())) {
            if (isBlank(shop.getAuthToken())) {
                log.warn("[syncAllAvailableShopOrders][shopId({})] 店铺未配置授权 Token，跳过订单同步", shop.getId());
                continue;
            }
            rabbitTemplate.convertAndSend(TemuOrderSyncRabbitMQConfig.EXCHANGE_NAME,
                    TemuOrderSyncRabbitMQConfig.ROUTING_KEY, new TemuOrderSyncMessage(shop.getId()));
        }
    }

    /**
     * 调用 Temu 订单列表查询接口。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应
     */
    private TemuApiResponse<OrderListDto> queryOrderList(OrderManagementOrderListReqVO request) {
        OrderListReqVO orderRequest = new OrderListReqVO();
        orderRequest.setParentOrderStatus(request.getParentOrderStatus());
        orderRequest.setRegionId(request.getRegionId());
        orderRequest.setPageNumber(request.getPageNumber());
        orderRequest.setPageSize(request.getPageSize());
        return createFrameworkClient(request).getOrder().listOrdersV2(orderRequest);
    }

    /**
     * 按店铺最后同步的 Temu 更新时间增量拉取全部订单分页。
     *
     * @param shopId Temu 店铺编号
     */
    @Override
    public void syncShopOrders(Long shopId) {
        TemuShopDO shop = shopMapper.selectById(shopId);
        if (shop == null || !CommonStatusEnum.ENABLE.getStatus().equals(shop.getStatus())) {
            log.warn("[syncShopOrders][shopId({})] 店铺不存在或已停用，跳过订单同步", shopId);
            return;
        }
        if (isBlank(shop.getAuthToken())) {
            log.warn("[syncShopOrders][shopId({})] 店铺未配置授权 Token，跳过订单同步", shopId);
            return;
        }
        if (shop.getRegionId() == null) {
            log.warn("[syncShopOrders][shopId({})] 店铺缺少区域授权信息，跳过订单同步", shop.getId());
            return;
        }
        cn.iocoder.yudao.module.temu.framework.client.TemuClient client = createFrameworkClient(shop);
        LocalDateTime latestUpdateTime = orderMapper.selectLatestTemuUpdateTimeByShopId(shop.getId());
        int pageNumber = 1;
        while (true) {
            TemuApiResponse<OrderListDto> response = client.getOrder().listOrdersV2(
                    buildAutoSyncRequest(shop.getRegionId(), pageNumber, latestUpdateTime));
            if (!Boolean.TRUE.equals(response.getSuccess())) {
                throw new IllegalStateException("Temu 订单列表接口返回失败: " + response);
            }
            List<OrderPageItemDto> pageItems = response.getResult().getPageItems();
            transactionTemplate.executeWithoutResult(status -> syncOrderList(pageItems, shop.getId()));
            if (pageItems == null || pageItems.size() < SYNC_PAGE_SIZE) {
                return;
            }
            pageNumber++;
        }
    }

    /**
     * 构造自动同步的 Temu 请求参数。
     *
     * @param regionId 店铺卖家所属区域
     * @param pageNumber 当前页码
     * @param latestUpdateTime 本地最新 Temu 更新时间；首次同步时为空
     * @return Temu 订单列表请求参数
     */
    private OrderListReqVO buildAutoSyncRequest(Integer regionId, int pageNumber, LocalDateTime latestUpdateTime) {
        OrderListReqVO request = new OrderListReqVO();
        request.setParentOrderStatus(ALL_PARENT_ORDER_STATUS);
        request.setRegionId(regionId.longValue());
        request.setPageNumber(pageNumber);
        request.setPageSize(SYNC_PAGE_SIZE);
        // 首次同步无需传递更新时间，避免错误地筛掉历史订单。
        if (latestUpdateTime != null) {
            request.setUpdateTime(latestUpdateTime.atZone(ZoneId.systemDefault()).toEpochSecond());
        }
        return request;
    }

    /**
     * 分页查询本地已同步订单。
     *
     * @param request 店铺及订单筛选条件
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
    public TemuApiResponse<OrderDetailDto> getOrderDetail(OrderManagementParentOrderReqVO request) {
        return createFrameworkClient(request).getOrder().detailOrderV2(parentOrderRequest(request.getParentOrderSn()));
    }

    /**
     * 调用 Temu 定制订单详情查询接口。
     *
     * @param request 子订单编号列表查询参数
     * @return Temu 官方定制订单详情响应
     */
    @Override
    public TemuApiResponse<CustomizationOrderListDto> getCustomOrderDetail(OrderManagementCustomOrderReqVO request) {
        CustomizationOrderReqVO orderRequest = new CustomizationOrderReqVO();
        orderRequest.setOrderSnList(request.getOrderSnList());
        return createFrameworkClient(request).getOrder().customizationOrder(orderRequest);
    }

    /**
     * 调用 Temu 父订单收货信息查询接口。
     *
     * @param request 父订单查询参数
     * @return Temu 官方收货信息响应
     */
    @Override
    public TemuApiResponse<ShippingInfoDto> getOrderShippingInfo(OrderManagementParentOrderReqVO request) {
        return createFrameworkClient(request).getOrder().shippinginfoOrderV2(parentOrderRequest(request.getParentOrderSn()));
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
     * 按请求站点创建新版 Temu 客户端，供已迁移的订单接口使用。
     *
     * @param request 包含站点与授权 Token 的订单管理请求
     * @return 已按区域配置初始化的新版 Temu 客户端
     */
    private cn.iocoder.yudao.module.temu.framework.client.TemuClient createFrameworkClient(OrderManagementBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new cn.iocoder.yudao.module.temu.framework.client.TemuClient(
                region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.name());
    }

    /**
     * 按店铺保存的站点和授权 Token 创建新版 Temu 客户端，用于异步订单同步。
     *
     * @param shop 已启用的 Temu 店铺
     * @return 已初始化的新版 Temu 客户端
     */
    private cn.iocoder.yudao.module.temu.framework.client.TemuClient createFrameworkClient(TemuShopDO shop) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(shop.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new cn.iocoder.yudao.module.temu.framework.client.TemuClient(
                region.getAppKey(), region.getAppSecret(), shop.getAuthToken(), site.name());
    }

    /**
     * 校验店铺存在，并从受控的店铺授权关系取得卖家编号。
     *
     * <p>卖家编号不从请求参数接收，防止订单被关联到其他店铺的卖家。</p>
     *
     * @param shopId 本地 Temu 店铺编号
     * @return 该店铺的卖家授权信息
     */
    private TemuShopDO validateOrderOwner(Long shopId) {
        TemuShopDO shop = shopMapper.selectById(shopId);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        if (shop.getMallId() == null) {
            throw exception(SELLER_NOT_EXISTS);
        }
        return shop;
    }

    /**
     * 将 Temu 父订单聚合响应转换并幂等保存为子订单记录。
     *
     * @param pageItems Temu 返回的父订单数组
     * @param shopId 归属店铺编号
     */
    private void syncOrderList(List<OrderPageItemDto> pageItems, Long shopId) {
        if (pageItems == null || pageItems.isEmpty()) {
            return;
        }
        for (OrderPageItemDto pageItem : pageItems) {
            ParentOrderDto parent = pageItem.getParentOrderMap();
            if (parent == null || pageItem.getOrderList() == null) {
                continue;
            }
            for (ChildOrderDto child : pageItem.getOrderList()) {
                String orderSn = child.getOrderSn();
                if (isBlank(orderSn)) {
                    continue;
                }
                TemuOrderDO order = buildOrder(parent, child, shopId);
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
     * @param parent Temu 父订单 DTO
     * @param child Temu 子订单 DTO
     * @param shopId 归属店铺编号
     * @return 可持久化的订单记录
     */
    private TemuOrderDO buildOrder(ParentOrderDto parent, ChildOrderDto child, Long shopId) {
        TemuOrderDO order = new TemuOrderDO();
        order.setShopId(shopId);
        order.setParentOrderSn(parent.getParentOrderSn());
        order.setOrderSn(child.getOrderSn());
        order.setSiteId(parent.getSiteId());
        order.setRegionId(parent.getRegionId());
        order.setParentOrderStatus(parent.getParentOrderStatus());
        order.setOrderStatus(child.getOrderStatus());
        order.setParentOrderPaymentType(parent.getOrderPaymentType());
        order.setOrderPaymentType(child.getOrderPaymentType());
        order.setFulfillmentType(child.getFulfillmentType());
        order.setGoodsId(child.getGoodsId());
        order.setSkuId(child.getSkuId());
        order.setGoodsName(child.getGoodsName());
        order.setOriginalGoodsName(child.getOriginalGoodsName());
        order.setSpec(child.getSpec());
        order.setOriginalSpecName(child.getOriginalSpecName());
        order.setThumbUrl(child.getThumbUrl());
        order.setQuantity(child.getQuantity());
        order.setCanceledQuantityBeforeShipment(child.getCanceledQuantityBeforeShipment());
        order.setOriginalOrderQuantity(child.getOriginalOrderQuantity());
        order.setShippingMethod(parent.getShippingMethod());
        order.setShipmentConsolidatedByMainMall(parent.getIsShipmentConsolidatedByMainMall());
        order.setHasShippingFee(parent.getHasShippingFee());
        order.setParentOrderTime(epochSecond(parent.getParentOrderTime()));
        order.setParentConfirmTime(epochSecond(parent.getParentConfirmTime()));
        order.setOrderCreateTime(epochSecond(child.getOrderCreateTime()));
        order.setOrderShippingTime(epochSecond(child.getOrderShippingTime()));
        order.setExpectShipLatestTime(epochSecond(parent.getExpectShipLatestTime()));
        order.setLatestDeliveryTime(epochSecond(parent.getLatestDeliveryTime()));
        order.setTemuUpdateTime(epochSecond(parent.getUpdateTime()));
        order.setParentOrderLabels(json(parent.getParentOrderLabel()));
        order.setOrderLabels(json(child.getOrderLabel()));
        order.setParentFulfillmentWarnings(json(parent.getFulfillmentWarning()));
        order.setFulfillmentWarnings(json(child.getFulfillmentWarning()));
        order.setPackageAbnormalTypes(json(child.getPackageAbnormalTypeList()));
        order.setProductList(json(child.getProductList()));
        order.setLastSyncTime(LocalDateTime.now());
        return order;
    }

    /** 将响应中的数组或对象字段序列化为数据库 JSON 文本。 */
    private String json(Object value) {
        return value == null ? null : JSONUtil.toJsonStr(value);
    }

    /** 将 Temu Unix 秒级时间戳转换为本地时间。 */
    private LocalDateTime epochSecond(Long value) {
        return value == null ? null : Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /** 根据父订单号构造新版订单查询请求。 */
    private ParentOrderReqVO parentOrderRequest(String parentOrderSn) {
        ParentOrderReqVO request = new ParentOrderReqVO();
        request.setParentOrderSn(parentOrderSn);
        return request;
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
