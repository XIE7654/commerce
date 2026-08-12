package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement;

import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementCustomOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderSyncReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementParentOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.TemuOrderPageReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.TemuOrderRespVO;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.module.temu.service.ordermanagement.OrderManagementService;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.order.CustomizationOrderListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderDetailDto;
import cn.iocoder.yudao.module.temu.framework.client.order.OrderListDto;
import cn.iocoder.yudao.module.temu.framework.client.order.ShippingInfoDto;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 Order Management 订单管理接口。
 */
@Tag(name = "管理后台 - Order Management")
@RestController
@RequestMapping("/temu/order-management")
@Validated
public class OrderManagementController {

    @Resource
    private OrderManagementService orderManagementService;

    /**
     * 查询 Temu 订单列表。
     *
     * @param request 订单状态、区域和分页查询参数
     * @return Temu 官方订单列表响应；成功时会同步当前页数据到本地订单表
     */
    @PostMapping("/orders/list")
    @Operation(summary = "查询 Temu 订单列表")
    @PreAuthorize("@ss.hasPermission('temu:order-management:query')")
    public TemuApiResponse<OrderListDto> getOrderList(@Valid @RequestBody OrderManagementOrderListReqVO request) {
        return orderManagementService.getOrderList(request);
    }

    /**
     * 查询已同步到本地的 Temu 订单列表。
     *
     * @param request 店铺、卖家及订单筛选条件
     * @return 本地订单分页数据
     */
    @GetMapping("/orders/page")
    @Operation(summary = "查询本地 Temu 订单列表")
    @PreAuthorize("@ss.hasPermission('temu:order-management:query')")
    public CommonResult<PageResult<TemuOrderRespVO>> getLocalOrderPage(@Valid TemuOrderPageReqVO request) {
        PageResult<TemuOrderDO> pageResult = orderManagementService.getLocalOrderPage(request);
        return success(BeanUtils.toBean(pageResult, TemuOrderRespVO.class));
    }

    /**
     * 查询 Temu 父订单详情。
     *
     * @param request 父订单查询参数
     * @return Temu 官方订单详情响应
     */
    @PostMapping("/orders/detail")
    @Operation(summary = "查询 Temu 父订单详情")
    @PreAuthorize("@ss.hasPermission('temu:order-management:query')")
    public TemuApiResponse<OrderDetailDto> getOrderDetail(@Valid @RequestBody OrderManagementParentOrderReqVO request) {
        return orderManagementService.getOrderDetail(request);
    }

    /**
     * 拉取 Temu 父订单详情并保存到本地。
     *
     * @param request 父订单查询参数
     * @return Temu 官方订单详情响应
     */
    @PostMapping("/orders/detail/sync")
    @Operation(summary = "同步 Temu 父订单详情")
    @PreAuthorize("@ss.hasPermission('temu:order-management:update')")
    public TemuApiResponse<OrderDetailDto> syncOrderDetail(@Valid @RequestBody OrderManagementOrderSyncReqVO request) {
        return orderManagementService.syncOrderDetail(request);
    }

    /**
     * 查询 Temu 定制订单详情。
     *
     * @param request 子订单编号列表查询参数
     * @return Temu 官方定制订单详情响应
     */
    @PostMapping("/orders/customization/detail")
    @Operation(summary = "查询 Temu 定制订单详情")
    @PreAuthorize("@ss.hasPermission('temu:order-management:query')")
    public TemuApiResponse<CustomizationOrderListDto> getCustomOrderDetail(@Valid @RequestBody OrderManagementCustomOrderReqVO request) {
        return orderManagementService.getCustomOrderDetail(request);
    }

    /**
     * 查询 Temu 父订单收货信息。
     *
     * @param request 父订单查询参数
     * @return Temu 官方收货信息响应
     */
    @PostMapping("/orders/shipping-info")
    @Operation(summary = "查询 Temu 订单收货信息")
    @PreAuthorize("@ss.hasPermission('temu:order-management:query')")
    public TemuApiResponse<ShippingInfoDto> getOrderShippingInfo(@Valid @RequestBody OrderManagementParentOrderReqVO request) {
        return orderManagementService.getOrderShippingInfo(request);
    }

    /**
     * 拉取 Temu 父订单收货信息并保存到本地。
     *
     * @param request 父订单查询参数
     * @return Temu 官方收货信息响应
     */
    @PostMapping("/orders/shipping-info/sync")
    @Operation(summary = "同步 Temu 订单收货信息")
    @PreAuthorize("@ss.hasPermission('temu:order-management:update')")
    public TemuApiResponse<ShippingInfoDto> syncOrderShippingInfo(@Valid @RequestBody OrderManagementOrderSyncReqVO request) {
        return orderManagementService.syncOrderShippingInfo(request);
    }

}
