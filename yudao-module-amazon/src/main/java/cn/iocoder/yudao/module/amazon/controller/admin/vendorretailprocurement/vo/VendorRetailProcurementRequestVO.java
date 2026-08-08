package cn.iocoder.yudao.module.amazon.controller.admin.vendorretailprocurement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Vendor Retail Procurement API 通用请求参数。 */
@Data
public class VendorRetailProcurementRequestVO {

    @NotNull(message = "店铺编号不能为空")
    @Schema(description = "已完成 Vendor 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @NotBlank(message = "国家代码不能为空")
    @Schema(description = "请求服务端点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    private String countryCode;

    @Schema(description = "采购订单编号；查询单个采购订单时必填", example = "058-1234567-1234567")
    private String purchaseOrderNumber;

    @Schema(description = "交易编号；查询交易状态时必填", example = "3b6d6a83-9a61-4f15-a4d4-4d3e3e3a4de5")
    private String transactionId;

    @Schema(description = "与当前操作匹配的查询参数。Orders、Shipments 的参数名以 Amazon Vendor Retail Procurement OpenAPI 模型为准")
    private Map<String, String> query;

    @Schema(description = "提交确认、发票或货件的 Amazon 原始 JSON 请求体")
    private Map<String, Object> body;
}
