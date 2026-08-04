package cn.iocoder.yudao.module.temu.sdk;

/**
 * Temu OpenAPI 请求异常。
 */
public class TemuApiException extends RuntimeException {

    /**
     * 创建带原因的请求异常。
     *
     * @param message 异常说明
     * @param cause 原始异常
     */
    public TemuApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
