package cn.iocoder.yudao.module.amazon.sdk.listings.dto;

import lombok.Data;

import java.util.List;

/** Listings Item 写接口提交结果模型。 */
@Data
public class ListingsItemSubmissionDto {
    private String sku;
    private String status;
    private String submissionId;
    private List<IssueDto> issues;
}
