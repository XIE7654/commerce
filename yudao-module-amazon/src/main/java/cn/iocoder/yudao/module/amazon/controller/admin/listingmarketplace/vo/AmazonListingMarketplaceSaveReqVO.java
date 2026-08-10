package cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Listing信息表新增/修改 Request VO")
@Data
public class AmazonListingMarketplaceSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "24412")
    private Long id;

    @Schema(description = "关联 amazon_listing.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28700")
    @NotNull(message = "关联 amazon_listing.id不能为空")
    private Long listingId;

    @Schema(description = "Amazon Marketplace ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10850")
    @NotEmpty(message = "Amazon Marketplace ID不能为空")
    private String marketplaceId;

    @Schema(description = "Amazon 标准识别号")
    private String asin;

    @Schema(description = "Amazon 商品类型", example = "2")
    private String productType;

    @Schema(description = "商品状况类型", example = "2")
    private String conditionType;

    @Schema(description = "Amazon 商品名称", example = "赵六")
    private String itemName;

    @Schema(description = "Amazon Listing 创建时间")
    private LocalDateTime amazonCreatedTime;

    @Schema(description = "Amazon Listing 更新时间")
    private LocalDateTime amazonUpdatedTime;

    @Schema(description = "最后同步时间")
    private LocalDateTime lastSyncTime;

}