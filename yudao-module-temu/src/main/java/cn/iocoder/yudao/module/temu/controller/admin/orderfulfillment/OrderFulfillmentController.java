package cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment;

import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentConfirmReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentInfoReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.orderfulfillment.vo.OrderFulfillmentShipmentQueryReqVO;
import cn.iocoder.yudao.module.temu.service.orderfulfillment.OrderFulfillmentService;
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
import tools.jackson.databind.JsonNode;

/**
 * 管理后台 Order Fulfillment 订单履约接口。
 */
@Tag(name = "管理后台 - Order Fulfillment")
@RestController
@RequestMapping("/temu/order-fulfillment")
@Validated
public class OrderFulfillmentController {

    @Resource
    private OrderFulfillmentService orderFulfillmentService;

    /**
     * 确认 Temu 订单发货。
     *
     * @param request 发货方式及包裹明细
     * @return Temu 官方发货确认响应
     */
    @PostMapping("/shipments/confirm")
    @Operation(summary = "确认 Temu 订单发货")
    @PreAuthorize("@ss.hasPermission('temu:order-fulfillment:update')")
    public JsonNode confirmShipment(@Valid @RequestBody OrderFulfillmentShipmentConfirmReqVO request) {
        return orderFulfillmentService.confirmShipment(request);
    }

    /**
     * 查询 Temu 订单发货信息。
     *
     * @param request 父订单与子订单查询参数
     * @return Temu 官方发货信息响应
     */
    @PostMapping("/shipments/info")
    @Operation(summary = "查询 Temu 订单发货信息")
    @PreAuthorize("@ss.hasPermission('temu:order-fulfillment:query')")
    public JsonNode getShipmentInfo(@Valid @RequestBody OrderFulfillmentShipmentInfoReqVO request) {
        return orderFulfillmentService.getShipmentInfo(request);
    }

    /**
     * 查询 Temu 包裹物流信息。
     *
     * @param request 包裹、物流公司和运单查询参数
     * @return Temu 官方包裹物流信息响应
     */
    @PostMapping("/shipments/detail")
    @Operation(summary = "查询 Temu 包裹物流信息")
    @PreAuthorize("@ss.hasPermission('temu:order-fulfillment:query')")
    public JsonNode getShipment(@Valid @RequestBody OrderFulfillmentShipmentQueryReqVO request) {
        return orderFulfillmentService.getShipment(request);
    }
}
