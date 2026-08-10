package cn.iocoder.yudao.module.amazon.dal.dataobject.seller;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Amazon 卖家账户及企业档案 DO。
 */
@TableName("amazon_seller_account")
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonSellerAccountDO extends TenantBaseDO {

    /** 主键编号。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 关联的 Amazon 店铺编号。 */
    private Long shopId;
    /** Amazon 企业类型。 */
    private String businessType;
    /** Amazon 销售计划。 */
    private String sellingPlan;
    /** 主要联系人拉丁名称。 */
    private String primaryContactName;
    /** 主要联系人非拉丁名称。 */
    private String primaryContactNonLatinName;
    /** 主要联系人地址第一行。 */
    private String primaryContactAddressLine1;
    /** 主要联系人地址第二行。 */
    private String primaryContactAddressLine2;
    /** 主要联系人地址国家代码。 */
    private String primaryContactCountryCode;
    /** 主要联系人地址省州代码。 */
    private String primaryContactStateOrProvinceCode;
    /** 主要联系人地址城市。 */
    private String primaryContactCity;
    /** 主要联系人地址邮编。 */
    private String primaryContactPostalCode;
    /** 企业拉丁名称。 */
    private String businessName;
    /** 企业非拉丁名称。 */
    private String businessNonLatinName;
    /** 企业注册地址第一行。 */
    private String registeredAddressLine1;
    /** 企业注册地址第二行。 */
    private String registeredAddressLine2;
    /** 企业注册地址国家代码。 */
    private String registeredCountryCode;
    /** 企业注册地址省州代码。 */
    private String registeredStateOrProvinceCode;
    /** 企业注册地址城市。 */
    private String registeredCity;
    /** 企业注册地址邮编。 */
    private String registeredPostalCode;
    /** 企业注册号。 */
    private String companyRegistrationNumber;
    /** 企业税务识别号。 */
    private String companyTaxIdentificationNumber;
    /** Sellers Account 接口完整响应快照。 */
    private String responseJson;
    /** 最近一次同步账户信息的时间。 */
    private LocalDateTime lastSyncTime;
}
