CREATE TABLE IF NOT EXISTS temu_shipping_company (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    site VARCHAR(16) NOT NULL COMMENT 'Temu 站点代码，例如 US、DE、JP',
    region_id BIGINT NOT NULL COMMENT 'Temu 区域编号',
    logistics_service_provider_id BIGINT NOT NULL COMMENT 'Temu 物流服务商编号',
    logistics_service_provider_name VARCHAR(256) NOT NULL COMMENT '物流服务商名称',
    logistics_brand_name VARCHAR(256) NULL COMMENT '物流品牌名称',
    last_sync_time DATETIME NOT NULL COMMENT '最近一次从 Temu 同步的时间',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_temu_shipping_company_tenant_site_region_provider (tenant_id, site, region_id, logistics_service_provider_id),
    KEY idx_temu_shipping_company_tenant_region (tenant_id, site, region_id),
    KEY idx_temu_shipping_company_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Temu 区域承运商目录表';
