package cn.iocoder.yudao.module.amazon.dal.dataobject.shop;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Amazon 店铺授权信息，按租户隔离。
 */
@TableName("amazon_shop")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonShopDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId
    private Long id;
    /** Amazon sellerId。 */
    private String sellerId;
    /** 默认 marketplaceId。 */
    private String marketplaceId;
    /** 店铺名称。 */
    private String shopName;
    /** Amazon 区域。 */
    private String region;
    /** Seller refresh token。 */
    private String sellerRefreshToken;
    /** Seller access token。 */
    private String sellerAccessToken;
    /** Seller access token 过期时间。 */
    private LocalDateTime sellerAccessTokenExpiresAt;
    /** Ads refresh token。 */
    private String adRefreshToken;
    /** Ads access token。 */
    private String adAccessToken;
    /** Ads access token 过期时间。 */
    private LocalDateTime adAccessTokenExpiresAt;
    /** 店铺状态：0-启用，1-禁用。 */
    private Integer status;
}
