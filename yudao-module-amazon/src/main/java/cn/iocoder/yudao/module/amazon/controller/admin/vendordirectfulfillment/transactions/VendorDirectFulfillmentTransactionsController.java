package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.transactions;

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

/** Amazon Vendor Direct Fulfillment Transactions 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Direct Fulfillment Transactions")
@RestController
@RequestMapping("/amazon/vendor-direct-fulfillment/transactions")
@Validated
public class VendorDirectFulfillmentTransactionsController {

    @Resource
    private VendorDirectFulfillmentService vendorDirectFulfillmentService;

    /**
     * 查询 V1 事务处理状态。
     *
     * @param request 店铺、站点和 transactionId
     * @return Amazon 返回的事务状态
     */
    @PostMapping("/v1/get")
    @Operation(summary = "查询 Direct Fulfillment V1 事务状态")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:transactions:query')")
    public CommonResult<Map<String, Object>> getTransactionStatusV1(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getTransactionStatus", "GET", "/vendor/directFulfillment/transactions/v1/transactions/{transactionId}");
    }

    /**
     * 查询 2021-12-28 版本事务处理状态。
     *
     * @param request 店铺、站点和 transactionId
     * @return Amazon 返回的事务状态
     */
    @PostMapping("/2021-12-28/get")
    @Operation(summary = "查询 Direct Fulfillment 事务状态")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:transactions:query')")
    public CommonResult<Map<String, Object>> getTransactionStatus20211228(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getTransactionStatus20211228", "GET", "/vendor/directFulfillment/transactions/2021-12-28/transactions/{transactionId}");
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
