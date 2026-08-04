package cn.iocoder.yudao.module.temu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Temu 站点区域、接口地址和中文国家名称枚举。
 *
 * <p>枚举常量名称使用 Temu 文档中的站点代码，便于根据站点代码直接定位配置。</p>
 */
@Getter
@AllArgsConstructor
public enum TemuSiteRegionEnum {

    US("US", "https://openapi-b-us.temu.com/openapi/router", "美国"),

    MX("GLOBAL", "https://openapi-b-global.temu.com/openapi/router", "墨西哥"),
    KR("GLOBAL", "https://openapi-b-global.temu.com/openapi/router", "韩国"),
    JP("GLOBAL", "https://openapi-b-global.temu.com/openapi/router", "日本"),
    CA("GLOBAL", "https://openapi-b-global.temu.com/openapi/router", "加拿大"),
    AU("GLOBAL", "https://openapi-b-global.temu.com/openapi/router", "澳大利亚"),
    BR("GLOBAL", "https://openapi-b-global.temu.com/openapi/router", "巴西"),

    DE("EU", "https://openapi-b-eu.temu.com/openapi/router", "德国"),
    FR("EU", "https://openapi-b-eu.temu.com/openapi/router", "法国"),
    IT("EU", "https://openapi-b-eu.temu.com/openapi/router", "意大利"),
    ES("EU", "https://openapi-b-eu.temu.com/openapi/router", "西班牙"),
    BE("EU", "https://openapi-b-eu.temu.com/openapi/router", "比利时"),
    AT("EU", "https://openapi-b-eu.temu.com/openapi/router", "奥地利"),
    RO("EU", "https://openapi-b-eu.temu.com/openapi/router", "罗马尼亚"),
    NL("EU", "https://openapi-b-eu.temu.com/openapi/router", "荷兰"),
    PL("EU", "https://openapi-b-eu.temu.com/openapi/router", "波兰"),
    PT("EU", "https://openapi-b-eu.temu.com/openapi/router", "葡萄牙"),
    HU("EU", "https://openapi-b-eu.temu.com/openapi/router", "匈牙利"),
    CZ("EU", "https://openapi-b-eu.temu.com/openapi/router", "捷克"),
    DK("EU", "https://openapi-b-eu.temu.com/openapi/router", "丹麦"),
    SE("EU", "https://openapi-b-eu.temu.com/openapi/router", "瑞典"),
    GR("EU", "https://openapi-b-eu.temu.com/openapi/router", "希腊"),
    SK("EU", "https://openapi-b-eu.temu.com/openapi/router", "斯洛伐克"),
    GB("EU", "https://openapi-b-eu.temu.com/openapi/router", "英国"),
    FI("EU", "https://openapi-b-eu.temu.com/openapi/router", "芬兰"),
    TR("EU", "https://openapi-b-eu.temu.com/openapi/router", "土耳其"),
    SI("EU", "https://openapi-b-eu.temu.com/openapi/router", "斯洛文尼亚"),
    IE("EU", "https://openapi-b-eu.temu.com/openapi/router", "爱尔兰"),
    LT("EU", "https://openapi-b-eu.temu.com/openapi/router", "立陶宛"),
    HR("EU", "https://openapi-b-eu.temu.com/openapi/router", "克罗地亚"),
    EE("EU", "https://openapi-b-eu.temu.com/openapi/router", "爱沙尼亚"),
    CH("EU", "https://openapi-b-eu.temu.com/openapi/router", "瑞士"),
    AE("EU", "https://openapi-b-eu.temu.com/openapi/router", "阿联酋"),
    KAS("EU", "https://openapi-b-eu.temu.com/openapi/router", "阿联酋"),
    LVA("EU", "https://openapi-b-eu.temu.com/openapi/router", "拉脱维亚"),
    CY("EU", "https://openapi-b-eu.temu.com/openapi/router", "塞浦路斯"),
    NO("EU", "https://openapi-b-eu.temu.com/openapi/router", "挪威"),
    BG("EU", "https://openapi-b-eu.temu.com/openapi/router", "保加利亚"),
    LU("EU", "https://openapi-b-eu.temu.com/openapi/router", "卢森堡"),
    IS("EU", "https://openapi-b-eu.temu.com/openapi/router", "冰岛");

    /**
     * Temu 站点区域。
     */
    private final String siteRegion;
    /**
     * Temu OpenAPI 接口地址。
     */
    private final String endpoint;
    /**
     * 站点对应的中文国家名称。
     */
    private final String countryName;

}
