package cn.iocoder.yudao.module.temu.sdk;

import lombok.Data;

/** Temu OpenAPI 通用响应包装，业务结果由 {@code result} 泛型承载。 */
@Data
public class TemuApiResponse<T> {

    /** 请求是否成功。 */
    private Boolean success;

    /** Temu 请求标识，用于排查平台侧请求。 */
    private String requestId;

    /** Temu 业务错误码。 */
    private Integer errorCode;

    /** Temu 业务错误信息。 */
    private String errorMsg;

    /** 具体接口返回的业务结果。 */
    private T result;
}
