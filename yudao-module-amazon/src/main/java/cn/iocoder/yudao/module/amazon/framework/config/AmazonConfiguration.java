package cn.iocoder.yudao.module.amazon.framework.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Amazon 模块配置入口，负责注册配置属性。
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AwsProperties.class)
public class AmazonConfiguration {
}
