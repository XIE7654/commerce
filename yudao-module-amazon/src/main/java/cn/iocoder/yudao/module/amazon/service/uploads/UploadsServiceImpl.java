package cn.iocoder.yudao.module.amazon.service.uploads;

import cn.iocoder.yudao.module.amazon.controller.admin.uploads.vo.UploadsCreateDestinationReqVO;
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
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Amazon Uploads API 服务实现。 */
@Service
public class UploadsServiceImpl implements UploadsService {

    private static final String PATH = "/uploads/2020-11-01/uploadDestinations/";

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
    public Map<String, Object> createUploadDestination(UploadsCreateDestinationReqVO request) {
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, String> query = new LinkedHashMap<>();
        query.put("marketplaceIds", request.getMarketplaceIds().stream().collect(Collectors.joining(",")));
        query.put("contentMD5", request.getContentMD5());
        if (request.getContentType() != null && !request.getContentType().isBlank()) {
            query.put("contentType", request.getContentType());
        }
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + PATH + encodeResource(request.getResource()) + "?" + query(query));
        String marketplaceId = request.getMarketplaceIds().get(0);
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.POST, null, Map.of(), AmazonApiCategory.UPLOADS, "createUploadDestinationForResource",
                "upload-destination", shop.getId(), request.getCountryCode(), marketplaceId);
    }

    /** 保留贪婪路径参数内部的斜杠，同时对每个资源段中的保留字符进行编码。 */
    private String encodeResource(String resource) {
        String normalized = resource.startsWith("/") ? resource.substring(1) : resource;
        return UriUtils.encodePath(normalized, StandardCharsets.UTF_8);
    }

    /** 查询当前租户可见的店铺，确保凭证与租户隔离一致。 */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /** 根据国家代码解析 SP-API 调用端点。 */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /** 对查询参数编码，防止 MD5 尾部等保留字符改变请求语义。 */
    private String query(Map<String, String> values) {
        return values.entrySet().stream().map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /** 使用 UTF-8 百分号编码查询参数。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }
}
