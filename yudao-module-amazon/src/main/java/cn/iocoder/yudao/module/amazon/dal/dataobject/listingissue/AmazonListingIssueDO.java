package cn.iocoder.yudao.module.amazon.dal.dataobject.listingissue;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Amazon Listing 在站点维度的校验问题。
 */
@TableName("amazon_listing_issue")
@KeySequence("amazon_listing_issue_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonListingIssueDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联 amazon_listing_marketplace.id。 */
    private Long listingMarketplaceId;
    /** Amazon 问题代码。 */
    private String issueCode;
    /** 问题严重程度。 */
    private String severity;
    /** 问题说明。 */
    private String message;
    /** JSON 格式的关联属性名称列表。 */
    private String attributeNames;
    /** JSON 格式的问题原始值。 */
    private String issueValue;
}
