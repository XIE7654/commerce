package cn.iocoder.yudao.module.amazon.service.sales;

import cn.iocoder.yudao.module.amazon.controller.admin.sales.vo.AmazonSalesOrderMetricsReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/** Amazon Sales 服务实现。 */
@Service
public class AmazonSalesServiceImpl implements AmazonSalesService {
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getOrderMetrics(AmazonSalesOrderMetricsReqVO request) {
        if (!isBlank(request.getAsin()) && !isBlank(request.getSku())) {
            throw new IllegalArgumentException("asin 与 sku 不能同时传入");
        }
        if (!"Hour".equals(request.getGranularity()) && isBlank(request.getGranularityTimeZone())) {
            throw new IllegalArgumentException("granularity 大于 Hour 时 granularityTimeZone 必须传入");
        }
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, String> query = new TreeMap<>();
        query.put("marketplaceIds", marketplace.getMarketplaceId());
        query.put("interval", request.getInterval());
        query.put("granularity", request.getGranularity());
        put(query, "granularityTimeZone", request.getGranularityTimeZone());
        put(query, "buyerType", request.getBuyerType()); put(query, "fulfillmentNetwork", request.getFulfillmentNetwork());
        put(query, "firstDayOfWeek", request.getFirstDayOfWeek()); put(query, "asin", request.getAsin());
        put(query, "sku", request.getSku()); put(query, "amazonProgram", request.getAmazonProgram());
        URI uri = URI.create(marketplace.getEndpoint() + "/sales/v1/orderMetrics?" + query(query));
        return amazonSellingPartnerClient.getByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                AmazonApiCategory.SALES, "getOrderMetrics", "order-metrics", shop.getId(), request.getCountryCode(),
                marketplace.getMarketplaceId());
    }

    /** 查询当前租户的店铺，确保租户拦截器参与店铺隔离。 */
    private AmazonShopDO requireShop(Long shopId) { AmazonShopDO shop = amazonShopMapper.selectById(shopId); if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId); return shop; }
    /** 解析国家代码对应的 Amazon 站点。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) { AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode); if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode); return marketplace; }
    /** 将非空可选参数加入查询字符串。 */
    private void put(Map<String, String> query, String key, String value) { if (!isBlank(value)) query.put(key, value); }
    /** 以 RFC 3986 编码构造稳定排序的查询字符串。 */
    private String query(Map<String, String> query) { return query.entrySet().stream().map(item -> encode(item.getKey()) + "=" + encode(item.getValue())).collect(java.util.stream.Collectors.joining("&")); }
    /** 对查询参数执行 UTF-8 百分号编码。 */
    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~"); }
    /** 判断字符串是否为空白。 */
    private boolean isBlank(String value) { return value == null || value.isBlank(); }
}
