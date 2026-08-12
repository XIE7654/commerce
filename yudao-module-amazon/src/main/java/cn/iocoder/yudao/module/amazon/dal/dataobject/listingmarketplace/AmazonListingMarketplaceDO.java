package cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Amazon Listing 站点信息 DO。
 */
@TableName("amazon_listing_marketplace")
@KeySequence("amazon_listing_marketplace_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonListingMarketplaceDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联的 Amazon Listing 编号。 */
    private Long listingId;
    /** Amazon Marketplace ID。 */
    private String marketplaceId;
    /** Amazon 标准识别号。 */
    private String asin;
    /** Amazon 商品类型。 */
    private String productType;
    /** 商品状况类型。 */
    private String conditionType;
    /** 商品名称。 */
    private String itemName;
    /** Amazon Listing 创建时间。 */
    private LocalDateTime amazonCreatedTime;
    /** Amazon Listing 更新时间。 */
    private LocalDateTime amazonUpdatedTime;
    /** 最后同步时间。 */
    private LocalDateTime lastSyncTime;
}
