package cn.iocoder.yudao.module.temu.framework.client.auth;

import lombok.Data;

/** Temu 授权事件及其许可状态。 */
@Data
public class AuthEventCode {
    /** 事件编码。 */
    private String eventCode;
    /** 许可状态，Temu 返回 1 表示允许。 */
    private Integer permitsStatus;
}
