package cn.iocoder.yudao.module.amazon.service.orders;

import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentConfirmationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderRegulatedInfoUpdateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrder2026GetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrders2026ListReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Amazon Orders 服务实现。
 *
 * <p>订单买家信息和地址由 Amazon 按授权范围脱敏或拒绝访问，服务层只转发其原始响应，不自行缓存敏感业务字段。</p>
 */
@Service
public class AmazonOrdersServiceImpl implements AmazonOrdersService {

    private static final String ORDERS_PATH = "/orders/v0/orders";
    private static final String ORDERS_2026_PATH = "/orders/2026-01-01/orders";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrders(AmazonOrdersListReqVO request) {
        validateListRequest(request);
        return execute(request.getShopId(), request.getCountryCode(), buildListUri(request), "getOrders", "orders");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrder(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "", "getOrder", "order");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderItems(AmazonOrderItemsReqVO request) {
        return executeOrderItemsRequest(request, "/orderItems", "getOrderItems", "order-items");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderItemsBuyerInfo(AmazonOrderItemsReqVO request) {
        return executeOrderItemsRequest(request, "/orderItems/buyerInfo", "getOrderItemsBuyerInfo", "order-items-buyer-info");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderBuyerInfo(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "/buyerInfo", "getOrderBuyerInfo", "order-buyer-info");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderAddress(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "/address", "getOrderAddress", "order-address");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderRegulatedInfo(AmazonOrderGetReqVO request) {
        return executeOrderRequest(request, "/regulatedInfo", "getOrderRegulatedInfo", "order-regulated-info");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> updateShipmentStatus(AmazonOrderShipmentReqVO request) {
        Map<String, Object> body = new TreeMap<>();
        body.put("marketplaceId", requireMarketplace(request.getCountryCode()).getMarketplaceId());
        body.put("shipmentStatus", request.getShipmentStatus());
        if (!isEmpty(request.getOrderItems())) {
            body.put("orderItems", request.getOrderItems());
        }
        return executeMutation(request, "/shipment", HttpMethod.POST, body, "updateShipmentStatus");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> confirmShipment(AmazonOrderShipmentConfirmationReqVO request) {
        Map<String, Object> body = new TreeMap<>();
        body.put("marketplaceId", requireMarketplace(request.getCountryCode()).getMarketplaceId());
        body.put("packageDetail", request.getPackageDetail());
        if (!isBlank(request.getCodCollectionMethod())) {
            body.put("codCollectionMethod", request.getCodCollectionMethod());
        }
        return executeMutation(request, "/shipmentConfirmation", HttpMethod.POST, body, "confirmShipment");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> updateOrderRegulatedInfo(AmazonOrderRegulatedInfoUpdateReqVO request) {
        return executeMutation(request, "/regulatedInfo", HttpMethod.PATCH,
                Map.of("regulatedOrderVerificationStatus", request.getRegulatedOrderVerificationStatus()),
                "updateVerificationStatus");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrders2026(AmazonOrders2026ListReqVO request) {
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, String> query = new TreeMap<>();
        put(query, "createdAfter", request.getCreatedAfter());
        put(query, "createdBefore", request.getCreatedBefore());
        put(query, "lastUpdatedAfter", request.getLastUpdatedAfter());
        put(query, "lastUpdatedBefore", request.getLastUpdatedBefore());
        put(query, "fulfillmentStatuses", join(request.getFulfillmentStatuses()));
        put(query, "marketplaceIds", marketplace.getMarketplaceId());
        put(query, "fulfilledBy", join(request.getFulfilledBy()));
        put(query, "maxResultsPerPage", request.getMaxResultsPerPage() == null ? null : request.getMaxResultsPerPage().toString());
        put(query, "paginationToken", request.getPaginationToken());
        put(query, "includedData", join(request.getIncludedData()));
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_2026_PATH + "?" + buildQuery(query));
        return execute(request.getShopId(), request.getCountryCode(), uri, "getOrders2026", "orders-2026");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrder2026(AmazonOrder2026GetReqVO request) {
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        Map<String, String> query = new TreeMap<>();
        put(query, "includedData", join(request.getIncludedData()));
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_2026_PATH + "/" + orderId + "?" + buildQuery(query));
        return execute(request.getShopId(), request.getCountryCode(), uri, "getOrder2026", "order-2026");
    }

    /**
     * 调用指定订单子资源，确保所有订单详情接口使用相同的店铺授权和站点解析规则。
     *
     * @param request 店铺、国家代码和 Amazon 订单编号
     * @param suffix 订单资源路径后缀
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> executeOrderRequest(AmazonOrderGetReqVO request, String suffix, String operationName,
                                                    String storageName) {
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_PATH + "/" + orderId + suffix);
        return execute(request.getShopId(), request.getCountryCode(), uri, operationName, storageName);
    }

    /**
     * 调用订单商品资源并传递 Amazon 返回的分页令牌。
     *
     * @param request 订单商品查询参数
     * @param suffix 订单资源路径后缀
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> executeOrderItemsRequest(AmazonOrderItemsReqVO request, String suffix, String operationName,
                                                         String storageName) {
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        Map<String, String> query = new TreeMap<>();
        put(query, "NextToken", request.getNextToken());
        String queryString = query.isEmpty() ? "" : "?" + buildQuery(query);
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_PATH + "/" + orderId + suffix + queryString);
        return execute(request.getShopId(), request.getCountryCode(), uri, operationName, storageName);
    }

    /**
     * 调用指定订单写接口，并复用店铺授权、站点解析和审计归档链路。
     *
     * @param request 订单及站点定位参数
     * @param suffix 订单资源路径后缀
     * @param method HTTP 请求方式
     * @param body Amazon 请求体
     * @param operationName Amazon 操作名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> executeMutation(AmazonOrderGetReqVO request, String suffix, HttpMethod method,
                                                Map<String, Object> body, String operationName) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String orderId = UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8);
        URI uri = URI.create(marketplace.getEndpoint() + ORDERS_PATH + "/" + orderId + suffix);
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        return amazonSellingPartnerClient.mutateOrders(uri, accessToken, method, body, operationName, shop.getId(),
                request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 使用店铺 Seller Token 调用 Orders API，并将响应交由统一客户端审计和归档。
     *
     * @param shopId 当前租户的店铺编号
     * @param countryCode 站点国家代码
     * @param uri 已构造的 Orders API 地址
     * @param operationName Amazon 操作名称
     * @param storageName 响应归档文件名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> execute(Long shopId, String countryCode, URI uri, String operationName, String storageName) {
        AmazonShopDO shop = requireShop(shopId);
        AmazonMarketplaceEnum marketplace = requireMarketplace(countryCode);
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        return amazonSellingPartnerClient.getOrders(uri, accessToken, operationName, storageName, shop.getId(), countryCode,
                marketplace.getMarketplaceId());
    }

    /**
     * 构造订单列表 URI；Amazon 要求以站点 Marketplace ID 作为必传筛选条件。
     *
     * @param request 订单列表请求参数
     * @return 可直接发起请求的 URI
     */
    private URI buildListUri(AmazonOrdersListReqVO request) {
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, String> query = new TreeMap<>();
        query.put("MarketplaceIds", marketplace.getMarketplaceId());
        put(query, "CreatedAfter", request.getCreatedAfter());
        put(query, "CreatedBefore", request.getCreatedBefore());
        put(query, "LastUpdatedAfter", request.getLastUpdatedAfter());
        put(query, "LastUpdatedBefore", request.getLastUpdatedBefore());
        put(query, "OrderStatuses", join(request.getOrderStatuses()));
        put(query, "FulfillmentChannels", join(request.getFulfillmentChannels()));
        put(query, "PaymentMethods", join(request.getPaymentMethods()));
        put(query, "BuyerEmail", request.getBuyerEmail());
        put(query, "SellerOrderId", request.getSellerOrderId());
        put(query, "MaxResultsPerPage", request.getMaxResultsPerPage() == null ? null : request.getMaxResultsPerPage().toString());
        put(query, "EasyShipShipmentStatuses", join(request.getEasyShipShipmentStatuses()));
        put(query, "ElectronicInvoiceStatuses", join(request.getElectronicInvoiceStatuses()));
        put(query, "NextToken", request.getNextToken());
        put(query, "AmazonOrderIds", join(request.getAmazonOrderIds()));
        put(query, "ActualFulfillmentSupplySourceId", request.getActualFulfillmentSupplySourceId());
        put(query, "IsISPU", request.getIsISPU() == null ? null : request.getIsISPU().toString());
        put(query, "StoreChainStoreId", request.getStoreChainStoreId());
        put(query, "EarliestDeliveryDateBefore", request.getEarliestDeliveryDateBefore());
        put(query, "EarliestDeliveryDateAfter", request.getEarliestDeliveryDateAfter());
        put(query, "LatestDeliveryDateBefore", request.getLatestDeliveryDateBefore());
        put(query, "LatestDeliveryDateAfter", request.getLatestDeliveryDateAfter());
        return URI.create(marketplace.getEndpoint() + ORDERS_PATH + "?" + buildQuery(query));
    }

    /**
     * 校验 Orders API 的时间筛选规则，避免 Amazon 忽略互斥条件造成查询范围偏差。
     *
     * @param request 订单列表请求参数
     */
    private void validateListRequest(AmazonOrdersListReqVO request) {
        if (!isBlank(request.getNextToken())) {
            return; // 分页令牌存在时，Amazon 只使用令牌继续读取，其他筛选条件不参与本次查询。
        }
        if (isBlank(request.getCreatedAfter()) && isBlank(request.getLastUpdatedAfter())) {
            throw new IllegalArgumentException("CreatedAfter 或 LastUpdatedAfter 必须传入一个");
        }
        if (!isBlank(request.getCreatedAfter()) && (!isBlank(request.getLastUpdatedAfter()) || !isBlank(request.getLastUpdatedBefore()))) {
            throw new IllegalArgumentException("CreatedAfter 不能与 LastUpdatedAfter 或 LastUpdatedBefore 同时传入");
        }
        validateDateTimes(request.getCreatedAfter(), request.getCreatedBefore(), "CreatedAfter", "CreatedBefore");
        validateDateTimes(request.getLastUpdatedAfter(), request.getLastUpdatedBefore(), "LastUpdatedAfter", "LastUpdatedBefore");
        validateDateTimes(request.getEarliestDeliveryDateAfter(), request.getEarliestDeliveryDateBefore(),
                "EarliestDeliveryDateAfter", "EarliestDeliveryDateBefore");
        validateDateTimes(request.getLatestDeliveryDateAfter(), request.getLatestDeliveryDateBefore(),
                "LatestDeliveryDateAfter", "LatestDeliveryDateBefore");
        if (!isBlank(request.getSellerOrderId()) && (!isEmpty(request.getFulfillmentChannels())
                || !isEmpty(request.getOrderStatuses()) || !isEmpty(request.getPaymentMethods())
                || !isBlank(request.getLastUpdatedAfter()) || !isBlank(request.getLastUpdatedBefore())
                || !isBlank(request.getBuyerEmail()))) {
            throw new IllegalArgumentException("SellerOrderId 不能与履行渠道、订单状态、支付方式、更新时间或买家邮箱同时传入");
        }
    }

    /**
     * 验证 ISO 8601 时间区间，确保时间格式和上下限关系可被 Amazon 正确处理。
     *
     * @param after 下限时间
     * @param before 上限时间
     * @param afterName 下限参数名
     * @param beforeName 上限参数名
     */
    private void validateDateTimes(String after, String before, String afterName, String beforeName) {
        OffsetDateTime afterTime = parseDateTime(after, afterName);
        OffsetDateTime beforeTime = parseDateTime(before, beforeName);
        if (afterTime != null && beforeTime != null && beforeTime.isBefore(afterTime)) {
            throw new IllegalArgumentException(beforeName + " 不能早于 " + afterName);
        }
    }

    /**
     * 解析可选 ISO 8601 时间。
     *
     * @param value 待解析时间
     * @param name 参数名称
     * @return 解析后的时间；空值返回 {@code null}
     */
    private OffsetDateTime parseDateTime(String value, String name) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(name + " 必须为 ISO 8601 日期时间格式", exception);
        }
    }

    /**
     * 按 RFC 3986 编码并排序查询参数。
     *
     * @param query 待编码的查询参数
     * @return URI 查询字符串
     */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /**
     * 使用 UTF-8 对查询参数进行 RFC 3986 百分号编码。
     *
     * @param value 待编码的参数值
     * @return 编码后的参数值
     */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /**
     * 仅在值非空时写入可选查询参数。
     *
     * @param query 待写入的查询参数集合
     * @param key 参数名称
     * @param value 参数值
     */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /**
     * 将字符串列表转换为 Amazon 要求的逗号分隔参数。
     *
     * @param values 参数值列表
     * @return 逗号分隔值；空列表返回 {@code null}
     */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /**
     * 查询当前租户下的 Amazon 店铺。
     *
     * @param shopId 店铺编号
     * @return 当前租户的店铺授权信息
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 解析国家代码对应的 Amazon Marketplace。
     *
     * @param countryCode 国家代码
     * @return 目标站点配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /**
     * 判断字符串是否为空白。
     *
     * @param value 待判断字符串
     * @return 字符串为空或仅为空白字符时为 {@code true}
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * 判断集合是否为空。
     *
     * @param values 待判断集合
     * @return 集合为 {@code null} 或无元素时为 {@code true}
     */
    private boolean isEmpty(List<?> values) {
        return values == null || values.isEmpty();
    }

}
