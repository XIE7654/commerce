package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

/**
 * 卖家企业档案模型。
 */
@Data
public class BusinessDto {
    private String name;
    private String nonLatinName;
    private AddressDto registeredBusinessAddress;
    private String companyRegistrationNumber;
    private String companyTaxIdentificationNumber;
}
