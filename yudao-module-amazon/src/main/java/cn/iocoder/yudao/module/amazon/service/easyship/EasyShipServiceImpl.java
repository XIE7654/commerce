package cn.iocoder.yudao.module.amazon.service.easyship;

import cn.iocoder.yudao.module.amazon.controller.admin.easyship.vo.EasyShipRequestVO;
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
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Easy Ship 服务实现，统一完成授权、站点路由与参数编码。 */
@Service
public class EasyShipServiceImpl implements EasyShipService {
    private static final String API_PREFIX = "/easyShip/2022-03-23";
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;
    /** {@inheritDoc} */
    @Override public Map<String, Object> invoke(EasyShipRequestVO request, String operation, String method, String resourcePath) {
        AmazonShopDO shop = shop(request.getShopId()); AmazonMarketplaceEnum marketplace = marketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + API_PREFIX + resourcePath + query(request.getQuery()));
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()), HttpMethod.valueOf(method), request.getBody(), Map.of(), AmazonApiCategory.EASY_SHIP, operation, "easy-ship-" + operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }
    /** 查询当前租户店铺，避免跨租户使用授权。 */
    private AmazonShopDO shop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 解析国家代码对应的 Amazon 端点。 */
    private AmazonMarketplaceEnum marketplace(String countryCode) { AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return marketplace; }
    /** 过滤并编码查询参数，防止空参数及特殊字符导致请求失真。 */
    private String query(Map<String, String> values) { if (values == null || values.isEmpty()) return ""; String query = new TreeMap<>(values).entrySet().stream().filter(entry -> entry.getValue() != null && !entry.getValue().isBlank()).map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue())).collect(Collectors.joining("&")); return query.isBlank() ? "" : "?" + query; }
    /** 使用 UTF-8 编码请求参数。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
}
