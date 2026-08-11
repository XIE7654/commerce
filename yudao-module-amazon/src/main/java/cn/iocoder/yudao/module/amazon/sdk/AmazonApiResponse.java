package cn.iocoder.yudao.module.amazon.sdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Amazon SDK 统一响应，封装业务数据、HTTP 语义状态和 Amazon 错误信息。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmazonApiResponse<T> {
    private Integer code;
    private T data;
    private String msg;
}
