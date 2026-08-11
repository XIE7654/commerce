package cn.iocoder.yudao.module.amazon.sdk.orders;

import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getList;

/** Amazon Seller Orders API SDK，统一构造路径、查询参数及审计调用。 */
@Component
public class AmazonOrdersApi {
    private static final String V0 = "/orders/v0/orders";
    private static final String V2026 = "/orders/2026-01-01/orders";
    @Resource
    private AmazonSellingPartnerClient client;

    /**
     * 查询 Orders v0 订单列表。
     *
     * @param request 已填充店铺授权和列表筛选条件的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrders(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 查询 Orders v0 指定订单。
     *
     * @param request 已填充订单编号的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrder(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 查询 Orders v0 指定订单的买家信息。
     *
     * @param request 已填充订单编号的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrderBuyerInfo(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 查询 Orders v0 指定订单的收货地址。
     *
     * @param request 已填充订单编号的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrderAddress(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 查询 Orders v0 指定订单的商品。
     *
     * @param request 已填充订单编号和分页令牌的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrderItems(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 查询 Orders v0 指定订单商品的买家信息。
     *
     * @param request 已填充订单编号和分页令牌的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrderItemsBuyerInfo(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 查询 Orders v0 受监管订单信息。
     *
     * @param request 已填充订单编号的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrderRegulatedInfo(AmazonOrdersRequest request) {
        return get(request, V0);
    }

    /**
     * 更新 Orders v0 Easy Ship 发货状态。
     *
     * @param request 已填充订单编号与发货状态请求体的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> updateShipmentStatus(AmazonOrdersRequest request) {
        return mutate(request, HttpMethod.POST);
    }

    /**
     * 更新 Orders v0 受监管订单验证状态。
     *
     * @param request 已填充订单编号与验证状态请求体的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> updateVerificationStatus(AmazonOrdersRequest request) {
        return mutate(request, HttpMethod.PATCH);
    }

    /**
     * 确认 Orders v0 卖家自配送订单发货。
     *
     * @param request 已填充订单编号与包裹请求体的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> confirmShipment(AmazonOrdersRequest request) {
        return mutate(request, HttpMethod.POST);
    }

    /**
     * 查询 Orders 2026-01-01 订单列表。
     *
     * @param request 已填充店铺授权和列表筛选条件的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> searchOrders(AmazonOrdersRequest request) {
        return get(request, V2026);
    }

    /**
     * 查询 Orders 2026-01-01 指定订单。
     *
     * @param request 已填充订单编号和返回数据集的请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> getOrder2026(AmazonOrdersRequest request) {
        return get(request, V2026);
    }

    /**
     * 调用 Orders 读接口并归档响应。
     *
     * @param request 包含店铺授权、路径和查询参数的 SDK 请求
     * @param basePath Orders API 版本基础路径
     * @return Amazon 原始响应及错误状态
     */
    private AmazonApiResponse<Map<String, Object>> get(AmazonOrdersRequest request, String basePath) {
        return response(client.getOrders(uri(request, basePath), request.getAccessToken(), request.getOperation(),
                request.getStorage(), request.getShopId(), request.getCountryCode(), request.getMarketplaceId()));
    }

    /**
     * 调用 Orders v0 写接口并归档响应。
     *
     * @param request 包含店铺授权、路径和请求体的 SDK 请求
     * @param method Amazon 要求的 HTTP 方法
     * @return Amazon 原始响应及错误状态
     */
    private AmazonApiResponse<Map<String, Object>> mutate(AmazonOrdersRequest request, HttpMethod method) {
        URI uri = uri(request, V0);
        return response(client.mutateOrders(uri, request.getAccessToken(), method, request.getBody(), request.getOperation(),
                request.getShopId(), request.getCountryCode(), request.getMarketplaceId()));
    }

    /**
     * 根据 SDK 请求字段构造 Amazon URI，并保留显式传入的完整 URI 以兼容已有调用方。
     *
     * @param request 已填充上下文的 SDK 请求
     * @param basePath Orders API 版本基础路径
     * @return 可直接发送的 Amazon URI
     */
    private URI uri(AmazonOrdersRequest request, String basePath) {
        if (request == null || request.getShopId() == null) throw new IllegalArgumentException("shopId 不能为空");
        if (request.getUri() != null) return request.getUri();
        if (request.getEndpoint() == null || request.getEndpoint().isBlank()) {
            throw new IllegalArgumentException("Orders API endpoint 不能为空");
        }
        StringBuilder path = new StringBuilder(basePath);
        if (request.getOrderId() != null) {
            path.append('/').append(UriUtils.encodePathSegment(request.getOrderId(), StandardCharsets.UTF_8));
        }
        if (request.getPath() != null) path.append(request.getPath());
        Map<String, String> query = request.getQuery() == null ? Map.of() : new TreeMap<>(request.getQuery());
        String suffix = query.isEmpty() ? "" : "?" + AmazonQueryUtils.buildQuery(query);
        return URI.create(request.getEndpoint() + path + suffix);
    }

    private AmazonApiResponse<Map<String, Object>> response(Map<String, Object> raw) {
        List<?> errors = getList(raw, "errors");
        return new AmazonApiResponse<>(errors.isEmpty() ? 200 : 400, raw, errors.isEmpty() ? null : String.valueOf(errors.get(0)));
    }
}
