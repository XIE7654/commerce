package cn.iocoder.yudao.module.amazon.service.finances;

import cn.iocoder.yudao.module.amazon.controller.admin.finances.vo.AmazonFinancesReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonMarketplaceProvider;
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

/** Amazon Finances 服务实现。 */
@Service
public class AmazonFinancesServiceImpl implements AmazonFinancesService {

    private static final String FINANCES_V0 = "/finances/v0";
    private static final String FINANCES_2024 = "/finances/2024-06-19";
    private static final String TRANSFERS_2024 = "/finances/transfers/2024-06-01";
    private static final String INVOICES_2026 = "/finances/invoices/2026-06-25";

    @Resource
    private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listTransactions(AmazonFinancesReqVO request) {
        requirePair(request.getRelatedIdentifierName(), request.getRelatedIdentifierValue(), "relatedIdentifierName", "relatedIdentifierValue");
        Map<String, String> query = commonTransactionQuery(request);
        put(query, "marketplaceId", request.getMarketplaceId());
        put(query, "transactionStatus", request.getTransactionStatus());
        return get(request, FINANCES_2024 + "/transactions", query, "listTransactions", "transactions");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listBalances(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceIds", join(request.getMarketplaceIds()));
        put(query, "balanceType", request.getBalanceType());
        put(query, "accountType", request.getAccountType());
        put(query, "asOfDate", request.getAsOfDate());
        put(query, "nextToken", request.getNextToken());
        return get(request, FINANCES_2024 + "/balances", query, "listBalances", "balances");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listSummary(AmazonFinancesReqVO request) {
        requirePair(request.getRelatedIdentifierName(), request.getRelatedIdentifierValue(), "relatedIdentifierName", "relatedIdentifierValue");
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceIds", join(request.getMarketplaceIds()));
        put(query, "accountType", request.getAccountType());
        put(query, "relatedIdentifierName", request.getRelatedIdentifierName());
        put(query, "relatedIdentifierValue", request.getRelatedIdentifierValue());
        put(query, "periodStart", request.getPeriodStart());
        put(query, "periodEnd", request.getPeriodEnd());
        put(query, "nextToken", request.getNextToken());
        return get(request, FINANCES_2024 + "/summary", query, "listSummary", "summary");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listFinancialEventGroups(AmazonFinancesReqVO request) {
        Map<String, String> query = v0Query(request);
        put(query, "FinancialEventGroupStartedBefore", request.getFinancialEventGroupStartedBefore());
        put(query, "FinancialEventGroupStartedAfter", request.getFinancialEventGroupStartedAfter());
        return get(request, FINANCES_V0 + "/financialEventGroups", query, "listFinancialEventGroups", "financial-event-groups");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listFinancialEventsByGroupId(AmazonFinancesReqVO request) {
        String eventGroupId = requireText(request.getEventGroupId(), "eventGroupId");
        return get(request, FINANCES_V0 + "/financialEventGroups/" + encode(eventGroupId) + "/financialEvents",
                v0FinancialEventsQuery(request), "listFinancialEventsByGroupId", "financial-events-by-group");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listFinancialEventsByOrderId(AmazonFinancesReqVO request) {
        String orderId = requireText(request.getOrderId(), "orderId");
        return get(request, FINANCES_V0 + "/orders/" + encode(orderId) + "/financialEvents", v0Query(request),
                "listFinancialEventsByOrderId", "financial-events-by-order");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listFinancialEvents(AmazonFinancesReqVO request) {
        return get(request, FINANCES_V0 + "/financialEvents", v0FinancialEventsQuery(request), "listFinancialEvents", "financial-events");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> initiatePayout(AmazonFinancesReqVO request) {
        Map<String, Object> body = request.getPayoutBody();
        if (body == null || isBlank(asString(body.get("marketplaceId"))) || isBlank(asString(body.get("accountType")))) {
            throw new IllegalArgumentException("payoutBody 必须包含 marketplaceId 和 accountType");
        }
        return mutate(request, TRANSFERS_2024 + "/payouts", body, "initiatePayout", "payout");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listPayouts(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceIds", join(request.getMarketplaceIds()));
        put(query, "createdAfter", request.getCreatedAfter());
        put(query, "createdBefore", request.getCreatedBefore());
        put(query, "payoutId", request.getPayoutId());
        put(query, "accountType", request.getAccountType());
        put(query, "nextToken", request.getNextToken());
        return get(request, TRANSFERS_2024 + "/payouts", query, "listPayouts", "payouts");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getPaymentMethods(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceId", requireText(request.getMarketplaceId(), "marketplaceId"));
        put(query, "paymentMethodTypes", join(request.getPaymentMethodTypes()));
        return get(request, TRANSFERS_2024 + "/paymentMethods", query, "getPaymentMethods", "payment-methods");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listExpectedPayouts(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceIds", join(request.getMarketplaceIds()));
        put(query, "accountType", request.getAccountType());
        put(query, "nextToken", request.getNextToken());
        return get(request, TRANSFERS_2024 + "/payouts/expected", query, "listExpectedPayouts", "expected-payouts");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoiceHeaders(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "nextToken", request.getNextToken());
        put(query, "marketplaceId", requireText(request.getMarketplaceId(), "marketplaceId"));
        put(query, "fromIssueDate", request.getFromIssueDate());
        put(query, "toIssueDate", request.getToIssueDate());
        put(query, "invoicesModifiedAfter", request.getInvoicesModifiedAfter());
        return get(request, INVOICES_2026 + "/invoices", query, "getInvoiceHeaders", "invoice-headers");
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInvoice(AmazonFinancesReqVO request) {
        String invoiceIdentifier = requireText(request.getInvoiceIdentifier(), "invoiceIdentifier");
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "marketplaceId", requireText(request.getMarketplaceId(), "marketplaceId"));
        put(query, "nextTokenForLineItems", request.getNextTokenForLineItems());
        return get(request, INVOICES_2026 + "/invoices/" + encode(invoiceIdentifier), query, "getInvoice", "invoice");
    }

    /** 构造 2024 交易接口共用的筛选参数。 */
    private Map<String, String> commonTransactionQuery(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        put(query, "postedAfter", request.getPostedAfter());
        put(query, "postedBefore", request.getPostedBefore());
        put(query, "relatedIdentifierName", request.getRelatedIdentifierName());
        put(query, "relatedIdentifierValue", request.getRelatedIdentifierValue());
        put(query, "nextToken", request.getNextToken());
        return query;
    }

    /** 构造 Finances v0 的通用分页参数。 */
    private Map<String, String> v0Query(AmazonFinancesReqVO request) {
        Map<String, String> query = new LinkedHashMap<>();
        if (request.getMaxResultsPerPage() != null) {
            put(query, "MaxResultsPerPage", request.getMaxResultsPerPage().toString());
        }
        put(query, "NextToken", request.getNextToken());
        return query;
    }

    /** 构造 Finances v0 财务事件接口的时间和分页参数。 */
    private Map<String, String> v0FinancialEventsQuery(AmazonFinancesReqVO request) {
        Map<String, String> query = v0Query(request);
        put(query, "PostedAfter", request.getPostedAfter());
        put(query, "PostedBefore", request.getPostedBefore());
        return query;
    }

    /** 调用 Finances 只读接口，使用当前租户的店铺令牌并归档响应。 */
    private Map<String, Object> get(AmazonFinancesReqVO request, String path, Map<String, String> query,
                                    String operationName, String storageName) {
        RequestContext context = context(request);
        return amazonSellingPartnerClient.getByCategory(uri(context.marketplace(), path, query), context.accessToken(),
                AmazonApiCategory.FINANCES, operationName, storageName, context.shopId(), request.getCountryCode(),
                context.marketplace().getMarketplaceId());
    }

    /** 调用 Finances 写接口，付款请求体会随审计记录保存。 */
    private Map<String, Object> mutate(AmazonFinancesReqVO request, String path, Map<String, Object> body,
                                       String operationName, String storageName) {
        RequestContext context = context(request);
        return amazonSellingPartnerClient.mutateByCategory(uri(context.marketplace(), path, Map.of()), context.accessToken(),
                HttpMethod.POST, body, AmazonApiCategory.FINANCES, operationName, storageName, context.shopId(),
                request.getCountryCode(), context.marketplace().getMarketplaceId());
    }

    /** 解析当前租户的店铺、授权令牌和区域端点，避免跨租户访问店铺凭据。 */
    private RequestContext context(AmazonFinancesReqVO request) {
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

    /** 根据端点、路径和非空筛选参数生成已编码 URI。 */
    private URI uri(AmazonMarketplaceEnum marketplace, String path, Map<String, String> query) {
        String queryString = query.entrySet().stream().map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(java.util.stream.Collectors.joining("&"));
        return URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + path + (queryString.isEmpty() ? "" : "?" + queryString));
    }

    /** 将可选字符串加入查询参数。 */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /** 将列表转换为 Finances API 要求的逗号分隔查询参数。 */
    private String join(List<String> values) {
        return values == null || values.isEmpty() ? null : String.join(",", values);
    }

    /** 校验成对筛选参数，避免 Amazon 忽略不完整的关联条件。 */
    private void requirePair(String left, String right, String leftName, String rightName) {
        if (isBlank(left) != isBlank(right)) {
            throw new IllegalArgumentException(leftName + " 与 " + rightName + " 必须同时传入");
        }
    }

    /** 校验必填文本参数并返回去除首尾空白后的值。 */
    private String requireText(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " 不能为空");
        }
        return value.trim();
    }

    /** 对 URL 路径和查询参数进行 UTF-8 百分号编码。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 将付款请求体的字段值安全转换为字符串以进行必填校验。 */
    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    /** 判断字符串是否为空白。 */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** Finances 请求调用所需的店铺、令牌和端点上下文。 */
    private record RequestContext(Long shopId, String accessToken, AmazonMarketplaceEnum marketplace) {
    }

}
