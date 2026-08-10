package cn.iocoder.yudao.module.amazon.service.seller;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.amazon.dal.dataobject.seller.AmazonShopMarketplaceDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.seller.AmazonShopMarketplaceParticipationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getList;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getMap;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.getString;
import static cn.iocoder.yudao.module.amazon.utils.AmazonResponseUtils.toMap;

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
        for (Object item : getParticipationList(response)) {
            if (!(item instanceof Map<?, ?> participationItem)) {
                continue;
            }
            Map<String, Object> participation = toMap(participationItem);
            Map<String, Object> marketplace = getMap(participation, "marketplace");
            String marketplaceId = getString(marketplace, "id");
            if (StrUtil.isBlank(marketplaceId)) {
                continue;
            }
            saveParticipation(shopId, marketplaceId, marketplace, participation);
        }
    }

    /**
     * 获取 Sellers 站点参与状态列表。
     *
     * <p>Amazon 标准响应的 {@code payload} 本身为数组；兼容历史包装格式，避免接口代理层
     * 包装为 {@code payload.marketplaceParticipationList} 时丢失同步数据。</p>
     *
     * @param response Sellers API 原始响应
     * @return Marketplace 参与状态列表
     */
    private List<?> getParticipationList(Map<String, Object> response) {
        List<?> payload = getList(response, "payload");
        if (!payload.isEmpty()) {
            return payload;
        }
        Map<String, Object> payloadObject = getMap(response, "payload");
        return getList(payloadObject, "marketplaceParticipationList");
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
        AmazonShopMarketplaceDO record = marketplaceParticipationMapper
                .selectByShopIdAndMarketplaceId(shopId, marketplaceId);
        if (record == null) {
            record = new AmazonShopMarketplaceDO();
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
