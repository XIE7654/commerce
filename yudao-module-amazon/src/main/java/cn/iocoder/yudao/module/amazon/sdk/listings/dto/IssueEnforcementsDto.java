package cn.iocoder.yudao.module.amazon.sdk.listings.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/** Amazon 对 Listings Issue 采取的 enforcement 动作及豁免信息。 */
@Data
public class IssueEnforcementsDto {
    private List<Map<String, Object>> actions;
    private Map<String, Object> exemption;
}
