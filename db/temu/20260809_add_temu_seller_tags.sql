-- 为已存在的 Temu 卖家表补充店铺标签字段。
ALTER TABLE temu_seller
    ADD COLUMN tags JSON NULL COMMENT 'Temu 店铺标签枚举列表' AFTER mall_type;

-- Temu 店铺标签字典：标签值由 Temu OpenAPI 返回，供商品刊登和履约场景使用。
INSERT INTO system_dict_type (name, type, status, remark, creator, updater, deleted, deleted_time)
SELECT 'Temu 店铺标签', 'temu_mall_tag', 0, 'Temu 本地店铺标签枚举', 'admin', 'admin', b'0', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM system_dict_type WHERE type = 'temu_mall_tag' AND deleted = b'0'
);

INSERT INTO system_dict_data (sort, label, value, dict_type, status, color_type, css_class, remark, creator, updater, deleted)
SELECT source.sort, source.label, source.value, source.dict_type, 0, source.color_type, '', source.remark,
       'admin', 'admin', b'0'
FROM (
    SELECT 1 AS sort, '无特殊标签（INIT）' AS label, '0' AS value, 'temu_mall_tag' AS dict_type,
           'primary' AS color_type, '仅可销售全新商品' AS remark
    UNION ALL SELECT 2, '翻新标签（REFURBISHMENT）', '1', 'temu_mall_tag', 'success', '仅可销售品牌或一级授权经销商官方翻新商品'
    UNION ALL SELECT 3, '按单生产标签（MADE_TO_ORDER）', '2', 'temu_mall_tag', 'success', '可销售下单后生产或组装的商品'
    UNION ALL SELECT 4, '二手标签（SECONDHAND）', '3', 'temu_mall_tag', 'warning', '仅可销售未官方翻新的二手商品'
    UNION ALL SELECT 5, '美国至加拿大履约标签（CA_BBC）', '4', 'temu_mall_tag', 'warning', '可从美国发货至加拿大并使用确认发货流程'
    UNION ALL SELECT 6, '书店标签（BOOKSTORE）', '5', 'temu_mall_tag', 'warning', '部分地区销售出版物需使用该标签及专用运费模板'
) AS source
WHERE NOT EXISTS (
    SELECT 1 FROM system_dict_data target
    WHERE target.dict_type = source.dict_type
      AND target.value = source.value
      AND target.deleted = b'0'
);
