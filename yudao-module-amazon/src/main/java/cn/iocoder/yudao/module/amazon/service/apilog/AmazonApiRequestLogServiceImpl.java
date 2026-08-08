package cn.iocoder.yudao.module.amazon.service.apilog;

import cn.iocoder.yudao.module.amazon.dal.dataobject.apilog.AmazonApiRequestLogDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.apilog.AmazonApiRequestLogMapper;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Amazon 外部 API 请求日志服务实现。
 */
@Service
@Slf4j
public class AmazonApiRequestLogServiceImpl implements AmazonApiRequestLogService {

    private static final int RESULT_SUCCESS = 1;
    private static final int RESULT_FAILURE = 2;
    private static final int ERROR_MESSAGE_MAX_LENGTH = 1000;
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "authorization", "access_token", "refresh_token", "client_id", "client_secret", "code",
            "x_amz_access_token", "x_amz_security_token", "x_amz_signature", "signature");

    @Resource
    private AmazonApiRequestLogMapper amazonApiRequestLogMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AwsProperties awsProperties;

    /** {@inheritDoc} */
    @Override
    public void log(AmazonApiRequestLogContext context, Integer httpStatusCode, HttpHeaders responseHeaders, Throwable exception) {
        try {
            LocalDateTime completedAt = LocalDateTime.now();
            AmazonApiRequestLogDO logDO = new AmazonApiRequestLogDO();
            boolean oauthTokenRequest = isOAuthTokenRequest(context);
            logDO.setRequestId(UUID.randomUUID().toString().replace("-", ""));
            logDO.setTraceId(MDC.get("traceId"));
            logDO.setShopId(context.shopId());
            logDO.setCountryCode(context.countryCode());
            logDO.setMarketplaceIds(toJson(context.marketplaceIds()));
            logDO.setApiCategory(context.apiCategory());
            logDO.setOperationName(context.operationName());
            logDO.setRequestMethod(context.method());
            // OAuth Token 请求包含应用凭据并使用配置端点；审计只保留结果，避免任何配置值进入日志表。
            logDO.setRequestUrl(oauthTokenRequest ? null : maskUrl(context.uri()));
            logDO.setRequestPath(oauthTokenRequest ? null : context.uri().getRawPath());
            logDO.setFileId(context.fileId());
            logDO.setRequestParams(oauthTokenRequest ? null : toMaskedJson(context.requestParams()));
            logDO.setRequestHeaders(oauthTokenRequest ? null
                    : toMaskedJson(context.requestHeaders() == null ? null : context.requestHeaders().toSingleValueMap()));
            logDO.setRequestBodyHash(oauthTokenRequest ? null : sha256(context.requestParams()));
            logDO.setHttpStatusCode(httpStatusCode);
            logDO.setResultStatus(exception == null ? RESULT_SUCCESS : RESULT_FAILURE);
            logDO.setErrorCode(exception == null ? null : exception.getClass().getSimpleName());
            logDO.setErrorMessage(exception == null ? null : truncate(maskText(exception.getMessage())));
            logDO.setAmazonRequestId(responseHeaders == null ? null : responseHeaders.getFirst("x-amzn-RequestId"));
            logDO.setRateLimit(responseHeaders == null ? null : responseHeaders.getFirst("x-amzn-RateLimit-Limit"));
            logDO.setRetryCount(0);
            logDO.setRequestedAt(context.requestedAt());
            logDO.setCompletedAt(completedAt);
            logDO.setDurationMs((int) Duration.between(context.requestedAt(), completedAt).toMillis());
            amazonApiRequestLogMapper.insert(logDO);
        } catch (Exception logException) {
            // 外部调用日志不能反向影响 Amazon 主流程；保留告警以便排查审计链路异常。
            log.warn("记录 Amazon API 请求日志失败，operation={}", context.operationName(), logException);
        }
    }

    /**
     * 将对象转换为 JSON；JSON 序列化失败时返回 {@code null}，避免审计故障影响业务调用。
     *
     * @param value 待序列化的对象
     * @return JSON 文本或 {@code null}
     */
    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JacksonException exception) {
            return null;
        }
    }

    /**
     * 对对象中的敏感字段脱敏后转换为 JSON，避免 Token、授权码和密钥落库。
     *
     * @param value 待脱敏的对象
     * @return 脱敏后的 JSON 文本
     */
    private String toMaskedJson(Object value) {
        return toJson(maskValue(value));
    }

    /**
     * 递归处理 Map 和集合中的敏感键名，保持非敏感字段供问题排查使用。
     *
     * @param value 原始对象
     * @return 脱敏后的对象
     */
    private Object maskValue(Object value) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((key, item) -> {
                String name = String.valueOf(key);
                result.put(name, isSensitiveKey(name) ? "**" : maskValue(item));
            });
            return result;
        }
        if (value instanceof Collection<?> collection) {
            return collection.stream().map(this::maskValue).toList();
        }
        return value;
    }

    /**
     * 脱敏 URL 查询参数中的敏感值，避免签名和 Token 随完整 URL 记录。
     *
     * @param uri 原始请求 URI
     * @return 脱敏后的请求 URL
     */
    private String maskUrl(URI uri) {
        String query = uri.getRawQuery();
        if (query == null || query.isBlank()) {
            return uri.toString();
        }
        StringBuilder maskedQuery = new StringBuilder();
        for (String part : query.split("&")) {
            if (!maskedQuery.isEmpty()) {
                maskedQuery.append('&');
            }
            int separatorIndex = part.indexOf('=');
            String name = separatorIndex < 0 ? part : part.substring(0, separatorIndex);
            maskedQuery.append(name);
            if (separatorIndex >= 0) {
                maskedQuery.append('=').append(isSensitiveKey(name) ? "**" : part.substring(separatorIndex + 1));
            }
        }
        return uri.toString().replace(query, maskedQuery.toString());
    }

    /**
     * 根据键名判断字段是否敏感，兼容常见的横线和下划线命名风格。
     *
     * @param key 字段名称
     * @return 敏感字段时返回 {@code true}
     */
    private boolean isSensitiveKey(String key) {
        String normalizedKey = key.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace('-', '_').toLowerCase(Locale.ROOT);
        return SENSITIVE_KEYS.contains(normalizedKey);
    }

    /**
     * 对异常文本中形如 {@code key=value} 的敏感值进行替换。
     *
     * @param value 原始错误文本
     * @return 脱敏后的错误文本
     */
    private String maskText(String value) {
        if (value == null) {
            return null;
        }
        String result = maskConfiguredValues(value);
        for (String key : SENSITIVE_KEYS) {
            String keyPattern = key.replace("_", "[-_]");
            result = result.replaceAll("(?i)(" + keyPattern + "=)[^,\\s&]+", "$1**");
        }
        return result;
    }

    /**
     * 判断是否为 OAuth Token 请求。
     *
     * <p>此类请求的表单含有客户端凭据，且 URL 来自 AWS 配置，必须整段排除出请求日志。</p>
     *
     * @param context Amazon 请求日志上下文
     * @return 是 OAuth Token 请求时返回 {@code true}
     */
    private boolean isOAuthTokenRequest(AmazonApiRequestLogContext context) {
        return "tokens".equals(context.apiCategory()) && "requestToken".equals(context.operationName());
    }

    /**
     * 从异常信息中移除 AWS 字符串配置值，避免客户端库将请求地址或凭据回显到日志表。
     *
     * @param value 原始异常信息
     * @return 已替换配置值的异常信息
     */
    private String maskConfiguredValues(String value) {
        String result = value;
        for (String configuredValue : new String[]{awsProperties.getAppId(), awsProperties.getClientId(),
                awsProperties.getClientSecret(), awsProperties.getAdClientId(), awsProperties.getAdClientSecret(),
                awsProperties.getSellerAuthLoginUri(), awsProperties.getAdAuthLoginUri(), awsProperties.getCryptoKey(),
                awsProperties.getStoreTokenUrl(), awsProperties.getAdTokenUrl()}) {
            if (configuredValue != null && !configuredValue.isBlank()) {
                result = result.replace(configuredValue, "**");
            }
        }
        return result;
    }

    /**
     * 截断错误信息以匹配数据库字段长度。
     *
     * @param value 错误信息
     * @return 截断后的错误信息
     */
    private String truncate(String value) {
        return value == null || value.length() <= ERROR_MESSAGE_MAX_LENGTH ? value : value.substring(0, ERROR_MESSAGE_MAX_LENGTH);
    }

    /**
     * 计算对象序列化内容的 SHA-256，用于在不保存原文时关联重复请求或响应。
     *
     * @param value 待计算摘要的对象
     * @return 十六进制 SHA-256；无法序列化时返回 {@code null}
     */
    private String sha256(Object value) {
        String json = toJson(value);
        if (json == null) {
            return null;
        }
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(json.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(digest.length * 2);
            for (byte item : digest) {
                result.append(String.format("%02x", item));
            }
            return result.toString();
        } catch (Exception exception) {
            return null;
        }
    }
}
