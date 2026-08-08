package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.sandboxtestdata;

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

/** Amazon Vendor Direct Fulfillment Sandbox Test Data 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Direct Fulfillment Sandbox Test Data")
@RestController
@RequestMapping("/amazon/vendor-direct-fulfillment/sandbox-test-data")
@Validated
public class VendorDirectFulfillmentSandboxTestDataController {

    @Resource
    private VendorDirectFulfillmentService vendorDirectFulfillmentService;

    /**
     * 生成沙箱采购订单场景。
     *
     * @param request 店铺、站点和订单场景请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/2021-10-28/orders")
    @Operation(summary = "生成 Direct Fulfillment 沙箱订单场景")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:sandbox:update')")
    public CommonResult<Map<String, Object>> generateOrderScenarios(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "generateOrderScenarios", "POST", "/vendor/directFulfillment/sandbox/2021-10-28/orders");
    }

    /**
     * 查询沙箱订单场景生成事务。
     *
     * @param request 店铺、站点和 transactionId
     * @return Amazon 返回的订单场景数据
     */
    @PostMapping("/2021-10-28/transactions/get")
    @Operation(summary = "查询 Direct Fulfillment 沙箱订单场景")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:sandbox:query')")
    public CommonResult<Map<String, Object>> getOrderScenarios(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "getOrderScenarios", "GET", "/vendor/directFulfillment/sandbox/2021-10-28/transactions/{transactionId}");
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
