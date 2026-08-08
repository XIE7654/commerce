package cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 创建 Amazon Feed Document 的请求参数。 */
@Data
public class AmazonFeedDocumentCreateReqVO {
    @NotNull private Long shopId;
    @NotBlank private String countryCode;
    @NotBlank @Schema(description = "Feed 文档内容类型", example = "text/tab-separated-values; charset=UTF-8")
    private String contentType;
}
