package cn.iocoder.yudao.module.amazon.service.datakiosk;

import cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo.*;
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
import java.util.Map;
import java.util.stream.Collectors;

/** Data Kiosk API 服务实现。 */
@Service
public class DataKioskServiceImpl implements DataKioskService {
    private static final String BASE_PATH = "/dataKiosk/2023-11-15";
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */ @Override public Map<String, Object> createQuery(DataKioskCreateQueryReqVO request) { Map<String, Object> body = new LinkedHashMap<>(); body.put("query", request.getQuery()); putObject(body, "paginationToken", request.getPaginationToken()); return invoke(request, BASE_PATH + "/queries", HttpMethod.POST, body, "createQuery", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getQueries(DataKioskQueriesReqVO request) { Map<String, String> query = new LinkedHashMap<>(); if (request.getProcessingStatuses() != null && !request.getProcessingStatuses().isEmpty()) query.put("processingStatuses", String.join(",", request.getProcessingStatuses())); putString(query, "pageSize", request.getPageSize()); putString(query, "createdSince", request.getCreatedSince()); putString(query, "createdUntil", request.getCreatedUntil()); putString(query, "paginationToken", request.getPaginationToken()); return invoke(request, BASE_PATH + "/queries" + queryString(query), HttpMethod.GET, null, "getQueries", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getQuery(DataKioskQueryIdReqVO request) { return invoke(request, BASE_PATH + "/queries/" + encode(request.getQueryId()), HttpMethod.GET, null, "getQuery", false); }
    /** {@inheritDoc} */ @Override public Map<String, Object> cancelQuery(DataKioskQueryIdReqVO request) { return invoke(request, BASE_PATH + "/queries/" + encode(request.getQueryId()), HttpMethod.DELETE, null, "cancelQuery", true); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getDocument(DataKioskDocumentIdReqVO request) { return invoke(request, BASE_PATH + "/documents/" + encode(request.getDocumentId()), HttpMethod.GET, null, "getDocument", false); }

    /** 使用店铺卖家令牌调用 Data Kiosk；取消任务按模型规范接受 204 空响应。 */
    private Map<String, Object> invoke(DataKioskBaseReqVO request, String path, HttpMethod method, Object body, String operation, boolean allowEmptyResponse) {
        AmazonShopDO shop = requireShop(request.getShopId()); AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + path); String token = amazonOAuthService.getSellerAccessToken(shop.getId());
        return allowEmptyResponse ? amazonSellingPartnerClient.mutateByCategoryOptional(uri, token, method, body, AmazonApiCategory.DATA_KIOSK, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()) : method == HttpMethod.GET ? amazonSellingPartnerClient.getByCategory(uri, token, AmazonApiCategory.DATA_KIOSK, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId()) : amazonSellingPartnerClient.mutateByCategory(uri, token, method, body, AmazonApiCategory.DATA_KIOSK, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 构造编码后的查询串，防止分页令牌等保留字符改变请求含义。 */
    private String queryString(Map<String, String> query) { return query.isEmpty() ? "" : "?" + query.entrySet().stream().map(e -> encode(e.getKey()) + "=" + encode(e.getValue())).collect(Collectors.joining("&")); }
    /** 仅在值存在时写入可选 JSON 参数。 */
    private void putObject(Map<String, Object> parameters, String key, Object value) { if (value != null && (!(value instanceof String) || !((String) value).isBlank())) parameters.put(key, value); }
    /** 将非空可选参数转换为查询字符串值。 */
    private void putString(Map<String, String> parameters, String key, Object value) { if (value != null) { String text = value.toString(); if (!text.isBlank()) parameters.put(key, text); } }
    /** 查询当前租户店铺，确保租户拦截器完成数据隔离。 */
    private AmazonShopDO requireShop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 根据国家代码取得受支持的 SP-API 站点。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) { AmazonMarketplaceEnum value = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (value == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return value; }
    /** 以 UTF-8 百分号编码路径与查询参数。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~"); }
}
