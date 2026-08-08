package cn.iocoder.yudao.module.amazon.service.awd;

import cn.iocoder.yudao.module.amazon.controller.admin.awd.vo.AwdRequestVO;
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
import java.util.stream.Collectors;

/** AWD 服务实现，统一处理店铺隔离、站点解析及 API 调用。 */
@Service
public class AwdServiceImpl implements AwdService {
    @Resource private AmazonOAuthService amazonOAuthService;
    @Resource private AmazonShopMapper amazonShopMapper;
    @Resource private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(AwdRequestVO request, String operation, String method, String resourcePath) {
        AmazonShopDO shop = amazonShopMapper.selectById(request.getShopId());
        if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + request.getShopId());
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(request.getCountryCode());
        if (marketplace == null) throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + request.getCountryCode());
        String path = resourcePath.replace("{id}", encode(request.getResourceId()));
        String query = request.getQuery() == null ? "" : request.getQuery().entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isBlank())
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue())).collect(Collectors.joining("&"));
        URI uri = URI.create(marketplace.getEndpoint() + "/awd/2024-05-09" + path + (query.isBlank() ? "" : "?" + query));
        String token = amazonOAuthService.getSellerAccessToken(shop.getId());
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if (httpMethod == HttpMethod.GET) {
            return amazonSellingPartnerClient.getByCategory(uri, token, AmazonApiCategory.AMAZON_WAREHOUSING_AND_DISTRIBUTION,
                    operation, "awd-" + operation, shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
        }
        // Amazon 对确认、取消及运输信息更新成功时返回 204，客户端必须允许空响应体。
        boolean allowEmpty = operation.startsWith("confirm") || operation.startsWith("cancel")
                || operation.contains("Transport");
        if (allowEmpty) {
            return amazonSellingPartnerClient.mutateByCategoryOptional(uri, token, httpMethod, request.getBody(),
                    AmazonApiCategory.AMAZON_WAREHOUSING_AND_DISTRIBUTION, operation, "awd-" + operation,
                    shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
        }
        return amazonSellingPartnerClient.mutateByCategory(uri, token, httpMethod, request.getBody(),
                AmazonApiCategory.AMAZON_WAREHOUSING_AND_DISTRIBUTION, operation, "awd-" + operation,
                shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /** 对路径和查询参数进行 UTF-8 编码。 */
    private String encode(String value) { return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8).replace("+", "%20"); }
}
