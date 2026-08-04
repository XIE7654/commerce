package cn.iocoder.yudao.module.temu.framework.config;

import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Temu OpenAPI 区域配置。
 *
 * <p>配置前缀为 {@code temu}，区域配置通过枚举中的 {@code siteRegion} 进行匹配。</p>
 */
@Data
@ConfigurationProperties(prefix = "temu")
public class TemuProperties {

    /**
     * 按 US、GLOBAL、EU 保存的区域应用配置。
     */
    private Map<String, RegionProperties> regions = new HashMap<>();

    /**
     * 根据 Temu 站点枚举获取对应区域的应用配置。
     *
     * @param site Temu 站点枚举
     * @return 站点所属区域配置；传入 {@code null} 或未配置区域时返回 {@code null}
     */
    public RegionProperties getRegion(TemuSiteRegionEnum site) {
        if (site == null) {
            return null;
        }
        RegionProperties region = regions.get(site.getSiteRegion());
        if (region != null) {
            return region;
        }
        // 兼容配置绑定过程中区域键大小写发生变化的情况。
        return regions.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(site.getSiteRegion()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 单个 Temu 区域的应用配置。
     */
    @Data
    public static class RegionProperties {

        /**
         * Temu 应用标识。
         */
        private String appKey;
        /**
         * Temu 应用密钥。
         */
        private String appSecret;

    }

}
