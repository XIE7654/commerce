package cn.iocoder.yudao.module.amazon.controller.admin.externalfulfillment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.externalfulfillment.vo.ExternalFulfillmentRequestVO;
import cn.iocoder.yudao.module.amazon.service.externalfulfillment.ExternalFulfillmentService;
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

/** Amazon External Fulfillment 管理接口。 */
@Tag(name = "管理后台 - Amazon External Fulfillment")
@RestController
@RequestMapping("/amazon/external-fulfillment")
@Validated
public class ExternalFulfillmentController {

    @Resource
    private ExternalFulfillmentService externalFulfillmentService;

    /** 批量更新或查询 External Fulfillment 库存。 */
    @PostMapping("/inventory/batch")
    @Operation(summary = "批量处理 External Fulfillment 库存")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:update')")
    public CommonResult<Map<String, Object>> batchInventory(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.batchInventory(request));
    }

    /** 查询 External Fulfillment 退货列表。 */
    @PostMapping("/returns/list")
    @Operation(summary = "查询 External Fulfillment 退货列表")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:query')")
    public CommonResult<Map<String, Object>> listReturns(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.listReturns(request));
    }

    /** 查询指定 External Fulfillment 退货。 */
    @PostMapping("/returns/detail")
    @Operation(summary = "查询 External Fulfillment 退货详情")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:query')")
    public CommonResult<Map<String, Object>> getReturn(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.getReturn(request));
    }

    /** 查询 External Fulfillment 货件列表。 */
    @PostMapping("/shipments/list")
    @Operation(summary = "查询 External Fulfillment 货件列表")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:query')")
    public CommonResult<Map<String, Object>> getShipments(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.getShipments(request));
    }

    /** 查询指定 External Fulfillment 货件。 */
    @PostMapping("/shipments/detail")
    @Operation(summary = "查询 External Fulfillment 货件详情")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:query')")
    public CommonResult<Map<String, Object>> getShipment(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.getShipment(request));
    }

    /** 确认或拒绝 External Fulfillment 货件。 */
    @PostMapping("/shipments/process")
    @Operation(summary = "确认或拒绝 External Fulfillment 货件")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:update')")
    public CommonResult<Map<String, Object>> processShipment(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.processShipment(request));
    }

    /** 为 External Fulfillment 货件创建包裹。 */
    @PostMapping("/shipments/packages/create")
    @Operation(summary = "创建 External Fulfillment 货件包裹")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:create')")
    public CommonResult<Map<String, Object>> createPackages(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.createPackages(request));
    }

    /** 更新 External Fulfillment 货件包裹。 */
    @PostMapping("/shipments/packages/update")
    @Operation(summary = "更新 External Fulfillment 货件包裹")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:update')")
    public CommonResult<Map<String, Object>> updatePackage(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.updatePackage(request));
    }

    /** 更新 External Fulfillment 包裹状态。 */
    @PostMapping("/shipments/packages/status")
    @Operation(summary = "更新 External Fulfillment 包裹状态")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:update')")
    public CommonResult<Map<String, Object>> updatePackageStatus(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.updatePackageStatus(request));
    }

    /** 查询 External Fulfillment 包裹配送选项。 */
    @PostMapping("/shipments/shipping-options")
    @Operation(summary = "查询 External Fulfillment 配送选项")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:query')")
    public CommonResult<Map<String, Object>> retrieveShippingOptions(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.retrieveShippingOptions(request));
    }

    /** 生成 External Fulfillment 货件发票。 */
    @PostMapping("/shipments/invoice/generate")
    @Operation(summary = "生成 External Fulfillment 发票")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:create')")
    public CommonResult<Map<String, Object>> generateInvoice(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.generateInvoice(request));
    }

    /** 获取 External Fulfillment 货件发票。 */
    @PostMapping("/shipments/invoice")
    @Operation(summary = "获取 External Fulfillment 发票")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:query')")
    public CommonResult<Map<String, Object>> retrieveInvoice(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.retrieveInvoice(request));
    }

    /** 生成或重新生成 External Fulfillment 货件面单。 */
    @PostMapping("/shipments/ship-labels/generate")
    @Operation(summary = "生成 External Fulfillment 货件面单")
    @PreAuthorize("@ss.hasPermission('amazon:external-fulfillment:update')")
    public CommonResult<Map<String, Object>> generateShipLabels(@Valid @RequestBody ExternalFulfillmentRequestVO request) {
        return CommonResult.success(externalFulfillmentService.generateShipLabels(request));
    }
}
