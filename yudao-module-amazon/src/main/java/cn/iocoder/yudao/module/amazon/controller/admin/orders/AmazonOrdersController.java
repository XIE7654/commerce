package cn.iocoder.yudao.module.amazon.controller.admin.orders;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
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
    public CommonResult<Map<String, Object>> getOrderItems(@Valid @RequestBody AmazonOrderGetReqVO request) {
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
    public CommonResult<Map<String, Object>> getOrderItemsBuyerInfo(@Valid @RequestBody AmazonOrderGetReqVO request) {
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

}
