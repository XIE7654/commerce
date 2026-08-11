package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

/**
 * Marketplace 与卖家参与状态组合模型。
 */
@Data
public class MarketplaceParticipationDto {
    private MarketplaceDto marketplace;
    private ParticipationDto participation;
    private String storeName;
}
