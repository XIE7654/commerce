package cn.iocoder.yudao.module.temu.dal.dataobject.shop;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Temu 店铺 DO
 *
 * @author 芋道源码
 */
@TableName("temu_shop")
@KeySequence("temu_shop_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuShopDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 店铺类型：1-全托管，2-半托管，3-本土店铺
     */
    private Integer shopType;
    /**
     * Temu 站点代码，例如 US、DE、JP
     */
    private String site;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * Temu 授权 Token
     */
    private String authToken;
    /**
     * 状态：0-开启，1-关闭，对应系统 common_status 字典。
     */
    private Integer status;

    /** Temu semiUniqueId。 */
    private String semiUniqueId;
    /** Temu 区域编号。 */
    private Integer regionId;
    /** Temu mallId。 */
    private Long mallId;
    /** Temu mallType。 */
    private Integer mallType;
    /** Temu 店铺标签 JSON。 */
    private String tags;
    /** 应用订阅状态。 */
    private Integer appSubscribeStatus;
    /** 授权过期时间 Unix 秒。 */
    private Long expiredTime;
    /** 授权过期时间。 */
    private LocalDateTime expiredAt;
    /** 应用订阅事件编码列表 JSON。 */
    private String appSubscribeEventCodeList;
    /** 授权事件及权限状态列表 JSON。 */
    private String authEventCodeList;
    /** API 权限范围列表 JSON。 */
    private String apiScopeList;
    /** 授权接口完整响应 JSON。 */
    private String responseJson;
    /** 最近一次同步授权信息时间。 */
    private LocalDateTime lastSyncTime;


}
