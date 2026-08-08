package cn.iocoder.yudao.module.temu.service.apirequestlog;

import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * Temu OpenAPI 请求的审计上下文。
 *
 * @param apiType Temu OpenAPI 接口 type
 * @param method HTTP 请求方式
 * @param uri 请求 URI
 * @param site Temu 站点代码，可为空
 * @param requestParams 请求参数或请求体
 * @param requestHeaders 请求头
 * @param requestedAt 请求开始时间
 */
public record TemuApiRequestLogContext(String apiType, String method, URI uri, String site,
                                       Object requestParams, HttpHeaders requestHeaders, LocalDateTime requestedAt) {
}
