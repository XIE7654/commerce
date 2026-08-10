package cn.iocoder.yudao.module.amazon.dal.dataobject.listingstatus;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Amazon Listing 在站点维度的状态。
 */
@TableName("amazon_listing_status")
@KeySequence("amazon_listing_status_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonListingStatusDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** 关联 amazon_listing_marketplace.id。 */
    private Long listingMarketplaceId;
    /** Amazon Listing 状态。 */
    private String status;
}
