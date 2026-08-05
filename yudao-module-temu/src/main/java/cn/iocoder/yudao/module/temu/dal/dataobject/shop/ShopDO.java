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
public class ShopDO extends BaseDO {

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


}