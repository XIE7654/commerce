package cn.iocoder.yudao.module.amazon.service.deliverybyamazon;

import cn.iocoder.yudao.module.amazon.controller.admin.deliverybyamazon.vo.DeliveryByAmazonRequestVO;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Delivery by Amazon 服务实现，统一处理店铺授权与查询参数编码。 */
@Service
public class DeliveryByAmazonServiceImpl implements DeliveryByAmazonService {
    private static final String API_PREFIX = "/delivery/2022-07-01";
    @Resource private AmazonMarketplaceProvider amazonMarketplaceProvider;
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(DeliveryByAmazonRequestVO request, String operation, String method, String resourcePath) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + API_PREFIX + resourcePath + query(request.getQuery()));
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.valueOf(method), request.getBody(), Map.of(), AmazonApiCategory.DELIVERY_BY_AMAZON, operation,
                "delivery-by-amazon-" + operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 查询当前租户店铺，避免跨租户使用授权。 */
    private AmazonShopDO requireShop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 解析国家代码对应的 API 端点与 Marketplace。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) { AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return marketplace; }
    /** 构建并编码非空查询参数，避免特殊字符改变 Amazon 请求语义。 */
    private String query(Map<String, String> values) { if (values == null || values.isEmpty()) return ""; String query = new TreeMap<>(values).entrySet().stream().filter(entry -> entry.getValue() != null && !entry.getValue().isBlank()).map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue())).collect(Collectors.joining("&")); return query.isBlank() ? "" : "?" + query; }
    /** 使用 UTF-8 编码查询参数。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
}
