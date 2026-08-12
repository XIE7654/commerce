package cn.iocoder.yudao.module.temu.framework.client.auth;

import lombok.Data;

/**
 * 创建 access token 结果。
 */
@Data
public class AccessTokenCreateResult {
    private String accessToken;
    private Long mallId;
    private Long expiredTime;
    private String appSubscribeStatus;
}
