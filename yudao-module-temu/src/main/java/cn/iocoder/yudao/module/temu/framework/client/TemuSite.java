package cn.iocoder.yudao.module.temu.framework.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Temu 站点与 OpenAPI Router 地址映射。 */
@Getter
@RequiredArgsConstructor
public enum TemuSite {
    US("US", "https://openapi-b-us.temu.com/openapi/router"),
    MX("GLOBAL", "https://openapi-b-global.temu.com/openapi/router"), KR("GLOBAL", "https://openapi-b-global.temu.com/openapi/router"),
    JP("GLOBAL", "https://openapi-b-global.temu.com/openapi/router"), CA("GLOBAL", "https://openapi-b-global.temu.com/openapi/router"),
    AU("GLOBAL", "https://openapi-b-global.temu.com/openapi/router"), BR("GLOBAL", "https://openapi-b-global.temu.com/openapi/router"),
    DE("EU", "https://openapi-b-eu.temu.com/openapi/router"), FR("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    IT("EU", "https://openapi-b-eu.temu.com/openapi/router"), ES("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    BE("EU", "https://openapi-b-eu.temu.com/openapi/router"), AT("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    RO("EU", "https://openapi-b-eu.temu.com/openapi/router"), NL("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    PL("EU", "https://openapi-b-eu.temu.com/openapi/router"), PT("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    HU("EU", "https://openapi-b-eu.temu.com/openapi/router"), CZ("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    DK("EU", "https://openapi-b-eu.temu.com/openapi/router"), SE("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    GR("EU", "https://openapi-b-eu.temu.com/openapi/router"), SK("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    UK("EU", "https://openapi-b-eu.temu.com/openapi/router"), GB("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    FI("EU", "https://openapi-b-eu.temu.com/openapi/router"), TR("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    SI("EU", "https://openapi-b-eu.temu.com/openapi/router"), IE("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    LT("EU", "https://openapi-b-eu.temu.com/openapi/router"), HR("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    EE("EU", "https://openapi-b-eu.temu.com/openapi/router"), CH("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    AE("EU", "https://openapi-b-eu.temu.com/openapi/router"), KAS("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    LVA("EU", "https://openapi-b-eu.temu.com/openapi/router"), CY("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    NO("EU", "https://openapi-b-eu.temu.com/openapi/router"), BG("EU", "https://openapi-b-eu.temu.com/openapi/router"),
    LU("EU", "https://openapi-b-eu.temu.com/openapi/router"), IS("EU", "https://openapi-b-eu.temu.com/openapi/router");

    private final String areaGroup;
    private final String endpoint;

    /** 按站点代码解析枚举。 */
    public static TemuSite from(String site) {
        if (site == null || site.isBlank()) throw new IllegalArgumentException("Temu site 不能为空");
        try { return valueOf(site.trim().toUpperCase(java.util.Locale.ROOT)); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("不支持的 Temu site: " + site, ex); }
    }
}
