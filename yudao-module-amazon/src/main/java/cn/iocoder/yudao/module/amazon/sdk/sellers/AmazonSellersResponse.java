package cn.iocoder.yudao.module.amazon.sdk.sellers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Sellers SDK 的统一响应封装，屏蔽 Amazon 原始 payload/errors 包装。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmazonSellersResponse<T> {
    private Integer code;
    private T data;
    private String msg;
}
