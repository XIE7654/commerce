package cn.iocoder.yudao.module.amazon.sdk;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileMapper;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Amazon JSON 请求结果归档服务。
 *
 * <p>通过 Infra 的文件 API 写入当前主文件配置，因而可随配置自动保存到本地存储或 S3。</p>
 */
@Service
public class AmazonJsonStorageService {

    private static final DateTimeFormatter FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Resource
    private FileApi fileApi;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 将 Amazon 返回的 JSON 数据归档到当前租户的文件目录。
     *
     * @param category Amazon API 类型，用于区分归档目录
     * @param operation 调用场景，用于生成可读文件名
     * @param data 已反序列化的 JSON 数据
     * @return infra_file 文件编号
     */
    public Long persist(AmazonApiCategory category, String operation, Object data) {
        try {
            byte[] content = objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
            String url = fileApi.createFile(content, buildFileName(operation), buildDirectory(category), MediaType.APPLICATION_JSON_VALUE);
            FileDO file = fileMapper.selectOne(FileDO::getUrl, url);
            if (file == null) {
                // 文件服务写入 infra_file 后才会返回 URL；缺少记录时不能生成失效关联。
                throw new IllegalStateException("Amazon JSON 归档文件未写入 infra_file");
            }
            return file.getId();
        } catch (JacksonException ex) {
            throw new IllegalStateException("Amazon JSON 数据序列化失败", ex);
        }
    }

    /**
     * 构建归档目录，按租户和 Amazon API 类型隔离请求结果。
     *
     * @param category Amazon API 类型
     * @return 文件服务使用的相对目录
     */
    private String buildDirectory(AmazonApiCategory category) {
        Long tenantId = TenantContextHolder.getTenantId();
        String categoryName = category == null ? "request" : category.getDirectoryName();
        return "amazon/json/" + (tenantId == null ? 0L : tenantId) + "/" + categoryName;
    }

    /**
     * 构建唯一 JSON 文件名，防止并发请求覆盖归档文件。
     *
     * @param operation 调用场景
     * @return 不含路径的 JSON 文件名
     */
    private String buildFileName(String operation) {
        String safeOperation = sanitizeDirectoryName(operation);
        return safeOperation + "-" + FILE_TIME_FORMATTER.format(LocalDateTime.now())
                + "-" + UUID.randomUUID().toString().replace("-", "") + ".json";
    }

    /**
     * 清理目录和文件名中的路径分隔符，避免调用场景影响文件归档目录。
     *
     * @param value 原始类型或调用场景
     * @return 可安全用于目录或文件名的值
     */
    private String sanitizeDirectoryName(String value) {
        if (value == null || value.isBlank()) {
            return "request";
        }
        return value.replaceAll("[^A-Za-z0-9 _()-]", "_");
    }

}
