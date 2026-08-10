package cn.iocoder.yudao.module.amazon.dal.dataobject.listingimage;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Amazon Listing 在站点维度的图片。
 */
@TableName("amazon_listing_image")
@KeySequence("amazon_listing_image_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonListingImageDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联 amazon_listing_marketplace.id。 */
    private Long listingMarketplaceId;
    /** 图片类型。 */
    private String imageType;
    /** 图片地址。 */
    private String imageUrl;
    /** 图片宽度。 */
    private Integer width;
    /** 图片高度。 */
    private Integer height;
    /** 同一类型图片的排序号。 */
    private Integer sortOrder;
}
