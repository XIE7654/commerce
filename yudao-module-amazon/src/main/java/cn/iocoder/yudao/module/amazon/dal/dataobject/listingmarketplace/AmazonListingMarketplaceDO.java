package cn.iocoder.yudao.module.amazon.dal.dataobject.listingmarketplace;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Listing信息表 DO
 *
 * @author 自达源码
 */
@TableName("amazon_listing_marketplace")
@KeySequence("amazon_listing_marketplace_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonListingMarketplaceDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 关联 amazon_listing.id
     */
    private Long listingId;
    /**
     * Amazon Marketplace ID
     */
    private String marketplaceId;
    /**
     * Amazon 标准识别号
     */
    private String asin;
    /**
     * Amazon 商品类型
     */
    private String productType;
    /**
     * 商品状况类型
     */
    private String conditionType;
    /**
     * Amazon 商品名称
     */
    private String itemName;
    /**
     * Amazon Listing 创建时间
     */
    private LocalDateTime amazonCreatedTime;
    /**
     * Amazon Listing 更新时间
     */
    private LocalDateTime amazonUpdatedTime;
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;


}