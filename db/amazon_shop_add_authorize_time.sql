ALTER TABLE amazon_shop
    ADD COLUMN IF NOT EXISTS authorize_time DATETIME NULL COMMENT '授权时间' AFTER region,
    ADD COLUMN IF NOT EXISTS authorize_expire_time DATETIME NULL COMMENT '授权过期时间' AFTER authorize_time,
    ADD COLUMN IF NOT EXISTS ad_authorize_time DATETIME NULL COMMENT '广告授权时间' AFTER ad_refresh_token,
    ADD COLUMN IF NOT EXISTS ad_authorize_expire_time DATETIME NULL COMMENT '广告授权过期时间' AFTER ad_authorize_time;
