package cn.iocoder.yudao.module.amazon.dal.dataobject.listingattribute;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Amazon Listing 在站点维度的动态属性原始值。
 */
@TableName("amazon_listing_attribute")
@KeySequence("amazon_listing_attribute_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonListingAttributeDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联 amazon_listing_marketplace.id。 */
    private Long listingMarketplaceId;
    /** Amazon 属性名称。 */
    private String attributeName;
    /** JSON 格式的 Amazon 属性原始值。 */
    private String attributeValue;
}
