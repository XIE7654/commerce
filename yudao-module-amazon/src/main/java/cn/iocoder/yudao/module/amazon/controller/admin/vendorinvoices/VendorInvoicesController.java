package cn.iocoder.yudao.module.amazon.controller.admin.vendorinvoices;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.vendorretailprocurement.vo.VendorRetailProcurementRequestVO;
import cn.iocoder.yudao.module.amazon.service.vendorretailprocurement.VendorRetailProcurementService;
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

/** Amazon Vendor Retail Procurement Invoices 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Invoices")
@RestController
@RequestMapping("/amazon/vendor-invoices")
@Validated
public class VendorInvoicesController {
    @Resource private VendorRetailProcurementService vendorRetailProcurementService;

    /** 提交 Vendor 发票。@param request 店铺、站点和发票内容 @return Amazon 异步交易信息 */
    @PostMapping("/submit") @Operation(summary = "提交 Amazon Vendor 发票") @PreAuthorize("@ss.hasPermission('amazon:vendor-invoices:create')")
    public CommonResult<Map<String, Object>> submitInvoices(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.submitInvoices(request)); }
}
