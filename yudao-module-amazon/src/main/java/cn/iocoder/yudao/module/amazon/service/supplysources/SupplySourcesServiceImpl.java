package cn.iocoder.yudao.module.amazon.service.supplysources;

import cn.iocoder.yudao.module.amazon.controller.admin.supplysources.vo.SupplySourcesReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/** Amazon Supply Sources 服务实现。 */
@Service
public class SupplySourcesServiceImpl implements SupplySourcesService {
    private static final String PATH = "/supplySources/2020-07-01/supplySources";
    @Resource private AmazonOAuthService amazonOAuthService; @Resource private AmazonShopMapper amazonShopMapper; @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */ @Override public Map<String, Object> getSupplySources(SupplySourcesReqVO request) { String query = query(request); return invoke(request, HttpMethod.GET, "", query, "getSupplySources"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> createSupplySource(SupplySourcesReqVO request) { return invoke(request, HttpMethod.POST, "", "", "createSupplySource"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> getSupplySource(SupplySourcesReqVO request) { return invoke(request, HttpMethod.GET, id(request), "", "getSupplySource"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> updateSupplySource(SupplySourcesReqVO request) { return invoke(request, HttpMethod.PUT, id(request), "", "updateSupplySource"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> archiveSupplySource(SupplySourcesReqVO request) { return invoke(request, HttpMethod.DELETE, id(request), "", "archiveSupplySource"); }
    /** {@inheritDoc} */ @Override public Map<String, Object> updateSupplySourceStatus(SupplySourcesReqVO request) { return invoke(request, HttpMethod.PUT, id(request) + "/status", "", "updateSupplySourceStatus"); }
    /** 将请求路由至 Supply Sources；归档操作按规范允许 HTTP 204。 */
    private Map<String, Object> invoke(SupplySourcesReqVO request, HttpMethod method, String suffix, String query, String operation) {
        AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + PATH + suffix + query);
        String token = amazonOAuthService.getSellerAccessToken(shop.getId());
        if (method == HttpMethod.GET) return amazonSellingPartnerClient.getByCategory(uri, token, AmazonApiCategory.SUPPLY_SOURCES, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
        return amazonSellingPartnerClient.mutateByCategoryOptional(uri, token, method, request.getBody(), AmazonApiCategory.SUPPLY_SOURCES, operation, operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 生成并校验路径供货源编号。 */ private String id(SupplySourcesReqVO request) { if (request.getSupplySourceId() == null || request.getSupplySourceId().isBlank()) throw new IllegalArgumentException("supplySourceId 不能为空"); return "/" + UriUtils.encodePathSegment(request.getSupplySourceId(), StandardCharsets.UTF_8); }
    /** 构建分页查询参数，避免只有 pageSize 时被误写为 nextPageToken。 */ private String query(SupplySourcesReqVO request) { StringBuilder value = new StringBuilder(); append(value, "nextPageToken", request.getNextPageToken()); append(value, "pageSize", request.getPageSize() == null ? null : String.valueOf(request.getPageSize())); return value.isEmpty() ? "" : "?" + value; }
    /** 将非空查询参数以 UTF-8 编码追加到查询串。 */ private void append(StringBuilder query, String key, String value) { if (value == null || value.isBlank()) return; if (!query.isEmpty()) query.append('&'); query.append(UriUtils.encodeQueryParam(key, StandardCharsets.UTF_8)).append('=').append(UriUtils.encodeQueryParam(value, StandardCharsets.UTF_8)); }
    /** 获取当前租户店铺。 */ private AmazonShopDO shop(Long shopId) { AmazonShopDO value = amazonShopMapper.selectById(shopId); if (value == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return value; }
    /** 解析 Amazon 调用站点。 */ private AmazonMarketplaceEnum marketplace(String countryCode) { AmazonMarketplaceEnum value = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (value == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return value; }
}
