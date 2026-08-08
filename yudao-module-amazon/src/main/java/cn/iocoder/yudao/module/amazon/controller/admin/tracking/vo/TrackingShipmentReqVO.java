package cn.iocoder.yudao.module.amazon.controller.admin.tracking.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Amazon Tracking 物流轨迹查询参数。 */
@Data
public class TrackingShipmentReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "查询站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "Amazon 生成的物流查询标识", example = "546e1ddb-dae0-4f76-84fd-ab4998ad00dd")
    private String id;

    @Schema(description = "航空货运单号 ACSIN", example = "75726371365")
    private String acsin;

    @Schema(description = "Amazon 履约跟踪编号 AFTN", example = "AFTN987654321")
    private String aftn;

    @Schema(description = "物流服务商提供的集装箱号", example = "MSKU4538324")
    private String containerNumber;

    @Schema(description = "分提单号 HBL", example = "AMZDCN203A900BD3")
    private String houseBillOfLadingNumber;

    @Schema(description = "承运商跟踪单号", example = "1Z999AA1234567890")
    private String carrierTrackingNumber;

    @Schema(description = "承运商代码，使用承运商跟踪单号时建议同时传入", example = "UPS")
    private String carrierCode;

    @Schema(description = "响应语言，目前 Amazon 仅支持 en-US", example = "en-US")
    private String acceptLanguage;
}
