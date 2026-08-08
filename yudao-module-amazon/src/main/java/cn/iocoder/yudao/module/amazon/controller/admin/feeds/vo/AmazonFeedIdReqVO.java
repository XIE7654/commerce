package cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Amazon Feed 或 Feed Document 标识参数。 */
@Data
public class AmazonFeedIdReqVO {
    @NotNull @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;
    @NotBlank @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    private String countryCode;
    @NotBlank @Schema(description = "Feed 标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "3485934")
    private String id;
}
