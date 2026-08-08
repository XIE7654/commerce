package cn.iocoder.yudao.module.amazon.controller.admin.vendordirectfulfillment.inventory;

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

/** Amazon Vendor Direct Fulfillment Inventory 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Direct Fulfillment Inventory")
@RestController
@RequestMapping("/amazon/vendor-direct-fulfillment/inventory")
@Validated
public class VendorDirectFulfillmentInventoryController {

    @Resource
    private VendorDirectFulfillmentService vendorDirectFulfillmentService;

    /**
     * 提交供应商仓库库存更新。
     *
     * @param request 店铺、站点、warehouseId 和库存更新请求体
     * @return Amazon 返回的事务编号
     */
    @PostMapping("/v1/updates")
    @Operation(summary = "提交 Direct Fulfillment 库存更新")
    @PreAuthorize("@ss.hasPermission('amazon:vendor-direct-fulfillment:inventory:update')")
    public CommonResult<Map<String, Object>> submitInventoryUpdate(@Valid @RequestBody VendorDirectFulfillmentRequestVO request) {
        return call(request, "submitInventoryUpdate", "POST", "/vendor/directFulfillment/inventory/v1/warehouses/{warehouseId}/items");
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
