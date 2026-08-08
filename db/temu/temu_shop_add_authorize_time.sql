ALTER TABLE temu_shop
    ADD COLUMN IF NOT EXISTS authorize_time DATETIME NULL COMMENT '授权时间' AFTER auth_token,
    ADD COLUMN IF NOT EXISTS authorize_expire_time DATETIME NULL COMMENT '授权过期时间' AFTER authorize_time;
