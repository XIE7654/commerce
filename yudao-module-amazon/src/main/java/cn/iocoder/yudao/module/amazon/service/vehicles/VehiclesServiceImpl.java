package cn.iocoder.yudao.module.amazon.service.vehicles;

import cn.iocoder.yudao.module.amazon.controller.admin.vehicles.vo.VehiclesListReqVO;
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
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Amazon Vehicles API 服务实现。 */
@Service
public class VehiclesServiceImpl implements VehiclesService {

    private static final String PATH = "/catalog/2024-11-01/automotive/vehicles";

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
    public Map<String, Object> getVehicles(VehiclesListReqVO request) {
        validateUpdatedAfter(request.getUpdatedAfter());
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, String> query = new LinkedHashMap<>();
        query.put("marketplaceId", request.getMarketplaceId());
        query.put("vehicleType", request.getVehicleType());
        put(query, "pageToken", request.getPageToken());
        put(query, "updatedAfter", request.getUpdatedAfter());
        URI uri = URI.create(amazonMarketplaceProvider.getEndpoint(marketplace) + PATH + "?" + query(query));
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.GET, null, Map.of(), AmazonApiCategory.VEHICLES, "getVehicles", "vehicles",
                shop.getId(), request.getCountryCode(), request.getMarketplaceId());
    }

    /** 校验可选更新时间为 Amazon 要求的 ISO 8601 偏移日期时间。 */
    private void validateUpdatedAfter(String updatedAfter) {
        if (updatedAfter == null || updatedAfter.isBlank()) {
            return;
        }
        try {
            OffsetDateTime.parse(updatedAfter);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("updatedAfter 必须为 ISO 8601 日期时间格式", exception);
        }
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

    /** 将非空可选条件写入查询集合。 */
    private void put(Map<String, String> values, String key, String value) {
        if (value != null && !value.isBlank()) {
            values.put(key, value);
        }
    }

    /** 使用 UTF-8 百分号编码查询参数，避免分页令牌中的保留字符改变 URI 语义。 */
    private String query(Map<String, String> values) {
        return values.entrySet().stream().map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /** 使用 UTF-8 百分号编码查询参数。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }
}
