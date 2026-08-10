package cn.iocoder.yudao.module.amazon.dal.dataobject.seller;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Amazon 店铺 Marketplace 参与状态 DO。
 */
@TableName("amazon_shop_marketplace_participation")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonShopMarketplaceParticipationDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 关联的 Amazon 店铺编号。 */
    private Long shopId;
    /** Amazon Marketplace ID。 */
    private String marketplaceId;
    /** 站点国家或地区代码。 */
    private String countryCode;
    /** Amazon 站点名称。 */
    private String marketplaceName;
    /** 站点默认货币代码。 */
    private String defaultCurrencyCode;
    /** 站点默认语言代码。 */
    private String defaultLanguageCode;
    /** 站点域名。 */
    private String domainName;
    /** 卖家在该站点展示的店铺名称。 */
    private String storeName;
    /** 是否参与该站点销售。 */
    private Boolean isParticipating;
    /** 是否存在被停售的商品。 */
    private Boolean hasSuspendedListings;
    /** 最近一次同步参与状态的时间。 */
    private LocalDateTime lastSyncTime;
}
