package cn.iocoder.yudao.module.amazon.dal.dataobject.listing;

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
 * Amazon Listing 主表 DO。
 */
@TableName("amazon_listing")
@KeySequence("amazon_listing_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonListingDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联的 Amazon 店铺编号。 */
    private Long shopId;
    /** Amazon Seller SKU。 */
    private String sku;
    /** ERP 主 SKU。 */
    private String mSku;
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
    /** JSON 格式的 Amazon Listing 状态列表。 */
    private String statuses;
    /** JSON 格式的 Amazon Listing 图片列表。 */
    private String images;
    /** JSON 格式的 Amazon Listing 动态属性。 */
    private String attributes;
    /** JSON 格式的 Amazon Listing 校验问题列表。 */
    private String issues;
    /** 首次同步时间。 */
    private LocalDateTime firstSyncTime;
    /** 最后同步时间。 */
    private LocalDateTime lastSyncTime;
}
