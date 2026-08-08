package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.payments;

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

/** Amazon Vendor Direct Fulfillment Payments 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Direct Fulfillment Payments")
@RestController
@RequestMapping("/amazon/vendor-direct-fulfillment/payments")
@Validated
public class VendorDirectFulfillmentPaymentsController {

    @Resource
    private VendorDirectFulfillmentService vendorDirectFulfillmentService;

    /**
     * 提交 Direct Fulfillment 发票。
     *
     * @param request 店铺、站点和发票请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/v1/invoices")
    @Operation(summary = "提交 Direct Fulfillment 发票")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:payments:update')")
    public CommonResult<Map<String, Object>> submitInvoice(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "submitInvoice", "POST", "/vendor/directFulfillment/payments/v1/invoices");
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
