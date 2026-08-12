package cn.iocoder.yudao.module.temu.framework.client;

/** Temu 客户端传输或响应转换异常。 */
public class TemuClientException extends RuntimeException {
    /**
     * 创建不包含底层异常原因的 Temu 客户端异常。
     *
     * @param message 面向调用方的失败原因
     */
    public TemuClientException(String message) {
        super(message);
    }

    /**
     * 创建包含底层异常原因的 Temu 客户端异常。
     *
     * @param message 面向调用方的失败原因
     * @param cause 底层传输或数据转换异常
     */
    public TemuClientException(String message, Throwable cause) { super(message, cause); }
}
