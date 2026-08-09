package cn.iocoder.yudao.module.temu.dal.dataobject.shippingcompany;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Temu 区域承运商目录 DO
 *
 * @author 自达源码
 */
@TableName("temu_shipping_company")
@KeySequence("temu_shipping_company_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuShippingCompanyDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * Temu 站点代码，例如 US、DE、JP
     */
    private String site;
    /**
     * Temu 区域编号
     */
    private Long regionId;
    /**
     * Temu 物流服务商编号
     */
    private Long logisticsServiceProviderId;
    /**
     * 物流服务商名称
     */
    private String logisticsServiceProviderName;
    /**
     * 物流品牌名称
     */
    private String logisticsBrandName;
    /**
     * 最近一次从 Temu 同步的时间
     */
    private LocalDateTime lastSyncTime;


}