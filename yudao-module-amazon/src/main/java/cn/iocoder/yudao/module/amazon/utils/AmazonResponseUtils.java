package cn.iocoder.yudao.module.amazon.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Amazon SP-API 动态 JSON 响应读取工具。
 */
public final class AmazonResponseUtils {

    private AmazonResponseUtils() {
    }

    /**
     * 读取嵌套 JSON 对象；字段缺失或类型不符时返回空对象。
     *
     * @param source 源 JSON 对象
     * @param key 字段名称
     * @return 字段对应的 Map
     */
    public static Map<String, Object> getMap(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof Map<?, ?> map ? toMap(map) : Map.of();
    }

    /**
     * 读取 JSON 数组；字段缺失或类型不符时返回空列表。
     *
     * @param source 源 JSON 对象
     * @param key 字段名称
     * @return 字段对应的列表
     */
    public static List<?> getList(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof List<?> list ? list : List.of();
    }

    /**
     * 读取 JSON 字符串字段。
     *
     * @param source 源 JSON 对象
     * @param key 字段名称
     * @return 字段值；缺失时返回 {@code null}
     */
    public static String getString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value == null ? null : String.valueOf(value);
    }

    /**
     * 获取 Amazon 响应业务数据，兼容直接响应与封装在 {@code payload} 中的响应。
     *
     * @param response Amazon API 响应
     * @return 包含业务字段的响应对象
     */
    public static Map<String, Object> getPayload(Map<String, Object> response) {
        Map<String, Object> payload = getMap(response, "payload");
        return payload.isEmpty() ? response : payload;
    }

    /**
     * 将未知泛型的 JSON 对象转换为字符串键 Map。
     *
     * @param source JSON 对象
     * @return 可读取的 Map
     */
    public static Map<String, Object> toMap(Map<?, ?> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        source.forEach((key, value) -> result.put(String.valueOf(key), value));
        return result;
    }
}
