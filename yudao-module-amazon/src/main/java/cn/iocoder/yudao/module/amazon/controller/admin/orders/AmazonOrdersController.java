package cn.iocoder.yudao.module.amazon.controller.admin.orders;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderRegulatedInfoUpdateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentConfirmationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderShipmentReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrder2026GetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrders2026ListReqVO;
import cn.iocoder.yudao.module.amazon.service.orders.AmazonOrdersService;
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

/**
 * Amazon Orders 管理接口。
 */
@Tag(name = "管理后台 - Amazon Orders")
@RestController
@RequestMapping("/amazon/orders")
@Validated
public class AmazonOrdersController {

    @Resource
    private AmazonOrdersService amazonOrdersService;

    /**
     * 查询指定店铺和站点的订单列表。
     *
     * @param request 店铺、站点和订单筛选条件
     * @return Amazon 订单列表原始响应
     */
    @PostMapping("/list")
    @Operation(summary = "查询 Amazon 订单列表")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrders(@Valid @RequestBody AmazonOrdersListReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrders(request));
    }

    /**
     * 查询指定 Amazon 订单详情。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 订单详情原始响应
     */
    @PostMapping("/detail")
    @Operation(summary = "查询 Amazon 订单详情")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrder(@Valid @RequestBody AmazonOrderGetReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrder(request));
    }

    /**
     * 查询指定 Amazon 订单的商品。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 订单商品原始响应
     */
    @PostMapping("/items")
    @Operation(summary = "查询 Amazon 订单商品")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrderItems(@Valid @RequestBody AmazonOrderItemsReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrderItems(request));
    }

    /**
     * 查询指定 Amazon 订单商品的买家信息。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 订单商品买家信息原始响应
     */
    @PostMapping("/items/buyer-info")
    @Operation(summary = "查询 Amazon 订单商品买家信息")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrderItemsBuyerInfo(@Valid @RequestBody AmazonOrderItemsReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrderItemsBuyerInfo(request));
    }

    /**
     * 查询指定 Amazon 订单的买家信息。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 订单买家信息原始响应
     */
    @PostMapping("/buyer-info")
    @Operation(summary = "查询 Amazon 订单买家信息")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrderBuyerInfo(@Valid @RequestBody AmazonOrderGetReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrderBuyerInfo(request));
    }

    /**
     * 查询指定 Amazon 订单的收货地址。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 订单收货地址原始响应
     */
    @PostMapping("/address")
    @Operation(summary = "查询 Amazon 订单收货地址")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrderAddress(@Valid @RequestBody AmazonOrderGetReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrderAddress(request));
    }

    /**
     * 查询指定 Amazon 订单的受监管信息。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 受监管订单信息原始响应
     */
    @PostMapping("/regulated-info")
    @Operation(summary = "查询 Amazon 受监管订单信息")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrderRegulatedInfo(@Valid @RequestBody AmazonOrderGetReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrderRegulatedInfo(request));
    }

    /**
     * 更新 Easy Ship 订单的发货状态。
     *
     * @param request 订单、站点和发货状态
     * @return Amazon 原始响应
     */
    @PostMapping("/shipment")
    @Operation(summary = "更新 Amazon Easy Ship 发货状态")
    @PreAuthorize("@ss.hasPermission('amazon:orders:update')")
    public CommonResult<Map<String, Object>> updateShipmentStatus(@Valid @RequestBody AmazonOrderShipmentReqVO request) {
        return CommonResult.success(amazonOrdersService.updateShipmentStatus(request));
    }

    /**
     * 确认卖家自配送订单的发货信息。
     *
     * @param request 订单、站点和包裹明细
     * @return Amazon 原始响应
     */
    @PostMapping("/shipment-confirmation")
    @Operation(summary = "确认 Amazon 订单发货")
    @PreAuthorize("@ss.hasPermission('amazon:orders:update')")
    public CommonResult<Map<String, Object>> confirmShipment(@Valid @RequestBody AmazonOrderShipmentConfirmationReqVO request) {
        return CommonResult.success(amazonOrdersService.confirmShipment(request));
    }

    /**
     * 更新受监管订单的验证状态。
     *
     * @param request 订单、站点和验证状态
     * @return Amazon 原始响应
     */
    @PostMapping("/regulated-info/update")
    @Operation(summary = "更新 Amazon 受监管订单验证状态")
    @PreAuthorize("@ss.hasPermission('amazon:orders:update')")
    public CommonResult<Map<String, Object>> updateOrderRegulatedInfo(
            @Valid @RequestBody AmazonOrderRegulatedInfoUpdateReqVO request) {
        return CommonResult.success(amazonOrdersService.updateOrderRegulatedInfo(request));
    }

    /**
     * 查询 Orders 2026-01-01 版本的订单列表。
     *
     * @param request 店铺、站点和新版筛选条件
     * @return Amazon 原始响应
     */
    @PostMapping("/v2026/list")
    @Operation(summary = "查询 Amazon Orders 2026 订单列表")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrders2026(@Valid @RequestBody AmazonOrders2026ListReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrders2026(request));
    }

    /**
     * 查询 Orders 2026-01-01 版本的指定订单。
     *
     * @param request 店铺、站点、订单编号和返回数据集
     * @return Amazon 原始响应
     */
    @PostMapping("/v2026/detail")
    @Operation(summary = "查询 Amazon Orders 2026 订单详情")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<Map<String, Object>> getOrder2026(@Valid @RequestBody AmazonOrder2026GetReqVO request) {
        return CommonResult.success(amazonOrdersService.getOrder2026(request));
    }

}
