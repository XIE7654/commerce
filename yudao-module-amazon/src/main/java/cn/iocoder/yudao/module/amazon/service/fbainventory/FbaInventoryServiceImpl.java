package cn.iocoder.yudao.module.amazon.service.fbainventory;

import cn.iocoder.yudao.module.amazon.controller.admin.fbainventory.vo.FbaInventorySummariesReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.sdk.AmazonSellingPartnerClient;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Amazon FBA Inventory 服务实现。
 */
@Service
public class FbaInventoryServiceImpl implements FbaInventoryService {

    @Resource
    private AmazonOAuthService amazonOAuthService;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private AmazonSellingPartnerClient amazonSellingPartnerClient;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getInventorySummaries(FbaInventorySummariesReqVO request) {
        validateRequest(request);
        AmazonShopDO shop = requireShop(request.getShopId());
        AmazonMarketplaceEnum marketplace = requireMarketplace(request.getCountryCode());
        String accessToken = amazonOAuthService.getSellerAccessToken(shop.getId());
        return amazonSellingPartnerClient.getInventorySummaries(buildRequestUri(marketplace, request), accessToken,
                shop.getId(), request.getCountryCode(), marketplace.getMarketplaceId());
    }

    /**
     * 校验 FBA Inventory 的互斥与依赖筛选条件，防止 Amazon 忽略参数而导致调用方误判查询范围。
     *
     * @param request FBA 库存摘要查询参数
     */
    private void validateRequest(FbaInventorySummariesReqVO request) {
        if (!isBlank(request.getStartDateTime())) {
            validateDateTime(request.getStartDateTime());
            if (!isEmpty(request.getSellerSkus()) || !isBlank(request.getSellerSku())) {
                throw new IllegalArgumentException("startDateTime 不能与 sellerSkus 或 sellerSku 同时传入");
            }
        }
        if (!isEmpty(request.getSellerSkus()) && !isBlank(request.getSellerSku())) {
            throw new IllegalArgumentException("sellerSkus 与 sellerSku 不能同时传入");
        }
        if (!isBlank(request.getNextToken()) && isBlank(request.getStartDateTime())) {
            throw new IllegalArgumentException("nextToken 必须与 startDateTime 同时传入");
        }
    }

    /**
     * 构造 getInventorySummaries 请求 URI；Marketplace 粒度由国家代码统一转换，避免传入不匹配的粒度 ID。
     *
     * @param marketplace 目标站点配置
     * @param request FBA 库存摘要查询参数
     * @return 可直接发起请求的 URI
     */
    private URI buildRequestUri(AmazonMarketplaceEnum marketplace, FbaInventorySummariesReqVO request) {
        Map<String, String> query = new TreeMap<>();
        query.put("granularityType", "Marketplace");
        query.put("granularityId", marketplace.getMarketplaceId());
        query.put("marketplaceIds", marketplace.getMarketplaceId());
        put(query, "details", request.getDetails() == null ? null : request.getDetails().toString());
        put(query, "startDateTime", request.getStartDateTime());
        put(query, "sellerSkus", join(request.getSellerSkus()));
        put(query, "sellerSku", request.getSellerSku());
        put(query, "nextToken", request.getNextToken());
        return URI.create(marketplace.getEndpoint() + "/fba/inventory/v1/summaries?" + buildQuery(query));
    }

    /**
     * 验证时间格式为带偏移量的 ISO 8601，确保请求参数可被 Amazon 正确解析。
     *
     * @param dateTime 待验证的开始时间
     */
    private void validateDateTime(String dateTime) {
        try {
            OffsetDateTime.parse(dateTime);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("startDateTime 必须为 ISO 8601 日期时间格式", exception);
        }
    }

    /**
     * 按 RFC 3986 编码并排序查询参数。
     *
     * @param query 查询参数
     * @return 用于 URI 的查询字符串
     */
    private String buildQuery(Map<String, String> query) {
        List<String> entries = new ArrayList<>();
        query.forEach((key, value) -> entries.add(urlEncode(key) + "=" + urlEncode(value)));
        return String.join("&", entries);
    }

    /**
     * 使用 UTF-8 对查询参数进行 RFC 3986 百分号编码。
     *
     * @param value 待编码的参数值
     * @return 编码后的参数值
     */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

    /**
     * 仅在值非空时写入可选查询参数。
     *
     * @param query 待写入的查询参数集合
     * @param key 参数名称
     * @param value 参数值
     */
    private void put(Map<String, String> query, String key, String value) {
        if (!isBlank(value)) {
            query.put(key, value);
        }
    }

    /**
     * 将卖家 SKU 列表转换为 Amazon 要求的逗号分隔参数。
     *
     * @param values 卖家 SKU 列表
     * @return 逗号分隔的 SKU；空列表返回 {@code null}
     */
    private String join(List<String> values) {
        return isEmpty(values) ? null : String.join(",", values);
    }

    /**
     * 查询当前租户下的 Amazon 店铺，以复用租户拦截器确保店铺隔离。
     *
     * @param shopId 店铺编号
     * @return 当前租户的 Amazon 店铺
     */
    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) {
            throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        }
        return shop;
    }

    /**
     * 解析请求国家代码对应的 Amazon Marketplace。
     *
     * @param countryCode 国家代码
     * @return 目标站点配置
     */
    private AmazonMarketplaceEnum requireMarketplace(String countryCode) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return marketplace;
    }

    /**
     * 判断字符串是否为空白。
     *
     * @param value 待判断的字符串
     * @return 字符串为空或仅包含空白字符时返回 {@code true}
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * 判断集合是否为空。
     *
     * @param values 待判断的集合
     * @return 集合为 {@code null} 或不含元素时返回 {@code true}
     */
    private boolean isEmpty(List<?> values) {
        return values == null || values.isEmpty();
    }
}
