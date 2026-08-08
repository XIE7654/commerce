package cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/** Amazon Feed 列表查询参数。 */
@Data
public class AmazonFeedsListReqVO {
    @NotNull @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;
    @NotBlank @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    private String countryCode;
    @Size(max = 10) private List<String> feedTypes;
    @Size(max = 10) private List<String> marketplaceIds;
    @Min(1) @Max(100) private Integer pageSize;
    private List<String> processingStatuses;
    private String createdSince;
    private String createdUntil;
    private String nextToken;
}
