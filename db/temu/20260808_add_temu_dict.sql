-- Temu 基础字典：站点代码与店铺类型。
-- 使用条件插入避免重复执行时新增重复字典记录。

INSERT INTO system_dict_type (name, type, status, remark, creator, updater, deleted, deleted_time)
SELECT 'Temu 站点代码', 'temu_site_code', 0, 'Temu OpenAPI 支持的站点代码', 'admin', 'admin', b'0', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM system_dict_type WHERE type = 'temu_site_code' AND deleted = b'0'
);

INSERT INTO system_dict_type (name, type, status, remark, creator, updater, deleted, deleted_time)
SELECT 'Temu 店铺类型', 'temu_shop_type', 0, 'Temu 店铺经营模式', 'admin', 'admin', b'0', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM system_dict_type WHERE type = 'temu_shop_type' AND deleted = b'0'
);

INSERT INTO system_dict_data (sort, label, value, dict_type, status, color_type, css_class, remark, creator, updater, deleted)
SELECT source.sort, source.label, source.value, source.dict_type, 0, source.color_type, '', source.remark, 'admin', 'admin', b'0'
FROM (
    SELECT 1 AS sort, '美国' AS label, 'US' AS value, 'temu_site_code' AS dict_type, 'primary' AS color_type, 'Temu 美国站' AS remark
    UNION ALL SELECT 2, '墨西哥', 'MX', 'temu_site_code', 'success', 'Temu 墨西哥站'
    UNION ALL SELECT 3, '韩国', 'KR', 'temu_site_code', 'success', 'Temu 韩国站'
    UNION ALL SELECT 4, '日本', 'JP', 'temu_site_code', 'success', 'Temu 日本站'
    UNION ALL SELECT 5, '加拿大', 'CA', 'temu_site_code', 'success', 'Temu 加拿大站'
    UNION ALL SELECT 6, '澳大利亚', 'AU', 'temu_site_code', 'success', 'Temu 澳大利亚站'
    UNION ALL SELECT 7, '巴西', 'BR', 'temu_site_code', 'success', 'Temu 巴西站'
    UNION ALL SELECT 8, '德国', 'DE', 'temu_site_code', 'warning', 'Temu 德国站'
    UNION ALL SELECT 9, '法国', 'FR', 'temu_site_code', 'warning', 'Temu 法国站'
    UNION ALL SELECT 10, '意大利', 'IT', 'temu_site_code', 'warning', 'Temu 意大利站'
    UNION ALL SELECT 11, '西班牙', 'ES', 'temu_site_code', 'warning', 'Temu 西班牙站'
    UNION ALL SELECT 12, '比利时', 'BE', 'temu_site_code', 'warning', 'Temu 比利时站'
    UNION ALL SELECT 13, '奥地利', 'AT', 'temu_site_code', 'warning', 'Temu 奥地利站'
    UNION ALL SELECT 14, '罗马尼亚', 'RO', 'temu_site_code', 'warning', 'Temu 罗马尼亚站'
    UNION ALL SELECT 15, '荷兰', 'NL', 'temu_site_code', 'warning', 'Temu 荷兰站'
    UNION ALL SELECT 16, '波兰', 'PL', 'temu_site_code', 'warning', 'Temu 波兰站'
    UNION ALL SELECT 17, '葡萄牙', 'PT', 'temu_site_code', 'warning', 'Temu 葡萄牙站'
    UNION ALL SELECT 18, '匈牙利', 'HU', 'temu_site_code', 'warning', 'Temu 匈牙利站'
    UNION ALL SELECT 19, '捷克', 'CZ', 'temu_site_code', 'warning', 'Temu 捷克站'
    UNION ALL SELECT 20, '丹麦', 'DK', 'temu_site_code', 'warning', 'Temu 丹麦站'
    UNION ALL SELECT 21, '瑞典', 'SE', 'temu_site_code', 'warning', 'Temu 瑞典站'
    UNION ALL SELECT 22, '希腊', 'GR', 'temu_site_code', 'warning', 'Temu 希腊站'
    UNION ALL SELECT 23, '斯洛伐克', 'SK', 'temu_site_code', 'warning', 'Temu 斯洛伐克站'
    UNION ALL SELECT 24, '英国', 'GB', 'temu_site_code', 'warning', 'Temu 英国站'
    UNION ALL SELECT 25, '芬兰', 'FI', 'temu_site_code', 'warning', 'Temu 芬兰站'
    UNION ALL SELECT 26, '土耳其', 'TR', 'temu_site_code', 'warning', 'Temu 土耳其站'
    UNION ALL SELECT 27, '斯洛文尼亚', 'SI', 'temu_site_code', 'warning', 'Temu 斯洛文尼亚站'
    UNION ALL SELECT 28, '爱尔兰', 'IE', 'temu_site_code', 'warning', 'Temu 爱尔兰站'
    UNION ALL SELECT 29, '立陶宛', 'LT', 'temu_site_code', 'warning', 'Temu 立陶宛站'
    UNION ALL SELECT 30, '克罗地亚', 'HR', 'temu_site_code', 'warning', 'Temu 克罗地亚站'
    UNION ALL SELECT 31, '爱沙尼亚', 'EE', 'temu_site_code', 'warning', 'Temu 爱沙尼亚站'
    UNION ALL SELECT 32, '瑞士', 'CH', 'temu_site_code', 'warning', 'Temu 瑞士站'
    UNION ALL SELECT 33, '阿联酋', 'AE', 'temu_site_code', 'warning', 'Temu 阿联酋站'
    UNION ALL SELECT 34, '阿联酋（KAS）', 'KAS', 'temu_site_code', 'warning', 'Temu 阿联酋 KAS 站'
    UNION ALL SELECT 35, '拉脱维亚', 'LVA', 'temu_site_code', 'warning', 'Temu 拉脱维亚站'
    UNION ALL SELECT 36, '塞浦路斯', 'CY', 'temu_site_code', 'warning', 'Temu 塞浦路斯站'
    UNION ALL SELECT 37, '挪威', 'NO', 'temu_site_code', 'warning', 'Temu 挪威站'
    UNION ALL SELECT 38, '保加利亚', 'BG', 'temu_site_code', 'warning', 'Temu 保加利亚站'
    UNION ALL SELECT 39, '卢森堡', 'LU', 'temu_site_code', 'warning', 'Temu 卢森堡站'
    UNION ALL SELECT 40, '冰岛', 'IS', 'temu_site_code', 'warning', 'Temu 冰岛站'
) AS source
WHERE NOT EXISTS (
    SELECT 1 FROM system_dict_data target
    WHERE target.dict_type = source.dict_type
      AND target.value = source.value
      AND target.deleted = b'0'
);

INSERT INTO system_dict_data (sort, label, value, dict_type, status, color_type, css_class, remark, creator, updater, deleted)
SELECT source.sort, source.label, source.value, source.dict_type, 0, source.color_type, '', source.remark, 'admin', 'admin', b'0'
FROM (
    SELECT 1 AS sort, '全托管' AS label, '1' AS value, 'temu_shop_type' AS dict_type, 'primary' AS color_type, 'Temu 全托管店铺' AS remark
    UNION ALL SELECT 2, '半托管', '2', 'temu_shop_type', 'success', 'Temu 半托管店铺'
    UNION ALL SELECT 3, '本土店铺', '3', 'temu_shop_type', 'warning', 'Temu 本土店铺'
) AS source
WHERE NOT EXISTS (
    SELECT 1 FROM system_dict_data target
    WHERE target.dict_type = source.dict_type
      AND target.value = source.value
      AND target.deleted = b'0'
);
