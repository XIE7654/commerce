package cn.iocoder.yudao.module.temu.dal.dataobject.shop;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Temu 店铺 DO。
 *
 * <p>店铺授权信息按租户隔离，具体数据表由分片路由选择 {@code temu_shop_0} 至
 * {@code temu_shop_4} 中的一张表。</p>
 */
@TableName("temu_shop")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuShopDO extends TenantBaseDO {

    /**
     * 主键编号。
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺类型：1-全托管店铺，2-半托管店铺，3-本土店铺。
     */
    private Integer shopType;

    /**
     * Temu 站点代码，例如 US、DE、JP。
     */
    private String site;

    /**
     * 店铺名称。
     */
    private String shopName;

    /**
     * Temu 授权 Token。
     */
    private String authToken;

    /**
     * Temu 授权生效时间。
     */
    private LocalDateTime authorizeTime;

    /**
     * Temu 授权过期时间，用于提示店铺重新授权。
     */
    private LocalDateTime authorizeExpireTime;

}
