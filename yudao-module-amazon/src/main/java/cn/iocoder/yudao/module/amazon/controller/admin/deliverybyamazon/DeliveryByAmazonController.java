package cn.iocoder.yudao.module.amazon.controller.admin.deliverybyamazon;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.deliverybyamazon.vo.DeliveryByAmazonRequestVO;
import cn.iocoder.yudao.module.amazon.service.deliverybyamazon.DeliveryByAmazonService;
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

/** Amazon Delivery by Amazon 管理接口。 */
@Tag(name = "管理后台 - Amazon Delivery by Amazon")
@RestController
@RequestMapping("/amazon/delivery-by-amazon")
@Validated
public class DeliveryByAmazonController {
    @Resource private DeliveryByAmazonService deliveryByAmazonService;
    /** 提交 Delivery by Amazon 货件发票。 */
    @PostMapping("/invoice") @Operation(summary = "提交 Delivery by Amazon 发票") @PreAuthorize("@ss.hasPermission('amazon:delivery-by-amazon:update')")
    public CommonResult<Map<String, Object>> submitInvoice(@Valid @RequestBody DeliveryByAmazonRequestVO request) { return call(request, "submitInvoice", "POST", "/invoice"); }
    /** 查询 Delivery by Amazon 发票处理状态。 */
    @PostMapping("/invoice/status") @Operation(summary = "查询 Delivery by Amazon 发票状态") @PreAuthorize("@ss.hasPermission('amazon:delivery-by-amazon:query')")
    public CommonResult<Map<String, Object>> getInvoiceStatus(@Valid @RequestBody DeliveryByAmazonRequestVO request) { return call(request, "getInvoiceStatus", "GET", "/invoice/status"); }
    /** 将受校验的请求转交 Service，Controller 不参与 Amazon 调用。 */
    private CommonResult<Map<String, Object>> call(DeliveryByAmazonRequestVO request, String operation, String method, String path) { return CommonResult.success(deliveryByAmazonService.invoke(request, operation, method, path)); }
}
