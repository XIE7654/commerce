package cn.iocoder.yudao.module.amazon.sdk.sellers.dto;

import lombok.Data;

/**
 * 卖家主要联系人模型。
 */
@Data
public class PrimaryContactDto {
    private String name;
    private String nonLatinName;
    private AddressDto address;
}
