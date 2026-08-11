package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

/**
 * Sellers 地址模型。
 */
@Data
public class AddressDto {
    private String addressLine1;
    private String addressLine2;
    private String countryCode;
    private String stateOrProvinceCode;
    private String city;
    private String postalCode;
}
