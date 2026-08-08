package cn.iocoder.yudao.module.amazon.service.apilog;

import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Amazon 外部 HTTP 请求的审计上下文。
 *
 * @param operationName Amazon API 操作名称
 * @param apiCategory Amazon API 分类
 * @param method HTTP 请求方式
 * @param uri 请求 URI
 * @param shopId 店铺编号，可为空
 * @param countryCode 站点国家代码，可为空
 * @param marketplaceIds Marketplace ID 列表，可为空
 * @param requestParams 请求参数或请求体
 * @param requestHeaders 请求头
 * @param requestedAt 请求开始时间
 */
public record AmazonApiRequestLogContext(String operationName, String apiCategory, String method, URI uri,
                                         Long shopId, String countryCode, List<String> marketplaceIds,
                                         Object requestParams, HttpHeaders requestHeaders, LocalDateTime requestedAt) {
}
