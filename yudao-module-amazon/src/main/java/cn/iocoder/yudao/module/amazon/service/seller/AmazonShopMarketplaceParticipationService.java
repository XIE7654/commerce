package cn.iocoder.yudao.module.amazon.service.seller;

import java.util.Map;
import java.util.List;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.MarketplaceParticipationDto;

/**
 * Amazon 店铺 Marketplace 参与状态同步 Service。
 */
public interface AmazonShopMarketplaceParticipationService {

    /**
     * 保存 Sellers 响应中的 Marketplace 参与状态。
     *
     * @param shopId 店铺编号
     * @param response Sellers API 原始响应
     */
    void syncMarketplaceParticipations(Long shopId, Map<String, Object> response);

    /** 保存已完成模型转换的 Marketplace 参与状态。 */
    void syncMarketplaceParticipations(Long shopId, List<MarketplaceParticipationDto> response);
}
