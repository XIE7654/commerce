package cn.iocoder.yudao.module.temu.framework.client.auth;

import lombok.Data;

import java.util.List;

/**
 * 本地店铺标签查询结果。
 */
@Data
public class LocalMallTagsResult {
    private List<String> tags;
}
