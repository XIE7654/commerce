package cn.iocoder.yudao.module.amazon.controller.admin.services.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** Services API 请求参数，未展开的官方字段通过 {@link #body} 或 {@link #query} 透传。 */
@Data
public class ServicesReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1") @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US") @NotBlank(message = "国家代码不能为空") private String countryCode;
    @Schema(description = "服务工单编号") private String serviceJobId;
    @Schema(description = "预约编号") private String appointmentId;
    @Schema(description = "服务资源编号") private String resourceId;
    @Schema(description = "预约预留编号") private String reservationId;
    @Schema(description = "ASIN；查询通用预约时必填") private String asin;
    @Schema(description = "门店编号；查询通用预约时必填") private String storeId;
    @Schema(description = "Marketplace ID 列表；Services 查询接口未传时使用国家默认站点") private List<String> marketplaceIds;
    @Schema(description = "官方模型中的查询参数，例如 pageToken、startTime 或 cancellationReasonCode") private Map<String, String> query;
    @Schema(description = "符合 Amazon Services 官方模型的请求体") private Map<String, Object> body;
}
