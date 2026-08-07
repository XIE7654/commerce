-- 将管理后台默认 OAuth2 客户端的访问令牌有效期调整为 24 小时。
-- 仅影响后续以 default 客户端登录时签发的 access token，不影响 Amazon OAuth 配置或已有令牌。
UPDATE system_oauth2_client
SET access_token_validity_seconds = 86400
WHERE client_id = 'default'
  AND deleted = 0;
