package cn.iocoder.yudao.module.temu.controller.admin.buyshipping;

import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingLabelReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingServicesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipLaterConfirmReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipLaterPackagesReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipmentCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo.BuyShippingShipmentUpdateReqVO;
import cn.iocoder.yudao.module.temu.service.buyshipping.BuyShippingService;
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
 * 管理后台 Buy Shipping 购单发货接口。
 */
@Tag(name = "管理后台 - Buy Shipping")
@RestController
@RequestMapping("/temu/buy-shipping")
@Validated
public class BuyShippingController {

    @Resource
    private BuyShippingService buyShippingService;

    /**
     * 查询订单可购买的物流服务。
     *
     * @param request 仓库、包裹尺寸和订单参数
     * @return Temu 官方物流服务响应
     */
    @PostMapping("/shipping-services/list")
    @Operation(summary = "查询 Temu 可购买物流服务")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:query')")
    public JsonNode getShippingServices(@Valid @RequestBody BuyShippingServicesReqVO request) {
        return buyShippingService.getShippingServices(request);
    }

    /**
     * 查询可用于购单发货的仓库。
     *
     * @param request 站点和授权参数
     * @return Temu 官方仓库列表响应
     */
    @PostMapping("/warehouses/list")
    @Operation(summary = "查询 Temu 购单发货仓库")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:query')")
    public JsonNode getWarehouseList(@Valid @RequestBody BuyShippingBaseReqVO request) {
        return buyShippingService.getWarehouseList(request);
    }

    /**
     * 创建 Temu 购单发货货件。
     *
     * @param request 发货方式和货件明细
     * @return Temu 官方创建货件响应
     */
    @PostMapping("/shipments/create")
    @Operation(summary = "创建 Temu 购单发货货件")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:update')")
    public JsonNode createShipment(@Valid @RequestBody BuyShippingShipmentCreateReqVO request) {
        return buyShippingService.createShipment(request);
    }

    /**
     * 更新需重试的 Temu 购单发货货件。
     *
     * @param request 重试包裹明细
     * @return Temu 官方更新货件响应
     */
    @PostMapping("/shipments/update")
    @Operation(summary = "更新 Temu 购单发货货件")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:update')")
    public JsonNode updateShipment(@Valid @RequestBody BuyShippingShipmentUpdateReqVO request) {
        return buyShippingService.updateShipment(request);
    }

    /**
     * 获取已创建货件的运输面单。
     *
     * @param request 包裹编号列表
     * @return Temu 官方面单响应
     */
    @PostMapping("/shipping-labels/get")
    @Operation(summary = "获取 Temu 购单发货面单")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:query')")
    public JsonNode getShippingLabel(@Valid @RequestBody BuyShippingLabelReqVO request) {
        return buyShippingService.getShippingLabel(request);
    }

    /**
     * 分页查询待确认发货的包裹。
     *
     * @param request 分页查询参数
     * @return Temu 官方待发货包裹响应
     */
    @PostMapping("/ship-later-packages/list")
    @Operation(summary = "查询 Temu 待发货包裹")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:query')")
    public JsonNode getShipLaterPackages(@Valid @RequestBody BuyShippingShipLaterPackagesReqVO request) {
        return buyShippingService.getShipLaterPackages(request);
    }

    /**
     * 确认待发货包裹已交由承运商。
     *
     * @param request 包裹发货确认明细
     * @return Temu 官方发货确认响应
     */
    @PostMapping("/ship-later-packages/confirm")
    @Operation(summary = "确认 Temu 待发货包裹已发货")
    @PreAuthorize("@ss.hasPermission('temu:buy-shipping:update')")
    public JsonNode confirmShipLaterPackagesShipped(@Valid @RequestBody BuyShippingShipLaterConfirmReqVO request) {
        return buyShippingService.confirmShipLaterPackagesShipped(request);
    }
}
