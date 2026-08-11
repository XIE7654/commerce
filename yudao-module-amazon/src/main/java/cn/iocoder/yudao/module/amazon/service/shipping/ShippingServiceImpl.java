package cn.iocoder.yudao.module.amazon.service.shipping;

import cn.iocoder.yudao.module.amazon.controller.admin.shipping.vo.ShippingRequestVO;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Shipping 服务实现，负责路径替换、V2 必填业务头及店铺授权隔离。 */
@Service
public class ShippingServiceImpl implements ShippingService {
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */
    @Override public Map<String, Object> invoke(ShippingRequestVO request, String operation, String method, String resourcePath) {
        AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode());
        boolean versionTwo = resourcePath.startsWith("/shipping/v2/");
        if (versionTwo && blank(request.getShippingBusinessId())) throw new IllegalArgumentException("shippingBusinessId 不能为空");
        String path = resourcePath;
        // 仅在路由声明对应占位符时校验该标识，集合与费率接口不需要路径参数。
        if (path.contains("{id}")) path = path.replace("{id}", pathId(request.getResourceId(), "resourceId"));
        if (path.contains("{secondaryId}")) path = path.replace("{secondaryId}", pathId(request.getSecondaryResourceId(), "secondaryResourceId"));
        Map<String, String> headers = new LinkedHashMap<>();
        if (versionTwo) headers.put("x-amzn-shipping-business-id", request.getShippingBusinessId());
        if (!blank(request.getIdempotencyKey())) headers.put("x-amzn-IdempotencyKey", request.getIdempotencyKey());
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + path + query(request.getQuery()));
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), HttpMethod.valueOf(method), request.getBody(), headers, AmazonApiCategory.SHIPPING, operation, "shipping-" + operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 编码并校验路径编号，避免保留字符被解释为额外路径。 */
    private String pathId(String value, String name) { if (blank(value)) throw new IllegalArgumentException(name + " 不能为空"); return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
    /** 查询当前租户店铺，避免跨租户使用授权。 */
    private AmazonShopDO shop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 解析国家代码对应的 Amazon 端点。 */
    private AmazonMarketplaceEnum marketplace(String countryCode) { AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return marketplace; }
    /** 过滤空值并编码查询参数。 */
    private String query(Map<String, String> values) { if (values == null || values.isEmpty()) return ""; String query = new TreeMap<>(values).entrySet().stream().filter(entry -> !blank(entry.getValue())).map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue())).collect(Collectors.joining("&")); return query.isBlank() ? "" : "?" + query; }
    /** 使用 UTF-8 编码请求参数。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
    /** 判断字符串是否为空白。 */
    private boolean blank(String value) { return value == null || value.isBlank(); }
}
