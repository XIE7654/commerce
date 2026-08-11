package cn.iocoder.yudao.module.amazon.service.sellerwallet;

import cn.iocoder.yudao.module.amazon.controller.admin.sellerwallet.vo.AmazonSellerWalletReqVO;
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
import org.springframework.web.util.UriUtils;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Amazon Seller Wallet 服务实现。 */
@Service
public class AmazonSellerWalletServiceImpl implements AmazonSellerWalletService {
    private static final String PATH = "/finances/transfers/wallet/2024-03-01";
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */ @Override public Map<String, Object> listAccounts(AmazonSellerWalletReqVO request) { return get(request, "/accounts", Map.of(), "listAccounts", "accounts"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getAccount(AmazonSellerWalletReqVO request) { return get(request, "/accounts/" + id(request.getAccountId(), "accountId"), Map.of(), "getAccount", "account"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> listAccountBalances(AmazonSellerWalletReqVO request) { return get(request, "/accounts/" + id(request.getAccountId(), "accountId") + "/balance", Map.of(), "listAccountBalances", "account-balances"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getTransferPreview(AmazonSellerWalletReqVO request) {
        require(request.getSourceCountryCode(), "sourceCountryCode"); require(request.getSourceCurrencyCode(), "sourceCurrencyCode"); require(request.getDestinationCountryCode(), "destinationCountryCode"); require(request.getDestinationCurrencyCode(), "destinationCurrencyCode"); require(request.getBaseAmount(), "baseAmount");
        return get(request, "/transferPreview", Map.of("sourceCountryCode", request.getSourceCountryCode(), "sourceCurrencyCode", request.getSourceCurrencyCode(), "destinationCountryCode", request.getDestinationCountryCode(), "destinationCurrencyCode", request.getDestinationCurrencyCode(), "baseAmount", request.getBaseAmount()), "getTransferPreview", "transfer-preview"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> listAccountTransactions(AmazonSellerWalletReqVO request) { require(request.getAccountId(), "accountId"); return get(request, "/transactions", optional("accountId", request.getAccountId(), "nextPageToken", request.getNextPageToken()), "listAccountTransactions", "transactions"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> createTransaction(AmazonSellerWalletReqVO request) { return mutate(request, "/transactions", HttpMethod.POST, "createTransaction"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getTransaction(AmazonSellerWalletReqVO request) { return get(request, "/transactions/" + id(request.getTransactionId(), "transactionId"), Map.of(), "getTransaction", "transaction"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> listTransferSchedules(AmazonSellerWalletReqVO request) { require(request.getAccountId(), "accountId"); return get(request, "/transferSchedules", optional("accountId", request.getAccountId(), "nextPageToken", request.getNextPageToken()), "listTransferSchedules", "transfer-schedules"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> createTransferSchedule(AmazonSellerWalletReqVO request) { return mutate(request, "/transferSchedules", HttpMethod.POST, "createTransferSchedule"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> updateTransferSchedule(AmazonSellerWalletReqVO request) { return mutate(request, "/transferSchedules", HttpMethod.PUT, "updateTransferSchedule"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getTransferSchedule(AmazonSellerWalletReqVO request) { return get(request, "/transferSchedules/" + id(request.getTransferScheduleId(), "transferScheduleId"), Map.of(), "getTransferSchedule", "transfer-schedule"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> deleteTransferSchedule(AmazonSellerWalletReqVO request) { return mutate(request, "/transferSchedules/" + id(request.getTransferScheduleId(), "transferScheduleId"), HttpMethod.DELETE, "deleteScheduleTransaction"); }
    /**
     * 调用 Seller Wallet 查询接口，并强制追加当前国家对应的 marketplaceId。
     *
     * @param request 店铺和站点参数
     * @param resource API 资源路径
     * @param parameters 额外查询参数
     * @param operation Amazon 操作名称
     * @param storage 响应归档名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> get(AmazonSellerWalletReqVO request, String resource, Map<String, String> parameters, String operation, String storage) { AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode()); Map<String, String> query = new TreeMap<>(parameters); query.put("marketplaceId", marketplace.getMarketplaceId()); URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + PATH + resource + "?" + query(query)); return amazonSellingPartnerClient.getByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), AmazonApiCategory.SELLER_WALLET, operation, storage, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()); }
    /**
     * 调用 Seller Wallet 写接口；数字签名仅随请求转发，由审计服务负责脱敏记录。
     *
     * @param request 含签名和请求体的参数
     * @param resource API 资源路径
     * @param method HTTP 请求方式
     * @param operation Amazon 操作名称
     * @return Amazon 原始 JSON 响应
     */
    private Map<String, Object> mutate(AmazonSellerWalletReqVO request, String resource, HttpMethod method, String operation) { AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode()); if (method != HttpMethod.DELETE) { if (request.getBody() == null || request.getBody().isEmpty()) throw new IllegalArgumentException("body 不能为空"); require(request.getDestAccountDigitalSignature(), "destAccountDigitalSignature"); require(request.getAmountDigitalSignature(), "amountDigitalSignature"); } URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + PATH + resource + "?" + query(Map.of("marketplaceId", marketplace.getMarketplaceId()))); return amazonSellingPartnerClient.mutateSellerWallet(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), method, request.getBody(), method == HttpMethod.DELETE ? Map.of() : Map.of("destAccountDigitalSignature", request.getDestAccountDigitalSignature(), "amountDigitalSignature", request.getAmountDigitalSignature()), operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()); }
    /** 将可选键值对中过滤空值，避免 Amazon 接收到无效空参数。 */
    private Map<String, String> optional(String key1, String value1, String key2, String value2) { Map<String, String> values = new TreeMap<>(); if (!blank(value1)) values.put(key1, value1); if (!blank(value2)) values.put(key2, value2); return values; }
    /** 对路径标识符进行编码并校验其必填性。 */
    private String id(String value, String name) { require(value, name); return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8); }
    /** 对必填字符串执行空白校验。 */
    private void require(String value, String name) { if (blank(value)) throw new IllegalArgumentException(name + " 不能为空"); }
    /** 构建经过 RFC 3986 编码的查询字符串。 */
    private String query(Map<String, String> query) { return query.entrySet().stream().map(item -> encode(item.getKey()) + "=" + encode(item.getValue())).collect(Collectors.joining("&")); }
    /** 使用 UTF-8 对查询字段编码。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~"); }
    /** 查询当前租户店铺，防止跨租户的 Seller 授权调用。 */
    private AmazonShopDO shop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 解析请求国家代码的 Marketplace 与区域端点。 */
    private AmazonMarketplaceEnum marketplace(String countryCode) { AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return marketplace; }
    /** 判断字符串是否为空白。 */
    private boolean blank(String value) { return value == null || value.isBlank(); }
}
