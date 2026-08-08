package cn.iocoder.yudao.module.amazon.service.invoices;

import cn.iocoder.yudao.module.amazon.controller.admin.invoices.vo.InvoicesRequestVO;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Amazon Invoices API 服务实现。 */
@Service
public class InvoicesServiceImpl implements InvoicesService {

    private static final String API_PREFIX = "/tax/invoices/2024-06-19";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoicesAttributes(InvoicesRequestVO request) {
        return get(request, "/attributes", marketplaceQuery(request), "getInvoicesAttributes", "attributes");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoicesDocument(InvoicesRequestVO request) {
        return get(request, "/documents/" + pathValue(request.getInvoicesDocumentId(), "invoicesDocumentId"), Map.of(),
                "getInvoicesDocument", "document");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createInvoicesExport(InvoicesRequestVO request) {
        requireBodyFields(request.getExportBody(), "exportBody", "marketplaceId");
        return mutate(request, "/exports", request.getExportBody(), "createInvoicesExport", "export");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoicesExports(InvoicesRequestVO request) {
        Map<String, String> query = marketplaceQuery(request);
        put(query, "dateStart", request.getDateStart());
        put(query, "dateEnd", request.getDateEnd());
        put(query, "nextToken", request.getNextToken());
        put(query, "pageSize", request.getPageSize());
        put(query, "status", request.getStatuses());
        return get(request, "/exports", query, "getInvoicesExports", "exports");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoicesExport(InvoicesRequestVO request) {
        return get(request, "/exports/" + pathValue(request.getExportId(), "exportId"), Map.of(),
                "getInvoicesExport", "export");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> createGovernmentInvoice(InvoicesRequestVO request) {
        requireBodyFields(request.getGovernmentInvoiceBody(), "governmentInvoiceBody", "marketplaceId", "shipmentId",
                "invoiceType", "transactionType");
        return mutate(request, "/governmentInvoiceRequests", request.getGovernmentInvoiceBody(),
                "createGovernmentInvoice", "government-invoice");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getGovernmentInvoiceStatus(InvoicesRequestVO request) {
        return get(request, "/governmentInvoiceRequests", governmentInvoiceQuery(request, false),
                "getGovernmentInvoiceStatus", "government-invoice-status");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getGovernmentInvoiceDocument(InvoicesRequestVO request) {
        return get(request, "/governmentInvoiceRequests/" + pathValue(request.getShipmentId(), "shipmentId"),
                governmentInvoiceQuery(request, true), "getGovernmentInvoiceDocument", "government-invoice-document");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoices(InvoicesRequestVO request) {
        Map<String, String> query = marketplaceQuery(request);
        put(query, "transactionIdentifierName", request.getTransactionIdentifierName());
        put(query, "transactionIdentifierId", request.getTransactionIdentifierId());
        put(query, "transactionType", request.getTransactionType());
        put(query, "invoiceType", request.getInvoiceType());
        put(query, "statuses", request.getStatuses());
        put(query, "externalInvoiceId", request.getExternalInvoiceId());
        put(query, "series", request.getSeries());
        put(query, "dateStart", request.getDateStart());
        put(query, "dateEnd", request.getDateEnd());
        put(query, "sortBy", request.getSortBy());
        put(query, "sortOrder", request.getSortOrder());
        put(query, "pageSize", request.getPageSize());
        put(query, "nextToken", request.getNextToken());
        return get(request, "/invoices", query, "getInvoices", "invoices");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoice(InvoicesRequestVO request) {
        return get(request, "/invoices/" + pathValue(request.getInvoiceId(), "invoiceId"), marketplaceQuery(request),
                "getInvoice", "invoice");
    }

    /** 调用只读 Invoices 接口，并使用当前租户店铺令牌归档响应。 */
    private Map<String, Object> get(InvoicesRequestVO request, String path, Map<String, String> query,
                                    String operation, String storageName) {
        RequestContext context = context(request);
        return amazonSellingPartnerClient.getByCategory(uri(context.marketplace(), path, query), context.accessToken(),
                AmazonApiCategory.INVOICING, operation, storageName, context.shopId(), request.getCountryCode(),
                isBlank(request.getMarketplaceId()) ? context.marketplace().getMarketplaceId() : request.getMarketplaceId().trim());
    }

    /** 调用创建 Invoices 资源的接口；请求体会写入 API 审计日志。 */
    private Map<String, Object> mutate(InvoicesRequestVO request, String path, Map<String, Object> body,
                                       String operation, String storageName) {
        RequestContext context = context(request);
        return amazonSellingPartnerClient.mutateByCategory(uri(context.marketplace(), path, Map.of()), context.accessToken(),
                HttpMethod.POST, body, AmazonApiCategory.INVOICING, operation, storageName, context.shopId(),
                request.getCountryCode(), marketplaceIdForBody(body));
    }

    /** 生成 Marketplace 必填的查询参数，确保 API 不会隐式使用错误站点。 */
    private Map<String, String> marketplaceQuery(InvoicesRequestVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceId", required(request.getMarketplaceId(), "marketplaceId"));
        return query;
    }

    /** 组装政府发票查询所需的共同条件，并在请求状态时补充 shipmentId。 */
    private Map<String, String> governmentInvoiceQuery(InvoicesRequestVO request, boolean shipmentInPath) {
        Map<String, String> query = marketplaceQuery(request);
        put(query, "transactionType", required(request.getTransactionType(), "transactionType"));
        put(query, "invoiceType", required(request.getInvoiceType(), "invoiceType"));
        if (!shipmentInPath) {
            put(query, "shipmentId", required(request.getShipmentId(), "shipmentId"));
        }
        put(query, "inboundPlanId", request.getInboundPlanId());
        put(query, "fileFormat", request.getFileFormat());
        return query;
    }

    /** 解析租户内店铺、授权令牌和端点，防止跨租户使用卖家授权。 */
    private RequestContext context(InvoicesRequestVO request) {
        AmazonShopDO shop = amazonShopMapper.selectById(request.getShopId());
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + request.getShopId());
        }
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(request.getCountryCode());
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + request.getCountryCode());
        }
        return new RequestContext(shop.getId(), amazonOAuthService.getSellerAccessToken(shop.getId()), marketplace);
    }

    /** 将 API 路径及非空查询参数编码为完整 URI，保留 Amazon 参数名称大小写。 */
    private URI uri(AmazonMarketplaceEnum marketplace, String path, Map<String, String> query) {
        String queryString = query.entrySet().stream()
                .filter(entry -> !isBlank(entry.getValue()))
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return URI.create(marketplace.getEndpoint() + API_PREFIX + path
                + (queryString.isEmpty() ? "" : "?" + queryString));
    }

    /** 校验创建请求体的必填字段，提前返回清晰错误而非 Amazon 的通用 400。 */
    private void requireBodyFields(Map<String, Object> body, String bodyName, String... fields) {
        if (body == null || body.isEmpty()) {
            throw new IllegalArgumentException(bodyName + " 不能为空");
        }
        for (String field : fields) {
            if (isBlank(value(body.get(field)))) {
                throw new IllegalArgumentException(bodyName + " 必须包含 " + field);
            }
        }
    }

    /** 从创建请求体中读取 Marketplace，用于记录实际调用的业务站点。 */
    private String marketplaceIdForBody(Map<String, Object> body) {
        return required(value(body.get("marketplaceId")), "请求体.marketplaceId");
    }

    /** 将非空字符串加入查询参数。 */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value.trim());
        }
    }

    /** 将整数查询参数转换为字符串。 */
    private void put(Map<String, String> query, String key, Integer value) {
        if (value != null) {
            query.put(key, value.toString());
        }
    }

    /** 将列表按 OpenAPI 默认 csv 格式写入查询参数。 */
    private void put(Map<String, String> query, String key, List<String> values) {
        if (values != null && !values.isEmpty()) {
            String value = values.stream().filter(item -> !isBlank(item)).collect(Collectors.joining(","));
            if (!value.isBlank()) {
                query.put(key, value);
            }
        }
    }

    /** 校验必填文本参数。 */
    private String required(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " 不能为空");
        }
        return value.trim();
    }

    /** 校验并编码路径变量，避免保留字符改变资源路径。 */
    private String pathValue(String value, String fieldName) {
        return encode(required(value, fieldName));
    }

    /** 对路径和查询参数执行 UTF-8 百分号编码。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 以字符串形式读取请求体字段，用于兼容 JSON 值类型。 */
    private String value(Object value) {
        return value == null ? null : value.toString();
    }

    /** 判断值是否为空白。 */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** Invoices 调用所需的店铺、令牌和区域端点上下文。 */
    private record RequestContext(Long shopId, String accessToken, AmazonMarketplaceEnum marketplace) {
    }
}
