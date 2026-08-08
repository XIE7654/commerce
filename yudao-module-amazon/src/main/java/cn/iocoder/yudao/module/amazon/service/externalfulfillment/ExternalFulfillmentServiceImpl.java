package cn.iocoder.yudao.module.amazon.service.externalfulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.externalfulfillment.vo.ExternalFulfillmentRequestVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/** Amazon External Fulfillment API 服务实现。 */
@Service
public class ExternalFulfillmentServiceImpl implements ExternalFulfillmentService {

    private static final String FULFILLMENT_PATH = "/externalFulfillment/2024-09-11";
    private static final String INVENTORY_PATH = "/externalFulfillment/inventory/2024-09-11/inventories";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> batchInventory(ExternalFulfillmentRequestVO request) {
        requireBody(request, "batchInventory");
        return invoke(request, "batchInventory", HttpMethod.POST, INVENTORY_PATH, false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listReturns(ExternalFulfillmentRequestVO request) {
        return invoke(request, "listReturns", HttpMethod.GET, FULFILLMENT_PATH + "/returns", false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getReturn(ExternalFulfillmentRequestVO request) {
        return invoke(request, "getReturn", HttpMethod.GET,
                FULFILLMENT_PATH + "/returns/" + requiredId(request.getReturnId(), "returnId"), false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getShipments(ExternalFulfillmentRequestVO request) {
        requireQuery(request, "status", "getShipments");
        return invoke(request, "getShipments", HttpMethod.GET, FULFILLMENT_PATH + "/shipments", false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getShipment(ExternalFulfillmentRequestVO request) {
        return invoke(request, "getShipment", HttpMethod.GET, shipmentPath(request), false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> processShipment(ExternalFulfillmentRequestVO request) {
        requireQuery(request, "operation", "processShipment");
        return invoke(request, "processShipment", HttpMethod.POST, shipmentPath(request), true);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createPackages(ExternalFulfillmentRequestVO request) {
        requireBody(request, "createPackages");
        return invoke(request, "createPackages", HttpMethod.POST, shipmentPath(request) + "/packages", true);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> updatePackage(ExternalFulfillmentRequestVO request) {
        requireBody(request, "updatePackage");
        return invoke(request, "updatePackage", HttpMethod.PUT, packagePath(request), true);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> updatePackageStatus(ExternalFulfillmentRequestVO request) {
        return invoke(request, "updatePackageStatus", HttpMethod.PATCH, packagePath(request), true);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> retrieveShippingOptions(ExternalFulfillmentRequestVO request) {
        requireQuery(request, "packageId", "retrieveShippingOptions");
        return invoke(request, "retrieveShippingOptions", HttpMethod.GET, shipmentPath(request) + "/shippingOptions", false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> generateInvoice(ExternalFulfillmentRequestVO request) {
        return invoke(request, "generateInvoice", HttpMethod.POST, shipmentPath(request) + "/invoice", false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> retrieveInvoice(ExternalFulfillmentRequestVO request) {
        return invoke(request, "retrieveInvoice", HttpMethod.GET, shipmentPath(request) + "/invoice", false);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> generateShipLabels(ExternalFulfillmentRequestVO request) {
        requireQuery(request, "operation", "generateShipLabels");
        return invoke(request, "generateShipLabels", HttpMethod.PUT, shipmentPath(request) + "/shipLabels", false);
    }

    /**
     * 执行 External Fulfillment 请求，并将响应按 API 分类归档。
     *
     * @param request 店铺、站点、查询参数和请求体
     * @param operation Amazon 操作名称
     * @param method HTTP 请求方法
     * @param path 不含查询字符串的 API 路径
     * @param allowEmptyResponse 是否允许 Amazon 以 204 表示成功
     * @return Amazon 原始 JSON；允许的 204 响应返回空 Map
     */
    private Map<String, Object> invoke(ExternalFulfillmentRequestVO request, String operation, HttpMethod method,
                                       String path, boolean allowEmptyResponse) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + path + buildQuery(request.getQuery()));
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        if (method == HttpMethod.GET) {
            return amazonSellingPartnerClient.getByCategory(uri, accessToken, AmazonApiCategory.EXTERNAL_FULFILLMENT,
                    operation, "external-fulfillment-" + operation, shop.getId(), request.getCountryCode(),
                    marketplace.getMarketplaceId());
        }
        if (allowEmptyResponse) {
            return amazonSellingPartnerClient.mutateByCategoryOptional(uri, accessToken, method, request.getBody(),
                    AmazonApiCategory.EXTERNAL_FULFILLMENT, operation, "external-fulfillment-" + operation,
                    shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
        }
        return amazonSellingPartnerClient.mutateByCategory(uri, accessToken, method, request.getBody(),
                AmazonApiCategory.EXTERNAL_FULFILLMENT, operation, "external-fulfillment-" + operation,
                shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /** 构造货件资源路径，并验证货件编号为必填字段。 */
    private String shipmentPath(ExternalFulfillmentRequestVO request) {
        return FULFILLMENT_PATH + "/shipments/" + requiredId(request.getShipmentId(), "shipmentId");
    }

    /** 构造包裹资源路径，并验证货件和包裹编号均存在。 */
    private String packagePath(ExternalFulfillmentRequestVO request) {
        return shipmentPath(request) + "/packages/" + requiredId(request.getPackageId(), "packageId");
    }

    /** 验证指定路径参数并进行 RFC 3986 编码，防止编号中的保留字符改变资源路径。 */
    private String requiredId(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " 不能为空");
        }
        return encode(value);
    }

    /** 验证模型要求的请求体存在，避免向 Amazon 提交无法处理的空请求。 */
    private void requireBody(ExternalFulfillmentRequestVO request, String operation) {
        if (request.getBody() == null || request.getBody().isEmpty()) {
            throw new IllegalArgumentException(operation + " 的请求体不能为空");
        }
    }

    /** 验证模型要求的查询字段存在，避免 Amazon 因缺参返回不明确的业务错误。 */
    private void requireQuery(ExternalFulfillmentRequestVO request, String name, String operation) {
        if (request.getQuery() == null || request.getQuery().get(name) == null || request.getQuery().get(name).isBlank()) {
            throw new IllegalArgumentException(operation + " 的查询参数 " + name + " 不能为空");
        }
    }

    /** 将可选查询参数编码为 URI 查询字符串，忽略空值以保持模型默认行为。 */
    private String buildQuery(Map<String, String> query) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        String value = query.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return value.isBlank() ? "" : "?" + value;
    }

    /** 对路径及查询字段执行 UTF-8 百分号编码。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 查询当前租户的店铺，确保授权令牌不会跨租户使用。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** 将国家代码解析为对应的 SP-API 区域端点和 Marketplace。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }
}
