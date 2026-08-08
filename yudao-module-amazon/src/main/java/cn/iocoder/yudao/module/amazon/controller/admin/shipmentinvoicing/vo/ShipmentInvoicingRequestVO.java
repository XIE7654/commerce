package cn.iocoder.yudao.module.amazon.controller.admin.shipmentinvoicing.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Amazon Shipment Invoicing API 请求参数。 */
@Data
public class ShipmentInvoicingRequestVO {

    @NotNull(message = "店铺编号不能为空")
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @NotBlank(message = "国家代码不能为空")
    @Schema(description = "请求服务端点所属国家代码；Shipment Invoicing 仅支持巴西", requiredMode = Schema.RequiredMode.REQUIRED, example = "BR")
    private String countryCode;

    @NotBlank(message = "货件编号不能为空")
    @Schema(description = "FBA 出库货件编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "F4385943758")
    private String shipmentId;

    @Schema(description = "提交发票的原始请求体，必须包含 ContentMD5Value 和 InvoiceContent")
    private Map<String, Object> invoiceBody;
}
