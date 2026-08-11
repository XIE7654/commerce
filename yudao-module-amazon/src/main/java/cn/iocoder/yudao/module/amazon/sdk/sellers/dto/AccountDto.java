package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

import java.util.List;

/**
 * Sellers Account 账户模型。
 */
@Data
public class AccountDto {
    private String businessType;
    private String sellingPlan;
    private List<MarketplaceParticipationDto> marketplaceParticipationList;
    private BusinessDto business;
    private PrimaryContactDto primaryContact;
}
