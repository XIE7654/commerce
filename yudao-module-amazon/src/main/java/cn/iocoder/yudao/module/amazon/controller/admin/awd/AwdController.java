package cn.iocoder.yudao.module.amazon.controller.admin.awd;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.awd.vo.AwdRequestVO;
import cn.iocoder.yudao.module.amazon.service.awd.AwdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Amazon Warehousing and Distribution (AWD) 管理接口。 */
@Tag(name = "管理后台 - Amazon AWD")
@RestController
@RequestMapping("/amazon/awd")
@Validated
public class AwdController {
    @Resource private AwdService awdService;

    /** 创建 AWD 入库订单。 */
    @PostMapping("/inbound-orders") @Operation(summary = "创建 AWD 入库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:create')")
    public CommonResult<Map<String,Object>> createInbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"createInboundOrder","POST","/inboundOrders"); }
    /** 查询 AWD 入库订单。 */
    @PostMapping("/inbound-orders/detail") @Operation(summary = "查询 AWD 入库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> getInbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"getInboundOrder","GET","/inboundOrders/{id}"); }
    /** 更新 AWD 入库订单。 */
    @PostMapping("/inbound-orders/update") @Operation(summary = "更新 AWD 入库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> updateInbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"updateInboundOrder","PUT","/inboundOrders/{id}"); }
    /** 取消 AWD 入库订单。 */
    @PostMapping("/inbound-orders/cancel") @Operation(summary = "取消 AWD 入库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> cancelInbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"cancelInboundOrder","POST","/inboundOrders/{id}/cancellation"); }
    /** 确认 AWD 入库订单。 */
    @PostMapping("/inbound-orders/confirm") @Operation(summary = "确认 AWD 入库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> confirmInbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"confirmInboundOrder","POST","/inboundOrders/{id}/confirmation"); }
    /** 查询入库货件列表。 */
    @PostMapping("/inbound-shipments/list") @Operation(summary = "查询 AWD 入库货件") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> listShipments(@Valid @RequestBody AwdRequestVO r) { return call(r,"listInboundShipments","GET","/inboundShipments"); }
    /** 查询入库货件详情。 */
    @PostMapping("/inbound-shipments/detail") @Operation(summary = "查询 AWD 入库货件详情") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> shipment(@Valid @RequestBody AwdRequestVO r) { return call(r,"getInboundShipment","GET","/inboundShipments/{id}"); }
    /** 查询货件标签页类型。 */
    @PostMapping("/inbound-shipments/label-page-types") @Operation(summary = "查询 AWD 标签页类型") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> labelTypes(@Valid @RequestBody AwdRequestVO r) { return call(r,"getLabelPageTypes","GET","/inboundShipments/{id}/labelPageTypes"); }
    /** 获取货件标签。 */
    @PostMapping("/inbound-shipments/labels") @Operation(summary = "获取 AWD 货件标签") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> labels(@Valid @RequestBody AwdRequestVO r) { return call(r,"getInboundShipmentLabels","GET","/inboundShipments/{id}/labels"); }
    /** 更新货件运输信息。 */
    @PostMapping("/inbound-shipments/transport") @Operation(summary = "更新 AWD 货件运输信息") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> transport(@Valid @RequestBody AwdRequestVO r) { return call(r,"updateInboundShipmentTransport","PUT","/inboundShipments/{id}/transport"); }
    /** 查询入库资格。 */
    @PostMapping("/inbound-eligibility") @Operation(summary = "查询 AWD 入库资格") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> eligibility(@Valid @RequestBody AwdRequestVO r) { return call(r,"getInboundEligibility","POST","/inboundEligibility"); }
    /** 查询 AWD 库存。 */
    @PostMapping("/inventory") @Operation(summary = "查询 AWD 库存") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> inventory(@Valid @RequestBody AwdRequestVO r) { return call(r,"listInventory","GET","/inventory"); }
    /** 查询出库订单列表。 */
    @PostMapping("/outbound-orders/list") @Operation(summary = "查询 AWD 出库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> listOutbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"listOutboundOrders","GET","/outboundOrders"); }
    /** 创建出库订单。 */
    @PostMapping("/outbound-orders") @Operation(summary = "创建 AWD 出库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:create')")
    public CommonResult<Map<String,Object>> createOutbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"createOutboundOrder","POST","/outboundOrders"); }
    /** 查询出库订单详情。 */
    @PostMapping("/outbound-orders/detail") @Operation(summary = "查询 AWD 出库订单详情") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> outbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"getOutboundOrder","GET","/outboundOrders/{id}"); }
    /** 更新出库订单。 */
    @PostMapping("/outbound-orders/update") @Operation(summary = "更新 AWD 出库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> updateOutbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"updateOutboundOrder","PUT","/outboundOrders/{id}"); }
    /** 确认出库订单。 */
    @PostMapping("/outbound-orders/confirm") @Operation(summary = "确认 AWD 出库订单") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> confirmOutbound(@Valid @RequestBody AwdRequestVO r) { return call(r,"confirmOutboundOrder","POST","/outboundOrders/{id}/confirmation"); }
    /** 查询补货订单列表。 */
    @PostMapping("/replenishment-orders/list") @Operation(summary = "查询 AWD 补货订单") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> listReplenishment(@Valid @RequestBody AwdRequestVO r) { return call(r,"listReplenishmentOrders","GET","/replenishmentOrders"); }
    /** 创建补货订单。 */
    @PostMapping("/replenishment-orders") @Operation(summary = "创建 AWD 补货订单") @PreAuthorize("@ss.hasPermission('amazon:awd:create')")
    public CommonResult<Map<String,Object>> createReplenishment(@Valid @RequestBody AwdRequestVO r) { return call(r,"createReplenishmentOrder","POST","/replenishmentOrders"); }
    /** 查询补货订单详情。 */
    @PostMapping("/replenishment-orders/detail") @Operation(summary = "查询 AWD 补货订单详情") @PreAuthorize("@ss.hasPermission('amazon:awd:query')")
    public CommonResult<Map<String,Object>> replenishment(@Valid @RequestBody AwdRequestVO r) { return call(r,"getReplenishmentOrder","GET","/replenishmentOrders/{id}"); }
    /** 确认补货订单。 */
    @PostMapping("/replenishment-orders/confirm") @Operation(summary = "确认 AWD 补货订单") @PreAuthorize("@ss.hasPermission('amazon:awd:update')")
    public CommonResult<Map<String,Object>> confirmReplenishment(@Valid @RequestBody AwdRequestVO r) { return call(r,"confirmReplenishmentOrder","POST","/replenishmentOrders/{id}/confirmation"); }
    /** 调用 AWD 服务。 */
    private CommonResult<Map<String,Object>> call(AwdRequestVO r, String op, String method, String path) { return CommonResult.success(awdService.invoke(r, op, method, path)); }
}
