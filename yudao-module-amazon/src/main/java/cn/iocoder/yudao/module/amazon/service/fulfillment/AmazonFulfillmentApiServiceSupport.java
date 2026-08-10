package cn.iocoder.yudao.module.amazon.service.fulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import cn.iocoder.yudao.module.amazon.service.shop.AmazonShopService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.iocoder.yudao.module.amazon.utils.AmazonQueryUtils.buildQuery;

/** Fulfillment 系列 API 的公共调用实现，负责租户店铺校验、参数编码及请求归档。 */
public abstract class AmazonFulfillmentApiServiceSupport {

    private static final Pattern PATH_PARAM_PATTERN = Pattern.compile("\\{([^}]+)}");

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopService amazonShopService;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /**
     * 按白名单操作定义调用 Amazon API，写操作允许 Amazon 使用 HTTP 204 返回成功。
     *
     * @param request 店铺、路径参数、查询参数和请求体
     * @param operation Amazon operationId
     * @param definition 操作的请求方法和资源路径
     * @param category API 归档分类
     * @param storageName 响应归档名称
     * @return Amazon 原始 JSON 响应；204 时为空 Map
     */
    protected Map<String, Object> invoke(AmazonFulfillmentApiReqVO request, String operation, OperationDefinition definition,
                                         AmazonApiCategory category, String storageName) {
        AmazonShopDO shop = amazonShopService.requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = amazonShopService.requireMarketplace(request.getCountryCode());
        String query = buildQuery(request.getQuery());
        URI uri = URI.create(marketplace.getEndpoint() + buildPath(definition.path(), request.getPathParams())
                + (query.isEmpty() ? "" : "?" + query));
        String token = amazonOAuthService.getSellerAccessToken(shop.getId());
        if (definition.method() == HttpMethod.GET) {
            return amazonSellingPartnerClient.getByCategory(uri, token, category, operation, storageName, shop.getId(),
                    request.getCountryCode(), marketplace.getMarketplaceId());
        }
        return amazonSellingPartnerClient.mutateByCategoryOptional(uri, token, definition.method(), request.getBody(),
                category, operation, storageName, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 替换并编码路径参数，缺少路径参数时在发起远程请求前失败。
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

    /** Amazon operation 的 HTTP 方法和路径定义。 */
    public record OperationDefinition(HttpMethod method, String path) {
    }
}
