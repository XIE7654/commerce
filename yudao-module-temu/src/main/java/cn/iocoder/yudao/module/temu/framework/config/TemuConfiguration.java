package cn.iocoder.yudao.module.temu.framework.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Temu 模块配置类，负责注册 Temu 配置属性。
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TemuProperties.class)
public class TemuConfiguration {
}
