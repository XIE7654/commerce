package cn.iocoder.yudao.module.temu.framework.client;

import lombok.Data;

/** Temu API 通用响应包装。 */
@Data
public class TemuApiResponse<T> {
    private Boolean success;
    private String requestId;
    private Integer errorCode;
    private String errorMsg;
    private T result;
}
