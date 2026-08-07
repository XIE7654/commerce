CREATE TABLE IF NOT EXISTS temu_shop (
    id BIGINT NOT NULL COMMENT '主键编号',
    shop_type TINYINT NOT NULL COMMENT '店铺类型：1-全托管，2-半托管，3-本土店铺',
    site VARCHAR(16) NOT NULL COMMENT 'Temu 站点代码，例如 US、DE、JP',
    shop_name VARCHAR(128) NOT NULL COMMENT '店铺名称',
    auth_token VARCHAR(512) NOT NULL COMMENT 'Temu 授权 Token',
    authorize_time DATETIME NULL COMMENT '授权时间',
    authorize_expire_time DATETIME NULL COMMENT '授权过期时间',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id),
    KEY idx_temu_shop_tenant_id (tenant_id),
    KEY idx_temu_shop_site (site)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Temu 店铺表';
