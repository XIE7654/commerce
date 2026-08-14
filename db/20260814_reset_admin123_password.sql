-- 重置租户 1 中 admin123 的后台密码，并使其重新完成 TOTP 绑定。
-- 新密码：TemuAdmin!2026Secure
START TRANSACTION;

UPDATE `system_users`
SET `password` = '$2y$04$y9VeX7JqeHjRYE8aDJkVv.e9H2tBBDMHCs1iFahtNcZsDiPdbLfim',
    `password_update_time` = NOW(),
    `totp_secret` = NULL,
    `totp_enabled_time` = NULL
WHERE `username` = 'admin'
  AND `tenant_id` = 1
  AND `deleted` = b'0'
LIMIT 1;

-- 密码变更后撤销旧访问令牌和刷新令牌。
DELETE token
FROM `system_oauth2_access_token` AS token
INNER JOIN `system_users` AS user ON user.`id` = token.`user_id`
WHERE user.`username` = 'admin'
  AND user.`tenant_id` = 1
  AND user.`deleted` = b'0'
  AND token.`tenant_id` = 1
  AND token.`user_type` = 1;

COMMIT;
