package cn.iocoder.yudao.module.amazon.service.apilog;

import org.springframework.http.HttpHeaders;

/**
 * Amazon 外部 API 请求日志服务。
 */
public interface AmazonApiRequestLogService {

    /**
     * 记录一次 Amazon 外部 HTTP 调用的最终结果；日志写入失败不得影响原始调用。
     *
     * @param context 请求审计上下文
     * @param httpStatusCode HTTP 状态码，网络异常时为空
     * @param responseHeaders 响应头，网络异常时为空
     * @param exception 请求异常，成功时为空
     */
    void log(AmazonApiRequestLogContext context, Integer httpStatusCode, HttpHeaders responseHeaders, Throwable exception);
}
