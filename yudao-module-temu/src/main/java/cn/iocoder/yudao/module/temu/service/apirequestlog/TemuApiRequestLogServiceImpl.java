package cn.iocoder.yudao.module.temu.service.apirequestlog;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.temu.controller.admin.apirequestlog.vo.*;
import cn.iocoder.yudao.module.temu.dal.dataobject.apirequestlog.TemuApiRequestLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.temu.dal.mysql.apirequestlog.TemuApiRequestLogMapper;
import cn.iocoder.yudao.module.temu.enums.TemuApiCategory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDateTime;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.temu.enums.ErrorCodeConstants.*;

/**
 * Temu OpenAPI 请求调用日志 Service 实现类
 *
 * @author 自达源码
 */
@Service
@Validated
@Slf4j
public class TemuApiRequestLogServiceImpl implements TemuApiRequestLogService {

    private static final int RESULT_SUCCESS = 1;
    private static final int RESULT_FAILURE = 2;
    private static final int ERROR_MESSAGE_MAX_LENGTH = 1000;
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "access_token", "app_key", "app_secret", "sign", "code", "authorization");

    @Resource
    private TemuApiRequestLogMapper apiRequestLogMapper;
    @Resource
    private ObjectMapper objectMapper;

    /** {@inheritDoc} */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void log(TemuApiRequestLogContext context, Integer httpStatusCode, HttpHeaders responseHeaders,
                    JsonNode responseBody, Throwable requestException) {
        try {
            LocalDateTime completedAt = LocalDateTime.now();
            TemuApiRequestLogDO logDO = new TemuApiRequestLogDO();
            logDO.setRequestId(context.requestId());
            logDO.setTraceId(MDC.get("traceId"));
            // 店铺编号由 SDK 创建客户端时传入，用于按店铺追踪 OpenAPI 调用记录。
            logDO.setShopId(context.shopId());
            logDO.setSite(context.site());
            logDO.setApiCategory(TemuApiCategory.fromApiType(context.apiType()).getDirectoryName());
            logDO.setOperationName(context.apiType());
            logDO.setRequestMethod(context.method());
            logDO.setRequestUrl(maskUrl(context.uri()));
            logDO.setRequestPath(context.uri().getRawPath());
            logDO.setRequestParams(toMaskedJson(context.requestParams()));
            logDO.setRequestHeaders(toMaskedJson(context.requestHeaders() == null
                    ? null : context.requestHeaders().toSingleValueMap()));
            logDO.setRequestBodyHash(sha256(context.requestParams()));
            logDO.setHttpStatusCode(httpStatusCode);
            logDO.setResultStatus(isFailure(responseBody, requestException) ? RESULT_FAILURE : RESULT_SUCCESS);
            logDO.setErrorCode(resolveErrorCode(responseBody, requestException));
            logDO.setErrorMessage(resolveErrorMessage(responseBody, requestException));
            logDO.setTemuRequestId(readResponseText(responseBody, "request_id", "requestId"));
            logDO.setRateLimit(responseHeaders == null ? null : responseHeaders.getFirst("X-RateLimit-Limit"));
            logDO.setRetryCount(0);
            logDO.setRequestedAt(context.requestedAt());
            logDO.setCompletedAt(completedAt);
            logDO.setDurationMs((int) Duration.between(context.requestedAt(), completedAt).toMillis());
            apiRequestLogMapper.insert(logDO);
        } catch (Exception logException) {
            // 审计链路故障不能反向影响 Temu 主调用，仅保留告警供运维排查。
            log.warn("记录 Temu API 请求日志失败，apiType={}", context.apiType(), logException);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFileId(String requestId, Long fileId) {
        if (requestId == null || fileId == null) return;
        apiRequestLogMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<TemuApiRequestLogDO>()
                .eq(TemuApiRequestLogDO::getRequestId, requestId)
                .set(TemuApiRequestLogDO::getFileId, fileId));
    }

    @Override
    public Long createApiRequestLog(TemuApiRequestLogSaveReqVO createReqVO) {
        // 插入
        TemuApiRequestLogDO apiRequestLog = BeanUtils.toBean(createReqVO, TemuApiRequestLogDO.class);
        apiRequestLogMapper.insert(apiRequestLog);

        // 返回
        return apiRequestLog.getId();
    }

    @Override
    public void updateApiRequestLog(TemuApiRequestLogSaveReqVO updateReqVO) {
        // 校验存在
        validateApiRequestLogExists(updateReqVO.getId());
        // 更新
        TemuApiRequestLogDO updateObj = BeanUtils.toBean(updateReqVO, TemuApiRequestLogDO.class);
        apiRequestLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteApiRequestLog(Long id) {
        // 校验存在
        validateApiRequestLogExists(id);
        // 删除
        apiRequestLogMapper.deleteById(id);
    }

    @Override
        public void deleteApiRequestLogListByIds(List<Long> ids) {
        // 删除
        apiRequestLogMapper.deleteByIds(ids);
        }


    private void validateApiRequestLogExists(Long id) {
        if (apiRequestLogMapper.selectById(id) == null) {
            throw exception(API_REQUEST_LOG_NOT_EXISTS);
        }
    }

    @Override
    public TemuApiRequestLogDO getApiRequestLog(Long id) {
        return apiRequestLogMapper.selectById(id);
    }

    @Override
    public PageResult<TemuApiRequestLogDO> getApiRequestLogPage(TemuApiRequestLogPageReqVO pageReqVO) {
        return apiRequestLogMapper.selectPage(pageReqVO);
    }

    /**
     * 将对象转换为 JSON；序列化失败时返回 {@code null}，避免审计故障影响业务调用。
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
     * 对对象中的敏感字段脱敏后转换为 JSON，防止 Token、签名和授权码落库。
     *
     * @param value 待脱敏的对象
     * @return 脱敏后的 JSON 文本
     */
    private String toMaskedJson(Object value) {
        return toJson(maskValue(value));
    }

    /**
     * 递归处理 Map 和集合中的敏感字段，保留非敏感参数以支持问题排查。
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
     * 脱敏 URL 查询参数中的敏感值，避免 GET 请求中的 Token 和签名被记录。
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
     * 根据字段名判断是否需要脱敏，兼容横线、下划线和驼峰命名。
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
     * 判断 Temu 响应或本地异常是否表示调用失败。
     *
     * @param responseBody Temu 响应体
     * @param requestException 调用异常
     * @return 调用失败时返回 {@code true}
     */
    private boolean isFailure(JsonNode responseBody, Throwable requestException) {
        return requestException != null || (responseBody != null && responseBody.path("success").isBoolean()
                && !responseBody.path("success").asBoolean());
    }

    /**
     * 获取 Temu 响应或本地异常中的错误码。
     *
     * @param responseBody Temu 响应体
     * @param requestException 调用异常
     * @return 错误码；调用成功时返回 {@code null}
     */
    private String resolveErrorCode(JsonNode responseBody, Throwable requestException) {
        return requestException == null ? readResponseText(responseBody, "error_code", "errorCode", "code")
                : requestException.getClass().getSimpleName();
    }

    /**
     * 获取 Temu 响应或本地异常中的错误信息，并按字段长度截断。
     *
     * @param responseBody Temu 响应体
     * @param requestException 调用异常
     * @return 脱敏且截断后的错误信息；调用成功时返回 {@code null}
     */
    private String resolveErrorMessage(JsonNode responseBody, Throwable requestException) {
        String message = requestException == null ? readResponseText(responseBody,
                "error_message", "error_msg", "errorMessage", "errorMsg", "message")
                : requestException.getMessage();
        return truncate(maskText(message));
    }

    /**
     * 从 Temu 顶层响应中读取第一个非空文本字段。
     *
     * @param responseBody Temu 响应体
     * @param fieldNames 候选字段名称
     * @return 字段文本；不存在时返回 {@code null}
     */
    private String readResponseText(JsonNode responseBody, String... fieldNames) {
        if (responseBody == null) {
            return null;
        }
        for (String fieldName : fieldNames) {
            JsonNode value = responseBody.path(fieldName);
            if (!value.isMissingNode() && !value.isNull() && value.isValueNode()) {
                String text = value.asText();
                if (!text.isBlank()) {
                    return text;
                }
            }
        }
        return null;
    }

    /**
     * 脱敏异常文本中形如 {@code key=value} 的敏感值。
     *
     * @param value 原始错误文本
     * @return 已脱敏的错误文本
     */
    private String maskText(String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        for (String key : SENSITIVE_KEYS) {
            String keyPattern = key.replace("_", "[-_]");
            result = result.replaceAll("(?i)(" + keyPattern + "=)[^,\\s&]+", "$1**");
        }
        return result;
    }

    /**
     * 截断错误信息以匹配数据库字段长度。
     *
     * @param value 原始错误信息
     * @return 截断后的错误信息
     */
    private String truncate(String value) {
        return value == null || value.length() <= ERROR_MESSAGE_MAX_LENGTH ? value : value.substring(0, ERROR_MESSAGE_MAX_LENGTH);
    }

    /**
     * 计算对象序列化内容的 SHA-256，用于关联重复请求且不保存原始敏感值。
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
