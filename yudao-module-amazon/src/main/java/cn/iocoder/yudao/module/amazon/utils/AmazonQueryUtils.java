package cn.iocoder.yudao.module.amazon.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Amazon SP-API 查询参数工具，统一处理空值过滤、排序和 RFC 3986 编码。
 */
public final class AmazonQueryUtils {

    private AmazonQueryUtils() {
    }

    /**
     * 构建按键名排序的 RFC 3986 查询字符串。
     *
     * @param query 原始查询参数
     * @return 不含问号的查询字符串；无有效参数时返回空串
     */
    public static String buildQuery(Map<String, String> query) {
        if (CollUtil.isEmpty(query)) {
            return "";
        }
        return new TreeMap<>(query).entrySet().stream()
                .filter(entry -> StrUtil.isNotBlank(entry.getValue()))
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    /**
     * 仅在参数值非空白时写入查询参数，避免发送 Amazon 不接受的空筛选条件。
     *
     * @param query 查询参数集合
     * @param key 参数名称
     * @param value 参数值
     */
    public static void putIfNotBlank(Map<String, String> query, String key, String value) {
        if (StrUtil.isNotBlank(value)) {
            query.put(key, value);
        }
    }

    /**
     * 将字符串列表转换为 Amazon 约定的逗号分隔参数。
     *
     * @param values 参数值列表
     * @return 逗号分隔值；空集合返回 {@code null}
     */
    public static String join(Collection<String> values) {
        return CollUtil.isEmpty(values) ? null : String.join(",", values);
    }

    /**
     * 使用 UTF-8 对查询参数执行 RFC 3986 兼容的百分号编码。
     *
     * @param value 原始参数值
     * @return 编码后的参数值
     */
    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("%7E", "~");
    }

}
