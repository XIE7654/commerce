package cn.iocoder.yudao.module.amazon.controller.admin.vendorshipments;

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

/** Amazon Vendor Retail Procurement Shipments 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Shipments")
@RestController
@RequestMapping("/amazon/vendor-shipments")
@Validated
public class VendorShipmentsController {
    @Resource private VendorRetailProcurementService vendorRetailProcurementService;

    /** 提交 Vendor 货件确认。@param request 店铺、站点和货件确认内容 @return Amazon 异步交易信息 */
    @PostMapping("/confirmations/submit") @Operation(summary = "提交 Amazon Vendor 货件确认") @PreAuthorize("@ss.hasPermission('amazon:vendor-shipments:update')")
    public CommonResult<Map<String, Object>> submitShipmentConfirmations(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.submitShipmentConfirmations(request)); }

    /** 提交 Vendor 货件。@param request 店铺、站点和货件内容 @return Amazon 异步交易信息 */
    @PostMapping("/submit") @Operation(summary = "提交 Amazon Vendor 货件") @PreAuthorize("@ss.hasPermission('amazon:vendor-shipments:create')")
    public CommonResult<Map<String, Object>> submitShipments(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.submitShipments(request)); }

    /** 查询 Vendor 货件详情。@param request 店铺、站点和货件筛选参数 @return 货件详情列表 */
    @PostMapping("/list") @Operation(summary = "查询 Amazon Vendor 货件") @PreAuthorize("@ss.hasPermission('amazon:vendor-shipments:query')")
    public CommonResult<Map<String, Object>> getShipmentDetails(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.getShipmentDetails(request)); }

    /** 查询 Vendor 运输标签。@param request 店铺、站点和标签筛选参数 @return 运输标签列表 */
    @PostMapping("/labels/list") @Operation(summary = "查询 Amazon Vendor 运输标签") @PreAuthorize("@ss.hasPermission('amazon:vendor-shipments:query')")
    public CommonResult<Map<String, Object>> getShipmentLabels(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.getShipmentLabels(request)); }
}
