package cn.iocoder.yudao.module.amazon.sdk.orders.dto;

import lombok.Data;

/** Amazon 金额模型，金额以字符串返回以避免浮点精度损失。 */
@Data
public class MoneyDto {
    private String currencyCode;
    private String amount;
}
