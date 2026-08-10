package cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Listing信息表分页 Request VO")
@Data
public class AmazonListingMarketplacePageReqVO extends PageParam {

    @Schema(description = "关联 amazon_listing.id", example = "28700")
    private Long listingId;

    @Schema(description = "Amazon Marketplace ID", example = "10850")
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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] amazonCreatedTime;

    @Schema(description = "Amazon Listing 更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] amazonUpdatedTime;

    @Schema(description = "最后同步时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastSyncTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}