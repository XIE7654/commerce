package cn.iocoder.yudao.module.temu.sdk;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.temu.enums.TemuApiCategory;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** Temu OpenAPI JSON 响应归档服务。 */
@Service
public class TemuJsonStorageService {

    private static final DateTimeFormatter FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Resource
    private FileApi fileApi;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 将 Temu 返回的 JSON 数据按租户和文档菜单归档。
     *
     * <p>只保存响应内容，避免将 access_token、签名等请求敏感信息写入文件。</p>
     *
     * @param apiType Temu OpenAPI 接口 type，用于确定目录和文件名
     * @param response Temu 已解析的响应 JSON
     */
    public Long persist(String apiType, JsonNode response) {
        try {
            byte[] content = objectMapper.writeValueAsString(response).getBytes(StandardCharsets.UTF_8);
            TemuApiCategory category = TemuApiCategory.fromApiType(apiType);
            return fileApi.createFileId(content, buildFileName(apiType), buildDirectory(category), MediaType.APPLICATION_JSON_VALUE);
        } catch (JacksonException ex) {
            throw new IllegalStateException("Temu JSON 响应序列化失败", ex);
        }
    }

    /**
     * 构建文件服务使用的相对目录，按租户隔离归档数据。
     *
     * @param category Temu API 文档菜单分类
     * @return 文件归档相对目录
     */
    private String buildDirectory(TemuApiCategory category) {
        Long tenantId = TenantContextHolder.getTenantId();
        return "temu/json/" + (tenantId == null ? 0L : tenantId) + "/" + category.getDirectoryName();
    }

    /**
     * 构建唯一的 JSON 文件名，避免同一接口的并发调用相互覆盖。
     *
     * @param apiType Temu OpenAPI 接口 type
     * @return 不含路径的 JSON 文件名
     */
    private String buildFileName(String apiType) {
        return sanitizeFileName(apiType) + "-" + FILE_TIME_FORMATTER.format(LocalDateTime.now())
                + "-" + UUID.randomUUID().toString().replace("-", "") + ".json";
    }

    /**
     * 清理接口 type 中不适合作为文件名的字符。
     *
     * @param value 原始接口 type
     * @return 可安全用于文件名的字符串
     */
    private String sanitizeFileName(String value) {
        if (value == null || value.isBlank()) {
            return "response";
        }
        return value.replaceAll("[^A-Za-z0-9._()-]", "_");
    }

}
