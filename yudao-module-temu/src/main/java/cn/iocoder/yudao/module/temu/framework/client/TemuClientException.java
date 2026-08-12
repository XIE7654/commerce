package cn.iocoder.yudao.module.temu.framework.client;

/** Temu 客户端传输或响应转换异常。 */
public class TemuClientException extends RuntimeException {
    public TemuClientException(String message, Throwable cause) { super(message, cause); }
}
