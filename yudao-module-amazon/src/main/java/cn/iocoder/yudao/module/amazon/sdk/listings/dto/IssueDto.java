package cn.iocoder.yudao.module.amazon.sdk.listings.dto;

import lombok.Data;

import java.util.List;

/** Listings Items API Issue 模型。 */
@Data
public class IssueDto {
    private String code;
    private String message;
    private String severity;
    private String attributeName;
    private List<String> attributeNames;
    private List<String> categories;
    private IssueEnforcementsDto enforcements;
}
