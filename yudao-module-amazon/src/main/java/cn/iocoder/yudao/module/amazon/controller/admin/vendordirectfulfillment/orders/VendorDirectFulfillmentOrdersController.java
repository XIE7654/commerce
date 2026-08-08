package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.orders;

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

/** Amazon Vendor Direct Fulfillment Orders 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Direct Fulfillment Orders")
@RestController
@RequestMapping("/amazon/vendor-direct-fulfillment/orders")
@Validated
public class VendorDirectFulfillmentOrdersController {

    @Resource
    private VendorDirectFulfillmentService vendorDirectFulfillmentService;

    /**
     * 查询 V1 采购订单列表。
     *
     * @param request 店铺、站点和订单查询条件
     * @return Amazon 返回的采购订单列表
     */
    @PostMapping("/v1/purchase-orders")
    @Operation(summary = "查询 Direct Fulfillment V1 采购订单")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:orders:query')")
    public CommonResult<Map<String, Object>> getOrdersV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getOrders", "GET", "/vendor/directFulfillment/orders/v1/purchaseOrders");
    }

    /**
     * 查询 V1 单个采购订单。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的采购订单详情
     */
    @PostMapping("/v1/purchase-orders/get")
    @Operation(summary = "查询 Direct Fulfillment V1 采购订单详情")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:orders:query')")
    public CommonResult<Map<String, Object>> getOrderV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getOrder", "GET", "/vendor/directFulfillment/orders/v1/purchaseOrders/{purchaseOrderNumber}");
    }

    /**
     * 提交 V1 采购订单确认。
     *
     * @param request 店铺、站点和订单确认请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/v1/acknowledgements")
    @Operation(summary = "提交 Direct Fulfillment V1 订单确认")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:orders:update')")
    public CommonResult<Map<String, Object>> submitAcknowledgementV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "submitAcknowledgement", "POST", "/vendor/directFulfillment/orders/v1/acknowledgements");
    }

    /**
     * 查询 2021-12-28 版本采购订单列表。
     *
     * @param request 店铺、站点和订单查询条件
     * @return Amazon 返回的采购订单列表
     */
    @PostMapping("/2021-12-28/purchase-orders")
    @Operation(summary = "查询 Direct Fulfillment 采购订单")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:orders:query')")
    public CommonResult<Map<String, Object>> getOrders20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getOrders20211228", "GET", "/vendor/directFulfillment/orders/2021-12-28/purchaseOrders");
    }

    /**
     * 查询 2021-12-28 版本单个采购订单。
     *
     * @param request 店铺、站点和 purchaseOrderNumber
     * @return Amazon 返回的采购订单详情
     */
    @PostMapping("/2021-12-28/purchase-orders/get")
    @Operation(summary = "查询 Direct Fulfillment 采购订单详情")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:orders:query')")
    public CommonResult<Map<String, Object>> getOrder20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getOrder20211228", "GET", "/vendor/directFulfillment/orders/2021-12-28/purchaseOrders/{purchaseOrderNumber}");
    }

    /**
     * 提交 2021-12-28 版本采购订单确认。
     *
     * @param request 店铺、站点和订单确认请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/2021-12-28/acknowledgements")
    @Operation(summary = "提交 Direct Fulfillment 订单确认")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:orders:update')")
    public CommonResult<Map<String, Object>> submitAcknowledgement20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "submitAcknowledgement20211228", "POST", "/vendor/directFulfillment/orders/2021-12-28/acknowledgements");
    }

    /**
     * 将受校验请求委托给 Service，Controller 不处理 Amazon 调用。
     *
     * @param request 已校验的 API 请求
     * @param operation Amazon 操作名称
     * @param method HTTP 请求方式
     * @param path 固定的 Amazon API 路径模板
     * @return Amazon 原始 JSON 响应
     */
    private CommonResult<Map<String, Object>> call(VendorDirectFulfillmentRequestVO request, String operation,
                                                    String method, String path) {
        return CommonResult.success(vendorDirectFulfillmentService.invoke(request, operation, method, path));
    }
}
