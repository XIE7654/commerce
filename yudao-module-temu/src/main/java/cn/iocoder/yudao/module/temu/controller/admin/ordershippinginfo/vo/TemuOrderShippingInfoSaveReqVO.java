package cn.iocoder.yudao.module.temu.controller.admin.ordershippinginfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Temu 父订单收货信息新增/修改 Request VO")
@Data
public class TemuOrderShippingInfoSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19629")
    private Long id;

    @Schema(description = "关联 temu_shop.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17290")
    @NotNull(message = "关联 temu_shop.id不能为空")
    private Long shopId;

    @Schema(description = "Temu 父订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Temu 父订单号不能为空")
    private String parentOrderSn;

    @Schema(description = "收件人姓名", example = "芋艿")
    private String receiptName;

    @Schema(description = "附加收件人姓名", example = "芋艿")
    private String receiptAdditionalName;

    @Schema(description = "收件人名字", example = "赵六")
    private String firstName;

    @Schema(description = "收件人姓氏", example = "王五")
    private String lastName;

    @Schema(description = "附加收件人名字", example = "张三")
    private String additionalFirstName;

    @Schema(description = "附加收件人姓氏", example = "张三")
    private String additionalLastName;

    @Schema(description = "收件邮箱")
    private String mail;

    @Schema(description = "收件手机号")
    private String mobile;

    @Schema(description = "备用手机号")
    private String backupMobile;

    @Schema(description = "一级行政区名称，例如国家")
    private String regionName1;

    @Schema(description = "二级行政区名称，例如州省")
    private String regionName2;

    @Schema(description = "三级行政区名称，例如城市")
    private String regionName3;

    @Schema(description = "四级行政区名称")
    private String regionName4;

    @Schema(description = "邮政编码")
    private String postCode;

    @Schema(description = "地址第一行")
    private String addressLine1;

    @Schema(description = "地址第二行")
    private String addressLine2;

    @Schema(description = "地址第三行")
    private String addressLine3;

    @Schema(description = "完整收货地址")
    private String addressLineAll;

    @Schema(description = "Temu 地址警告信息")
    private String warning;

    @Schema(description = "最近从 Temu 同步时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最近从 Temu 同步时间不能为空")
    private LocalDateTime lastSyncTime;

}