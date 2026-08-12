package cn.iocoder.yudao.module.amazon.dal.dataobject.shop;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Amazon店铺授权 DO
 *
 * @author 自达源码
 */
@TableName("amazon_shop")
@KeySequence("amazon_shop_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonShopDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * Amazon sellerId
     */
    private String sellerId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * Amazon 区域：NA、EU、FE
     */
    private String region;
    /**
     * 授权时间
     */
    private LocalDateTime authorizeTime;
    /**
     * 授权过期时间
     */
    private LocalDateTime authorizeExpireTime;
    /**
     * Seller refresh token
     */
    private String sellerRefreshToken;
    /**
     * Seller access token，短期缓存
     */
    private String sellerAccessToken;
    /**
     * Seller access token 过期时间
     */
    private LocalDateTime sellerAccessTokenExpiresAt;
    /**
     * 广告 refresh token
     */
    private String adRefreshToken;
    /**
     * 广告 access token，短期缓存
     */
    private String adAccessToken;
    /**
     * 广告 access token 过期时间
     */
    private LocalDateTime adAccessTokenExpiresAt;
    /**
     * 广告授权时间
     */
    private LocalDateTime adAuthorizeTime;
    /**
     * 广告授权过期时间
     */
    private LocalDateTime adAuthorizeExpireTime;
    /**
     * 状态：0-启用，1-禁用
     */
    private Integer status;


}
