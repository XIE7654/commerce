-- 为已部署的 Temu 店铺表补充状态字段，并与系统 common_status 字典保持一致。
ALTER TABLE temu_shop
    ADD COLUMN  status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-开启，1-关闭，对应系统 common_status 字典' AFTER auth_token;
