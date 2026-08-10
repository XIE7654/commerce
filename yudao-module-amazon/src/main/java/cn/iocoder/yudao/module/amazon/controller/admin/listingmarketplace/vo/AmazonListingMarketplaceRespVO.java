package cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Listing信息表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AmazonListingMarketplaceRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "24412")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "关联 amazon_listing.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28700")
    @ExcelProperty("关联 amazon_listing.id")
    private Long listingId;

    @Schema(description = "Amazon Marketplace ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10850")
    @ExcelProperty("Amazon Marketplace ID")
    private String marketplaceId;

    @Schema(description = "Amazon 标准识别号")
    @ExcelProperty("Amazon 标准识别号")
    private String asin;

    @Schema(description = "Amazon 商品类型", example = "2")
    @ExcelProperty("Amazon 商品类型")
    private String productType;

    @Schema(description = "商品状况类型", example = "2")
    @ExcelProperty("商品状况类型")
    private String conditionType;

    @Schema(description = "Amazon 商品名称", example = "赵六")
    @ExcelProperty("Amazon 商品名称")
    private String itemName;

    @Schema(description = "Amazon Listing 创建时间")
    @ExcelProperty("Amazon Listing 创建时间")
    private LocalDateTime amazonCreatedTime;

    @Schema(description = "Amazon Listing 更新时间")
    @ExcelProperty("Amazon Listing 更新时间")
    private LocalDateTime amazonUpdatedTime;

    @Schema(description = "最后同步时间")
    @ExcelProperty("最后同步时间")
    private LocalDateTime lastSyncTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}