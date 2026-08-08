package cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;
import java.util.Map;

/** 创建 Amazon Feed 的请求参数。 */
@Data
public class AmazonFeedCreateReqVO {
    @NotNull private Long shopId;
    @NotBlank private String countryCode;
    @NotBlank @Schema(description = "Feed 类型", example = "POST_PRODUCT_DATA") private String feedType;
    @NotEmpty @Size(max = 25) @Schema(description = "目标 Marketplace ID 列表") private List<String> marketplaceIds;
    @NotBlank @Schema(description = "已上传 Feed 文档编号") private String inputFeedDocumentId;
    private Map<String, String> feedOptions;
}
