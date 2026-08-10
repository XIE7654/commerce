package cn.iocoder.yudao.module.amazon.utils;

import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * Amazon SP-API ISO 8601 日期时间校验工具。
 */
public final class AmazonDateTimeUtils {

    private AmazonDateTimeUtils() {
    }

    /**
     * 校验可选日期时间区间的格式和上下限关系。
     *
     * @param after 下限时间
     * @param before 上限时间
     * @param afterName 下限参数名称
     * @param beforeName 上限参数名称
     */
    public static void validateRange(String after, String before, String afterName, String beforeName) {
        OffsetDateTime afterTime = parseOptional(after, afterName);
        OffsetDateTime beforeTime = parseOptional(before, beforeName);
        if (afterTime != null && beforeTime != null && beforeTime.isBefore(afterTime)) {
            throw new IllegalArgumentException(beforeName + " 不能早于 " + afterName);
        }
    }

    /**
     * 解析可选 ISO 8601 日期时间。
     *
     * @param value 待解析时间
     * @param name 参数名称
     * @return 解析结果；空白值返回 {@code null}
     */
    public static OffsetDateTime parseOptional(String value, String name) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(name + " 必须为 ISO 8601 日期时间格式", exception);
        }
    }

    /**
     * 解析 Amazon 响应中的可选 ISO 8601 时间；格式不规范的数据不影响整批业务同步。
     *
     * @param value Amazon 响应时间字符串
     * @return 本地日期时间；空白或格式无效时返回 {@code null}
     */
    public static LocalDateTime parseOrNull(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value).toLocalDateTime();
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

}
