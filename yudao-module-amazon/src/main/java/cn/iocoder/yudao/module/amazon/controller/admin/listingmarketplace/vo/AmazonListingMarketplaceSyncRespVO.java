package cn.iocoder.yudao.module.amazon.controller.admin.listingmarketplace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Amazon Listing Marketplace 同步汇总结果。
 */
@Data
@Schema(description = "管理后台 - Amazon Listing Marketplace 同步汇总结果")
public class AmazonListingMarketplaceSyncRespVO {

    /** 启用店铺数量。 */
    @Schema(description = "启用店铺数量", example = "2")
    private int shopCount;
    /** 成功调用的 Marketplace 数量。 */
    @Schema(description = "成功同步的站点数量", example = "3")
    private int marketplaceCount;
    /** 已写入本地的 Listing 站点记录数量。 */
    @Schema(description = "同步的 Listing 站点记录数量", example = "120")
    private int listingMarketplaceCount;
    /** 单个店铺站点同步失败明细。 */
    @Schema(description = "同步失败明细")
    private List<String> failures = new ArrayList<>();
}
