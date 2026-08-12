package cn.iocoder.yudao.module.amazon.controller.admin.orders;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrderItemsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.orders.vo.AmazonOrdersListReqVO;
import cn.iocoder.yudao.module.amazon.service.orders.AmazonOrdersService;
import com.amazon.SellingPartnerAPIAA.LWAException;
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
import software.amazon.spapi.ApiException;
import software.amazon.spapi.models.orders.v0.GetOrderItemsResponse;
import software.amazon.spapi.models.orders.v0.GetOrderResponse;
import software.amazon.spapi.models.orders.v0.GetOrdersResponse;

/** Amazon Orders 管理接口。 */
@Tag(name = "管理后台 - Amazon Orders")
@RestController
@RequestMapping("/amazon/orders")
@Validated
public class AmazonOrdersController {
    @Resource private AmazonOrdersService amazonOrdersService;

    /** 查询指定店铺和站点的订单列表。 */
    @PostMapping("/list")
    @Operation(summary = "查询 Amazon 订单列表")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<GetOrdersResponse> getOrders(@Valid @RequestBody AmazonOrdersListReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonOrdersService.getOrders(request));
    }

    /** 查询指定 Amazon 订单详情。 */
    @PostMapping("/detail")
    @Operation(summary = "查询 Amazon 订单详情")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<GetOrderResponse> getOrder(@Valid @RequestBody AmazonOrderGetReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonOrdersService.getOrder(request));
    }

    /** 查询指定 Amazon 订单的商品。 */
    @PostMapping("/items")
    @Operation(summary = "查询 Amazon 订单商品")
    @PreAuthorize("@ss.hasPermission('amazon:orders:query')")
    public CommonResult<GetOrderItemsResponse> getOrderItems(@Valid @RequestBody AmazonOrderItemsReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonOrdersService.getOrderItems(request));
    }
}
