package cn.iocoder.yudao.module.amazon.service.seller;

import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceParticipationDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.seller.AmazonShopMarketplaceParticipationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Amazon 店铺 Marketplace 参与状态同步 Service 实现。
 */
@Service
public class AmazonShopMarketplaceParticipationServiceImpl implements AmazonShopMarketplaceParticipationService {

    @Resource
    private AmazonShopMarketplaceParticipationMapper marketplaceParticipationMapper;

    /** {@inheritDoc} */
    @Override
    public void syncMarketplaceParticipations(Long shopId, Map<String, Object> response) {
        Map<String, Object> payload = getMap(response, "payload");
        for (Object item : getList(payload, "marketplaceParticipationList")) {
            if (!(item instanceof Map<?, ?> participationItem)) {
                continue;
            }
            Map<String, Object> participation = toMap(participationItem);
            Map<String, Object> marketplace = getMap(participation, "marketplace");
            String marketplaceId = getString(marketplace, "id");
            if (marketplaceId == null || marketplaceId.isBlank()) {
                continue;
            }
            saveParticipation(shopId, marketplaceId, marketplace, participation);
        }
    }

    /**
     * 根据店铺与 Marketplace 的唯一关系新增或刷新一条参与状态。
     *
     * @param shopId 店铺编号
     * @param marketplaceId Marketplace ID
     * @param marketplace Marketplace 基础信息
     * @param participationItem Marketplace 参与信息
     */
    private void saveParticipation(Long shopId, String marketplaceId, Map<String, Object> marketplace,
                                   Map<String, Object> participationItem) {
        Map<String, Object> participation = getMap(participationItem, "participation");
        AmazonShopMarketplaceParticipationDO record = marketplaceParticipationMapper
                .selectByShopIdAndMarketplaceId(shopId, marketplaceId);
        if (record == null) {
            record = new AmazonShopMarketplaceParticipationDO();
            record.setShopId(shopId);
            record.setMarketplaceId(marketplaceId);
        }
        record.setCountryCode(getString(marketplace, "countryCode"));
        record.setMarketplaceName(getString(marketplace, "name"));
        record.setDefaultCurrencyCode(getString(marketplace, "defaultCurrencyCode"));
        record.setDefaultLanguageCode(getString(marketplace, "defaultLanguageCode"));
        record.setDomainName(getString(marketplace, "domainName"));
        record.setStoreName(getString(participationItem, "storeName"));
        record.setIsParticipating(getBoolean(participation, "isParticipating"));
        record.setHasSuspendedListings(getBoolean(participation, "hasSuspendedListings"));
        record.setLastSyncTime(LocalDateTime.now());
        if (record.getId() == null) {
            marketplaceParticipationMapper.insert(record);
        } else {
            marketplaceParticipationMapper.updateById(record);
        }
    }

    /**
     * 读取嵌套对象；缺失字段按空对象处理，避免单个可选字段影响整次同步。
     *
     * @param source 源对象
     * @param key 字段名
     * @return 字段对应的 Map
     */
    private Map<String, Object> getMap(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof Map<?, ?> map ? toMap(map) : Map.of();
    }

    /**
     * 将未知泛型的 JSON 对象转换为字符串键的 Map。
     *
     * @param source JSON 对象
     * @return 可读取的 Map
     */
    private Map<String, Object> toMap(Map<?, ?> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        source.forEach((key, value) -> result.put(String.valueOf(key), value));
        return result;
    }

    /**
     * 读取 JSON 数组；字段缺失或格式不符时返回空列表。
     *
     * @param source 源对象
     * @param key 字段名
     * @return 数组内容
     */
    private List<?> getList(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof List<?> list ? list : List.of();
    }

    /**
     * 将响应字段安全转换为字符串。
     *
     * @param source 源对象
     * @param key 字段名
     * @return 字段值；字段缺失时返回 {@code null}
     */
    private String getString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * 兼容 Boolean 与文本布尔值，适配不同 JSON 反序列化器的返回类型。
     *
     * @param source 源对象
     * @param key 字段名
     * @return 布尔字段值；字段缺失时返回 {@code null}
     */
    private Boolean getBoolean(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value == null ? null : value instanceof Boolean bool ? bool : Boolean.valueOf(String.valueOf(value));
    }
}
