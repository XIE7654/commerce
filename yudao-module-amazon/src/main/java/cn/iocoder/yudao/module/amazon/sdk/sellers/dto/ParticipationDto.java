package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

/**
 * 卖家 Marketplace 参与状态模型。
 */
@Data
public class ParticipationDto {
    private Boolean isParticipating;
    private Boolean hasSuspendedListings;
}
