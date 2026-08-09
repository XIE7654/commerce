package cn.iocoder.yudao.module.temu.dal.dataobject.ordershippinginfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Temu 父订单收货信息 DO
 *
 * @author 自达源码
 */
@TableName("temu_order_shipping_info")
@KeySequence("temu_order_shipping_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemuOrderShippingInfoDO extends BaseDO {

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
     * Temu 父订单号
     */
    private String parentOrderSn;
    /**
     * 收件人姓名
     */
    private String receiptName;
    /**
     * 附加收件人姓名
     */
    private String receiptAdditionalName;
    /**
     * 收件人名字
     */
    private String firstName;
    /**
     * 收件人姓氏
     */
    private String lastName;
    /**
     * 附加收件人名字
     */
    private String additionalFirstName;
    /**
     * 附加收件人姓氏
     */
    private String additionalLastName;
    /**
     * 收件邮箱
     */
    private String mail;
    /**
     * 收件手机号
     */
    private String mobile;
    /**
     * 备用手机号
     */
    private String backupMobile;
    /**
     * 一级行政区名称，例如国家
     */
    private String regionName1;
    /**
     * 二级行政区名称，例如州省
     */
    private String regionName2;
    /**
     * 三级行政区名称，例如城市
     */
    private String regionName3;
    /**
     * 四级行政区名称
     */
    private String regionName4;
    /**
     * 邮政编码
     */
    private String postCode;
    /**
     * 地址第一行
     */
    private String addressLine1;
    /**
     * 地址第二行
     */
    private String addressLine2;
    /**
     * 地址第三行
     */
    private String addressLine3;
    /**
     * 完整收货地址
     */
    private String addressLineAll;
    /**
     * Temu 地址警告信息
     */
    private String warning;
    /**
     * 最近从 Temu 同步时间
     */
    private LocalDateTime lastSyncTime;


}