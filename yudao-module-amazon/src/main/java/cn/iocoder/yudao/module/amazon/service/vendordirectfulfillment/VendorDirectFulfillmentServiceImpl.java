package cn.iocoder.yudao.module.amazon.service.vendordirectfulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.vo.VendorDirectFulfillmentRequestVO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Vendor Direct Fulfillment 服务实现，统一保证店铺授权隔离与参数编码。 */
@Service
public class VendorDirectFulfillmentServiceImpl implements VendorDirectFulfillmentService {

    private static final Pattern PATH_PARAMETER = Pattern.compile("\\{([^}]+)}");

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
    public Map<String, Object> invoke(VendorDirectFulfillmentRequestVO request, String operation, String method,
                                      String resourcePath) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String path = expandPath(resourcePath, request.getPathParams());
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + path + query(request.getQuery()));
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.valueOf(method), request.getBody(), Map.of(), AmazonApiCategory.VENDOR_DIRECT_FULFILLMENT,
                operation, "vendor-direct-fulfillment-" + operation, shop.getId(), request.getCountryCode(),
                marketplace.getMarketplaceId());
    }

    /** 查询当前租户店铺，禁止使用不存在或无权访问店铺的授权。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** 解析国家代码对应的 Amazon API 端点与 Marketplace。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /**
     * 替换固定路径模板中的参数，并编码值以避免参数内容改变请求路径结构。
     *
     * @param template Controller 声明的 API 路径模板
     * @param values 调用方提供的路径参数
     * @return 可安全用于 URI 的实际请求路径
     */
    private String expandPath(String template, Map<String, String> values) {
        Matcher matcher = PATH_PARAMETER.matcher(template);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String value = values == null ? null : values.get(name);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(name + " 不能为空");
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(encode(value)));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /** 构建按名称排序且已编码的非空查询参数，保证签名请求参数稳定。 */
    private String query(Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        String query = new TreeMap<>(values).entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return query.isBlank() ? "" : "?" + query;
    }

    /** 使用 UTF-8 编码 URL 参数，并将空格规范为百分号编码。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
