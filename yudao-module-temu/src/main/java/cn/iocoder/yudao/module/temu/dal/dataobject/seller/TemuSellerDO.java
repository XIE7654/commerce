package cn.iocoder.yudao.module.temu.dal.dataobject.seller;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Temu 卖家商城授权信息 DO
 *
 * @author 自达源码
 */
@TableName("temu_seller")
@KeySequence("temu_seller_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuSellerDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 关联 temu_shop.id
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopName;
    /**
     * Temu semiUniqueId
     */
    private String semiUniqueId;
    /**
     * Temu 区域编号
     */
    private Integer regionId;
    /**
     * Temu mallId
     */
    private Long mallId;
    /**
     * Temu mallType
     */
    private Integer mallType;
    /**
     * 店铺标签列表，保存 Temu 返回的标签枚举 JSON 数组。
     */
    private String tags;
    /**
     * 应用订阅状态
     */
    private Integer appSubscribeStatus;
    /**
     * 授权过期时间，Unix 时间戳（秒）
     */
    private Long expiredTime;
    /**
     * 授权过期时间，便于数据库查询
     */
    private LocalDateTime expiredAt;
    /**
     * 应用订阅事件编码列表
     */
    private String appSubscribeEventCodeList;
    /**
     * 授权事件及权限状态列表
     */
    private String authEventCodeList;
    /**
     * API 权限范围列表
     */
    private String apiScopeList;
    /**
     * 接口完整响应快照，便于兼容后续字段
     */
    private String responseJson;
    /**
     * 最近一次同步授权信息时间
     */
    private LocalDateTime lastSyncTime;


}
