package cn.iocoder.yudao.module.amazon.service.spapi;

import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** 通用 SP-API 白名单操作调用实现，负责租户店铺校验、URL 编码和请求归档。 */
public abstract class AmazonSpApiServiceSupport {

    private static final Pattern PATH_PARAM_PATTERN = Pattern.compile("\\{([^}]+)}");

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /**
     * 按操作定义调用 Amazon API，非查询操作允许 204 空响应。
     *
     * @param request 店铺、路径参数、查询参数和请求体
     * @param operation Amazon operationId
     * @param definition 操作的请求方法和资源路径
     * @param category API 归档分类
     * @param storageName 响应归档名称
     * @return Amazon 原始 JSON 响应；204 时返回空 Map
     */
    protected Map<String, Object> invoke(AmazonSpApiReqVO request, String operation, OperationDefinition definition,
                                         AmazonApiCategory category, String storageName) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        URI uri = URI.create(marketplace.getEndpoint() + buildPath(definition.path(), request.getPathParams())
                + buildQuery(request.getQuery()));
        String token = amazonOAuthService.getSellerAccessToken(shop.getId());
        if (definition.method() == HttpMethod.GET) {
            return amazonSellingPartnerClient.getByCategory(uri, token, category, operation, storageName, shop.getId(),
                    request.getCountryCode(), marketplace.getMarketplaceId());
        }
        return amazonSellingPartnerClient.mutateByCategoryOptional(uri, token, definition.method(), request.getBody(),
                category, operation, storageName, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 替换并编码路径参数，避免路径分隔符改变 Amazon 资源定位。
     *
     * @param template Amazon 模型定义的路径模板
     * @param pathParams 调用方传入的路径参数
     * @return 已编码的实际路径
     */
    private String buildPath(String template, Map<String, String> pathParams) {
        Matcher matcher = PATH_PARAM_PATTERN.matcher(template);
        StringBuffer path = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String value = pathParams == null ? null : pathParams.get(name);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("缺少路径参数: " + name);
            }
            matcher.appendReplacement(path, Matcher.quoteReplacement(UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)));
        }
        matcher.appendTail(path);
        return path.toString();
    }

    /**
     * 构造并排序查询参数，保证特殊字符不会改变 Amazon 请求含义。
     *
     * @param query 调用方查询参数
     * @return 空串或以问号开头的编码查询串
     */
    private String buildQuery(Map<String, String> query) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        String result = new TreeMap<>(query).entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
        return result.isEmpty() ? "" : "?" + result;
    }

    /**
     * 使用 RFC 3986 兼容形式编码查询字段。
     *
     * @param value 原始参数值
     * @return UTF-8 百分号编码结果
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /**
     * 查询当前租户下的店铺，避免跨租户使用授权令牌。
     *
     * @param shopId 店铺编号
     * @return 当前租户店铺
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 解析国家代码对应的 Amazon Marketplace。
     *
     * @param countryCode 国家代码
     * @return Marketplace 配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /** Amazon operation 的 HTTP 方法和路径定义。 */
    public record OperationDefinition(HttpMethod method, String path) {
    }
}
