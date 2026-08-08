package cn.iocoder.yudao.module.amazon.controller.admin.vendororders;

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

/** Amazon Vendor Retail Procurement Orders 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Orders")
@RestController
@RequestMapping("/amazon/vendor-orders")
@Validated
public class VendorOrdersController {
    @Resource private VendorRetailProcurementService vendorRetailProcurementService;

    /** 查询 Vendor 采购订单列表。@param request 店铺、站点和订单筛选参数 @return 采购订单列表 */
    @PostMapping("/purchase-orders/list") @Operation(summary = "查询 Amazon Vendor 采购订单") @PreAuthorize("@ss.hasPermission('amazon:vendor-orders:query')")
    public CommonResult<Map<String, Object>> getPurchaseOrders(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.getPurchaseOrders(request)); }

    /** 查询指定 Vendor 采购订单。@param request 店铺、站点和采购订单编号 @return 采购订单详情 */
    @PostMapping("/purchase-orders/get") @Operation(summary = "查询 Amazon Vendor 采购订单详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-orders:query')")
    public CommonResult<Map<String, Object>> getPurchaseOrder(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.getPurchaseOrder(request)); }

    /** 提交 Vendor 采购订单确认。@param request 店铺、站点和确认内容 @return 异步交易信息 */
    @PostMapping("/acknowledgements/submit") @Operation(summary = "提交 Amazon Vendor 订单确认") @PreAuthorize("@ss.hasPermission('amazon:vendor-orders:update')")
    public CommonResult<Map<String, Object>> submitAcknowledgement(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.submitAcknowledgement(request)); }

    /** 查询 Vendor 采购订单状态。@param request 店铺、站点和状态筛选参数 @return 订单状态列表 */
    @PostMapping("/purchase-orders/status") @Operation(summary = "查询 Amazon Vendor 采购订单状态") @PreAuthorize("@ss.hasPermission('amazon:vendor-orders:query')")
    public CommonResult<Map<String, Object>> getPurchaseOrdersStatus(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.getPurchaseOrdersStatus(request)); }
}
