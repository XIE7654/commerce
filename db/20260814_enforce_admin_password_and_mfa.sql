-- SP-API 数据保护要求：后台账号密码至少 12 位且包含特殊字符，并按 365 天轮换。
-- 历史密码无法从 bcrypt 摘要反向验证复杂度，统一标记为过期，要求用户通过受保护的重置流程设置新密码。
ALTER TABLE `system_users`
    ADD COLUMN `password_update_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '密码最近一次设置时间' AFTER `password`,
    ADD COLUMN `totp_secret` varchar(255) NULL COMMENT '加密保存的 TOTP Base32 密钥' AFTER `password_update_time`,
    ADD COLUMN `totp_enabled_time` datetime NULL COMMENT 'TOTP 确认绑定时间' AFTER `totp_secret`;

-- 历史账号需要先通过 /system/auth/totp/setup 和 /system/auth/totp/confirm 完成认证器绑定后才能重新登录。
UPDATE `system_users`
SET `password_update_time` = '2000-01-01 00:00:00';
