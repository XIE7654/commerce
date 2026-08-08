package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.shipping;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.vo.VendorDirectFulfillmentRequestVO;
import cn.iocoder.yudao.module.amazon.service.vendordirectfulfillment.VendorDirectFulfillmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Amazon Vendor Direct Fulfillment Shipping 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Direct Fulfillment Shipping")
@RestController
@RequestMapping("/amazon/vendor-direct-fulfillment/shipping")
@Validated
public class VendorDirectFulfillmentShippingController {

    @Resource
    private VendorDirectFulfillmentService vendorDirectFulfillmentService;

    /**
     * 查询 V1 发货标签列表。
     *
     * @param request 店铺、站点和标签筛选条件
     * @return Amazon 返回的标签列表
     */
    @PostMapping("/v1/shipping-labels") @Operation(summary = "查询 Direct Fulfillment V1 发货标签") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getShippingLabelsV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getShippingLabels", "GET", "/vendor/directFulfillment/shipping/v1/shippingLabels"); }

    /**
     * 提交 V1 发货标签请求。
     *
     * @param request 店铺、站点和标签请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/v1/shipping-labels/submit") @Operation(summary = "提交 Direct Fulfillment V1 发货标签请求") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> submitShippingLabelRequestV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "submitShippingLabelRequest", "POST", "/vendor/directFulfillment/shipping/v1/shippingLabels"); }

    /**
     * 查询 V1 指定采购订单的发货标签。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的标签详情
     */
    @PostMapping("/v1/shipping-labels/get") @Operation(summary = "查询 Direct Fulfillment V1 发货标签详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getShippingLabelV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getShippingLabel", "GET", "/vendor/directFulfillment/shipping/v1/shippingLabels/{purchaseOrderNumber}"); }

    /**
     * 提交 V1 发货确认。
     *
     * @param request 店铺、站点和发货确认请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/v1/shipment-confirmations") @Operation(summary = "提交 Direct Fulfillment V1 发货确认") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> submitShipmentConfirmationsV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "submitShipmentConfirmations", "POST", "/vendor/directFulfillment/shipping/v1/shipmentConfirmations"); }

    /**
     * 提交 V1 发货状态更新。
     *
     * @param request 店铺、站点和发货状态请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/v1/shipment-status-updates") @Operation(summary = "提交 Direct Fulfillment V1 发货状态更新") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> submitShipmentStatusUpdatesV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "submitShipmentStatusUpdates", "POST", "/vendor/directFulfillment/shipping/v1/shipmentStatusUpdates"); }

    /**
     * 查询 V1 客户发票列表。
     *
     * @param request 店铺、站点和发票筛选条件
     * @return Amazon 返回的客户发票列表
     */
    @PostMapping("/v1/customer-invoices") @Operation(summary = "查询 Direct Fulfillment V1 客户发票") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getCustomerInvoicesV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getCustomerInvoices", "GET", "/vendor/directFulfillment/shipping/v1/customerInvoices"); }

    /**
     * 查询 V1 指定采购订单的客户发票。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的客户发票详情
     */
    @PostMapping("/v1/customer-invoices/get") @Operation(summary = "查询 Direct Fulfillment V1 客户发票详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getCustomerInvoiceV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getCustomerInvoice", "GET", "/vendor/directFulfillment/shipping/v1/customerInvoices/{purchaseOrderNumber}"); }

    /**
     * 查询 V1 装箱单列表。
     *
     * @param request 店铺、站点和装箱单筛选条件
     * @return Amazon 返回的装箱单列表
     */
    @PostMapping("/v1/packing-slips") @Operation(summary = "查询 Direct Fulfillment V1 装箱单") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getPackingSlipsV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getPackingSlips", "GET", "/vendor/directFulfillment/shipping/v1/packingSlips"); }

    /**
     * 查询 V1 指定采购订单的装箱单。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的装箱单详情
     */
    @PostMapping("/v1/packing-slips/get") @Operation(summary = "查询 Direct Fulfillment V1 装箱单详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getPackingSlipV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getPackingSlip", "GET", "/vendor/directFulfillment/shipping/v1/packingSlips/{purchaseOrderNumber}"); }

    /**
     * 查询 2021-12-28 发货标签列表。
     *
     * @param request 店铺、站点和标签筛选条件
     * @return Amazon 返回的标签列表
     */
    @PostMapping("/2021-12-28/shipping-labels") @Operation(summary = "查询 Direct Fulfillment 发货标签") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getShippingLabels20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getShippingLabels20211228", "GET", "/vendor/directFulfillment/shipping/2021-12-28/shippingLabels"); }

    /**
     * 提交 2021-12-28 发货标签请求。
     *
     * @param request 店铺、站点和标签请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/2021-12-28/shipping-labels/submit") @Operation(summary = "提交 Direct Fulfillment 发货标签请求") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> submitShippingLabelRequest20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "submitShippingLabelRequest20211228", "POST", "/vendor/directFulfillment/shipping/2021-12-28/shippingLabels"); }

    /**
     * 查询 2021-12-28 指定采购订单的发货标签。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的标签详情
     */
    @PostMapping("/2021-12-28/shipping-labels/get") @Operation(summary = "查询 Direct Fulfillment 发货标签详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getShippingLabel20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getShippingLabel20211228", "GET", "/vendor/directFulfillment/shipping/2021-12-28/shippingLabels/{purchaseOrderNumber}"); }

    /**
     * 为 2021-12-28 指定采购订单创建发货标签。
     *
     * @param request 店铺、站点、purchaseOrderNumber 和标签请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/2021-12-28/shipping-labels/create") @Operation(summary = "创建 Direct Fulfillment 发货标签") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> createShippingLabels20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "createShippingLabels", "POST", "/vendor/directFulfillment/shipping/2021-12-28/shippingLabels/{purchaseOrderNumber}"); }

    /**
     * 提交 2021-12-28 发货确认。
     *
     * @param request 店铺、站点和发货确认请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/2021-12-28/shipment-confirmations") @Operation(summary = "提交 Direct Fulfillment 发货确认") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> submitShipmentConfirmations20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "submitShipmentConfirmations20211228", "POST", "/vendor/directFulfillment/shipping/2021-12-28/shipmentConfirmations"); }

    /**
     * 提交 2021-12-28 发货状态更新。
     *
     * @param request 店铺、站点和发货状态请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/2021-12-28/shipment-status-updates") @Operation(summary = "提交 Direct Fulfillment 发货状态更新") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> submitShipmentStatusUpdates20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "submitShipmentStatusUpdates20211228", "POST", "/vendor/directFulfillment/shipping/2021-12-28/shipmentStatusUpdates"); }

    /**
     * 查询 2021-12-28 客户发票列表。
     *
     * @param request 店铺、站点和发票筛选条件
     * @return Amazon 返回的客户发票列表
     */
    @PostMapping("/2021-12-28/customer-invoices") @Operation(summary = "查询 Direct Fulfillment 客户发票") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getCustomerInvoices20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getCustomerInvoices20211228", "GET", "/vendor/directFulfillment/shipping/2021-12-28/customerInvoices"); }

    /**
     * 查询 2021-12-28 指定采购订单的客户发票。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的客户发票详情
     */
    @PostMapping("/2021-12-28/customer-invoices/get") @Operation(summary = "查询 Direct Fulfillment 客户发票详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getCustomerInvoice20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getCustomerInvoice20211228", "GET", "/vendor/directFulfillment/shipping/2021-12-28/customerInvoices/{purchaseOrderNumber}"); }

    /**
     * 查询 2021-12-28 装箱单列表。
     *
     * @param request 店铺、站点和装箱单筛选条件
     * @return Amazon 返回的装箱单列表
     */
    @PostMapping("/2021-12-28/packing-slips") @Operation(summary = "查询 Direct Fulfillment 装箱单") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getPackingSlips20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getPackingSlips20211228", "GET", "/vendor/directFulfillment/shipping/2021-12-28/packingSlips"); }

    /**
     * 查询 2021-12-28 指定采购订单的装箱单。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的装箱单详情
     */
    @PostMapping("/2021-12-28/packing-slips/get") @Operation(summary = "查询 Direct Fulfillment 装箱单详情") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:query')")
    public CommonResult<Map<String, Object>> getPackingSlip20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "getPackingSlip20211228", "GET", "/vendor/directFulfillment/shipping/2021-12-28/packingSlips/{purchaseOrderNumber}"); }

    /**
     * 创建 2021-12-28 容器标签。
     *
     * @param request 店铺、站点和容器标签请求体
     * @return Amazon 返回的容器标签
     */
    @PostMapping("/2021-12-28/container-label") @Operation(summary = "创建 Direct Fulfillment 容器标签") @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:shipping:update')")
    public CommonResult<Map<String, Object>> createContainerLabel20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) { return call(request, "createContainerLabel", "POST", "/vendor/directFulfillment/shipping/2021-12-28/containerLabel"); }

    /**
     * 将受校验请求委托给 Service，Controller 不处理 Amazon 调用。
     *
     * @param request 已校验的 API 请求
     * @param operation Amazon 操作名称
     * @param method HTTP 请求方式
     * @param path 固定的 Amazon API 路径模板
     * @return Amazon 原始 JSON 响应
     */
    private CommonResult<Map<String, Object>> call(VendorDirectFulfillmentRequestVO request, String operation, String method, String path) {
        return CommonResult.success(vendorDirectFulfillmentService.invoke(request, operation, method, path));
    }
}
