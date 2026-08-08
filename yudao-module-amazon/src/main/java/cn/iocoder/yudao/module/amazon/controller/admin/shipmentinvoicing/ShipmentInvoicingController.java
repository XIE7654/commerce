package cn.iocoder.yudao.module.amazon.controller.admin.shipmentinvoicing;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.shipmentinvoicing.vo.ShipmentInvoicingRequestVO;
import cn.iocoder.yudao.module.amazon.service.shipmentinvoicing.ShipmentInvoicingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Amazon Shipment Invoicing 管理接口。 */
@Tag(name = "管理后台 - Amazon Shipment Invoicing")
@RestController
@RequestMapping("/amazon/shipment-invoicing")
@Validated
public class ShipmentInvoicingController {

    @Resource
    private ShipmentInvoicingService shipmentInvoicingService;

    /**
     * 查询开具发票所需的 FBA 货件信息。
     *
     * @param request 店铺、巴西站点和 FBA 货件编号
     * @return Amazon 返回的货件发票信息
     */
    @PostMapping("/shipments/detail")
    @Operation(summary = "查询 Amazon 货件发票信息")
    @PreAuthorize("@ss.hasPermission('amazon:shipment-invoicing:query')")
    public CommonResult<Map<String, Object>> getShipmentDetails(@Valid @RequestBody ShipmentInvoicingRequestVO request) {
        return CommonResult.success(shipmentInvoicingService.getShipmentDetails(request));
    }

    /**
     * 向 Amazon 提交指定 FBA 货件的发票内容。
     *
     * @param request 店铺、巴西站点、货件编号和发票内容
     * @return Amazon 返回的发票提交结果
     */
    @PostMapping("/invoices/submit")
    @Operation(summary = "提交 Amazon 货件发票")
    @PreAuthorize("@ss.hasPermission('amazon:shipment-invoicing:update')")
    public CommonResult<Map<String, Object>> submitInvoice(@Valid @RequestBody ShipmentInvoicingRequestVO request) {
        return CommonResult.success(shipmentInvoicingService.submitInvoice(request));
    }

    /**
     * 查询指定 FBA 货件发票的处理状态。
     *
     * @param request 店铺、巴西站点和 FBA 货件编号
     * @return Amazon 返回的发票处理状态
     */
    @PostMapping("/invoices/status")
    @Operation(summary = "查询 Amazon 货件发票状态")
    @PreAuthorize("@ss.hasPermission('amazon:shipment-invoicing:query')")
    public CommonResult<Map<String, Object>> getInvoiceStatus(@Valid @RequestBody ShipmentInvoicingRequestVO request) {
        return CommonResult.success(shipmentInvoicingService.getInvoiceStatus(request));
    }
}
