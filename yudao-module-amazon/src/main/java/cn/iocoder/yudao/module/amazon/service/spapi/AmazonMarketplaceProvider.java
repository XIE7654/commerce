package cn.iocoder.yudao.module.amazon.service.spapi;

import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 根据沙盒开关选择当前环境下的 SP-API 端点。
 */
@Component
public class AmazonMarketplaceProvider {

    @Resource
    private AwsProperties properties;

    /**
     * 获取当前环境下的 SP-API 端点。
     *
     * @param marketplace 市场配置
     * @return 沙盒模式返回 sandbox 端点，否则返回生产端点
     */
    public String getEndpoint(AmazonMarketplaceEnum marketplace) {
        return properties.isSandboxMode() ? marketplace.getSandboxEndpoint() : marketplace.getEndpoint();
    }
}
