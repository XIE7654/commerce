package cn.iocoder.yudao.module.temu.framework.client.auth;

import lombok.Data;

import java.util.List;

/**
 * access token 授权信息结果。
 */
@Data
public class AccessTokenInfoResult {
    private String semiUniqueId;
    private Integer regionId;
    private Long mallId;
    private Integer mallType;
    private Integer appSubscribeStatus;
    private Long expiredTime;
    /** 平台订阅事件编码列表。 */
    private List<String> appSubscribeEventCodeList;
    /** 授权事件编码及授权状态列表。 */
    private List<AuthEventCode> authEventCodeList;
    /** 已授权 API 权限范围列表。 */
    private List<String> apiScopeList;
}
