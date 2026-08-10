CREATE TABLE IF NOT EXISTS amazon_listing (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    shop_id BIGINT NOT NULL COMMENT '关联 amazon_shop.id',
    sku VARCHAR(100) NOT NULL COMMENT 'Amazon Seller SKU',
    m_sku VARCHAR(100) NULL COMMENT 'ERP 主 SKU',
    first_sync_time DATETIME NULL COMMENT '首次同步时间',
    last_sync_time DATETIME NULL COMMENT '最后同步时间',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_amazon_listing_tenant_shop_sku (tenant_id, shop_id, sku),
    KEY idx_amazon_listing_tenant_shop (tenant_id, shop_id),
    KEY idx_amazon_listing_tenant_m_sku (tenant_id, m_sku),
    CONSTRAINT fk_amazon_listing_shop FOREIGN KEY (shop_id) REFERENCES amazon_shop (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon Listing 主表';

CREATE TABLE IF NOT EXISTS amazon_listing_marketplace (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    listing_id BIGINT NOT NULL COMMENT '关联 amazon_listing.id',
    marketplace_id VARCHAR(64) NOT NULL COMMENT 'Amazon Marketplace ID',
    asin VARCHAR(20) NULL COMMENT 'Amazon 标准识别号',
    product_type VARCHAR(100) NULL COMMENT 'Amazon 商品类型',
    condition_type VARCHAR(50) NULL COMMENT '商品状况类型',
    item_name VARCHAR(1000) NULL COMMENT 'Amazon 商品名称',
    amazon_created_time DATETIME(3) NULL COMMENT 'Amazon Listing 创建时间',
    amazon_updated_time DATETIME(3) NULL COMMENT 'Amazon Listing 更新时间',
    last_sync_time DATETIME NULL COMMENT '最后同步时间',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_amazon_listing_marketplace (listing_id, marketplace_id),
    KEY idx_amazon_listing_marketplace_tenant_asin (tenant_id, marketplace_id, asin),
    KEY idx_amazon_listing_marketplace_tenant_listing (tenant_id, listing_id),
    KEY idx_amazon_listing_marketplace_tenant_asin_only (tenant_id, asin),
    CONSTRAINT fk_amazon_listing_marketplace_listing FOREIGN KEY (listing_id) REFERENCES amazon_listing (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon Listing 站点信息表';

CREATE TABLE IF NOT EXISTS amazon_listing_status (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    listing_marketplace_id BIGINT NOT NULL COMMENT '关联 amazon_listing_marketplace.id',
    status VARCHAR(50) NOT NULL COMMENT 'Amazon Listing 状态',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_amazon_listing_status (listing_marketplace_id, status),
    KEY idx_amazon_listing_status_tenant_status (tenant_id, status),
    KEY idx_amazon_listing_status_tenant_marketplace (tenant_id, listing_marketplace_id),
    CONSTRAINT fk_amazon_listing_status_marketplace FOREIGN KEY (listing_marketplace_id) REFERENCES amazon_listing_marketplace (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon Listing 状态表';

CREATE TABLE IF NOT EXISTS amazon_listing_image (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    listing_marketplace_id BIGINT NOT NULL COMMENT '关联 amazon_listing_marketplace.id',
    image_type VARCHAR(32) NOT NULL DEFAULT 'MAIN' COMMENT '图片类型，例如 MAIN、PT01',
    image_url VARCHAR(1000) NOT NULL COMMENT '图片地址',
    width INT NULL COMMENT '图片宽度',
    height INT NULL COMMENT '图片高度',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '图片排序号',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_amazon_listing_image (listing_marketplace_id, image_type, sort_order),
    KEY idx_amazon_listing_image_tenant_marketplace (tenant_id, listing_marketplace_id),
    CONSTRAINT fk_amazon_listing_image_marketplace FOREIGN KEY (listing_marketplace_id) REFERENCES amazon_listing_marketplace (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon Listing 图片表';

CREATE TABLE IF NOT EXISTS amazon_listing_attribute (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    listing_marketplace_id BIGINT NOT NULL COMMENT '关联 amazon_listing_marketplace.id',
    attribute_name VARCHAR(100) NOT NULL COMMENT 'Amazon 属性名称',
    attribute_value JSON NOT NULL COMMENT 'Amazon 属性原始值',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_amazon_listing_attribute (listing_marketplace_id, attribute_name),
    KEY idx_amazon_listing_attribute_tenant_marketplace (tenant_id, listing_marketplace_id),
    CONSTRAINT fk_amazon_listing_attribute_marketplace FOREIGN KEY (listing_marketplace_id) REFERENCES amazon_listing_marketplace (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon Listing 动态属性表';

CREATE TABLE IF NOT EXISTS amazon_listing_issue (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键编号',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    listing_marketplace_id BIGINT NOT NULL COMMENT '关联 amazon_listing_marketplace.id',
    issue_code VARCHAR(100) NULL COMMENT 'Amazon 问题代码',
    severity VARCHAR(32) NULL COMMENT '问题严重程度',
    message VARCHAR(2000) NULL COMMENT '问题说明',
    attribute_names JSON NULL COMMENT '关联的属性名称列表',
    issue_value JSON NULL COMMENT 'Amazon 问题原始值',
    creator VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (id),
    KEY idx_amazon_listing_issue_tenant_marketplace (tenant_id, listing_marketplace_id),
    KEY idx_amazon_listing_issue_tenant_code (tenant_id, issue_code),
    CONSTRAINT fk_amazon_listing_issue_marketplace FOREIGN KEY (listing_marketplace_id) REFERENCES amazon_listing_marketplace (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Amazon Listing 问题表';
