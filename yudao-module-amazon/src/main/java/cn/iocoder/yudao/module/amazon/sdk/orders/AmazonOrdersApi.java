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
     * 查询 Orders v0 资源并归档响应。
     * @param request 包含店铺授权、站点和完整请求地址的 SDK 请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> get(AmazonOrdersRequest request) {
        return response(client.getOrders(uri(request, V0), request.getAccessToken(), request.getOperation(),
                request.getStorage(), request.getShopId(), request.getCountryCode(), request.getMarketplaceId()));
    }

    /**
     * 查询 Orders 2026-01-01 资源并归档响应。
     * @param request 包含店铺授权、站点和完整请求地址的 SDK 请求
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> get2026(AmazonOrdersRequest request) {
        return response(client.getOrders(uri(request, V2026), request.getAccessToken(), request.getOperation(),
                request.getStorage(), request.getShopId(), request.getCountryCode(), request.getMarketplaceId()));
    }

    /**
     * 调用 Orders 写接口并归档响应。
     * @param request 包含店铺授权、请求地址和请求体的 SDK 请求
     * @param method Amazon 要求的 HTTP 方法
     * @return Amazon 原始响应及错误状态
     */
    public AmazonApiResponse<Map<String, Object>> mutate(AmazonOrdersRequest request, HttpMethod method) {
        URI uri = uri(request, V0);
        return response(client.mutateOrders(uri, request.getAccessToken(), method, request.getBody(), request.getOperation(),
                request.getShopId(), request.getCountryCode(), request.getMarketplaceId()));
    }

    private URI uri(AmazonOrdersRequest request, String basePath) {
        if (request == null || request.getShopId() == null) throw new IllegalArgumentException("shopId 不能为空");
        if (request.getUri() != null) return request.getUri();
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
