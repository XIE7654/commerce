package cn.iocoder.yudao.module.amazon.framework.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link AwsProperties} 沙盒配置切换测试。
 */
class AwsPropertiesTest {

    @Test
    void productionModeUsesProdConfig() {
        AwsProperties properties = new AwsProperties();
        properties.getProd().setAppId("prod-app");
        properties.getProd().setClientId("prod-client");
        properties.getSandbox().setAppId("sandbox-app");
        properties.getSandbox().setClientId("sandbox-client");

        assertEquals("prod-app", properties.getAppId());
        assertEquals("prod-client", properties.getClientId());
        assertFalse(properties.isSandboxMode());
    }

    @Test
    void sandboxModeUsesSandboxConfig() {
        AwsProperties properties = new AwsProperties();
        properties.setSandboxMode(true);
        properties.getProd().setAppId("prod-app");
        properties.getProd().setClientId("prod-client");
        properties.getSandbox().setAppId("sandbox-app");
        properties.getSandbox().setClientId("sandbox-client");
        properties.getSandbox().setAdTokenUrl("https://sandbox.example.com/token");
        properties.getSandbox().setStoreTokenUrl("https://sandbox.example.com/auth/o2/token");

        assertEquals("sandbox-app", properties.getAppId());
        assertEquals("sandbox-client", properties.getClientId());
        assertEquals("https://sandbox.example.com/token", properties.getAdTokenUrl());
        assertEquals("https://sandbox.example.com/auth/o2/token", properties.getStoreTokenUrl());
    }

    @Test
    void defaultNumericValuesAreUsedWhenEnvironmentConfigUnset() {
        AwsProperties properties = new AwsProperties();

        assertEquals(600, properties.getStateExpires());
        assertEquals(3100, properties.getRefreshTokenExpires());
        assertEquals("https://api.amazon.com/auth/o2/token", properties.getStoreTokenUrl());
    }

    @Test
    void bindsProdAndSandboxFromConfiguration() {
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("aws.sandbox-mode", "true");
        source.put("aws.prod.client-id", "prod-client");
        source.put("aws.sandbox.client-id", "sandbox-client");
        AwsProperties properties = new Binder(new MapConfigurationPropertySource(source))
                .bind("aws", Bindable.of(AwsProperties.class))
                .get();

        assertTrue(properties.isSandboxMode());
        assertEquals("prod-client", properties.getProd().getClientId());
        assertEquals("sandbox-client", properties.getClientId());
    }

    @Test
    void blankNumericValuesBindToDefaults() {
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("aws.sandbox-mode", "true");
        source.put("aws.sandbox.state-expires", "");
        source.put("aws.sandbox.refresh-token-expires", "");
        AwsProperties properties = new Binder(new MapConfigurationPropertySource(source))
                .bind("aws", Bindable.of(AwsProperties.class))
                .get();

        assertEquals(600, properties.getStateExpires());
        assertEquals(3100, properties.getRefreshTokenExpires());
    }
}
