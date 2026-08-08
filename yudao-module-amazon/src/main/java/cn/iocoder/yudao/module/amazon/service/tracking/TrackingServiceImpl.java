package cn.iocoder.yudao.module.amazon.service.tracking;

import cn.iocoder.yudao.module.amazon.controller.admin.tracking.vo.TrackingShipmentReqVO;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Amazon Tracking API 服务实现。 */
@Service
public class TrackingServiceImpl implements TrackingService {

    private static final String PATH = "/tracking/2026-01-30/shipments/track";

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getShipmentTracking(TrackingShipmentReqVO request) {
        validateIdentifier(request);
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        Map<String, String> query = new TreeMap<>();
        put(query, "id", request.getId());
        put(query, "acsin", request.getAcsin());
        put(query, "aftn", request.getAftn());
        put(query, "containerNumber", request.getContainerNumber());
        put(query, "houseBillOfLadingNumber", request.getHouseBillOfLadingNumber());
        put(query, "carrierTracking.trackingNumber", request.getCarrierTrackingNumber());
        put(query, "carrierTracking.carrierCode", request.getCarrierCode());
        Map<String, String> headers = new LinkedHashMap<>();
        if (!blank(request.getAcceptLanguage())) {
            headers.put("Accept-Language", request.getAcceptLanguage());
        }
        URI uri = URI.create(marketplace.getEndpoint() + PATH + "?" + query(query));
        return amazonSellingPartnerClient.executeByCategory(uri, amazonOAuthService.getSellerAccessToken(shop.getId()),
                HttpMethod.GET, null, headers, AmazonApiCategory.TRACKING, "getShipmentTracking", "shipment-tracking",
                shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /** 校验至少提供一个可用于查询货件的标识，避免向 Amazon 发送无效空查询。 */
    private void validateIdentifier(TrackingShipmentReqVO request) {
        if (blank(request.getId()) && blank(request.getAcsin()) && blank(request.getAftn())
                && blank(request.getContainerNumber()) && blank(request.getHouseBillOfLadingNumber())
                && blank(request.getCarrierTrackingNumber())) {
            throw new IllegalArgumentException("至少需要传入一个货件跟踪标识");
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

    /** 忽略空值并编码查询参数，保持 Amazon 定义的嵌套参数名称。 */
    private String query(Map<String, String> values) {
        return values.entrySet().stream().map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /** 将非空参数放入查询集合。 */
    private void put(Map<String, String> values, String key, String value) {
        if (!blank(value)) {
            values.put(key, value);
        }
    }

    /** 使用 UTF-8 百分号编码查询值，避免标识符中的保留字符改变 URI 语义。 */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /** 判断可选字符串是否为空白。 */
    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
