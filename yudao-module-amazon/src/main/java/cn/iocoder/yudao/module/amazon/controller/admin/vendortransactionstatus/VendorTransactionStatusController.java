package cn.iocoder.yudao.module.amazon.controller.admin.vendortransactionstatus;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.vendorretailprocurement.vo.VendorRetailProcurementRequestVO;
import cn.iocoder.yudao.module.amazon.service.vendorretailprocurement.VendorRetailProcurementService;
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

/** Amazon Vendor Retail Procurement Transaction Status 管理接口。 */
@Tag(name = "管理后台 - Amazon Vendor Transaction Status")
@RestController
@RequestMapping("/amazon/vendor-transaction-status")
@Validated
public class VendorTransactionStatusController {
    @Resource private VendorRetailProcurementService vendorRetailProcurementService;

    /** 查询指定异步 Vendor 操作的处理状态。@param request 店铺、站点和交易编号 @return Amazon 交易状态 */
    @PostMapping("/get") @Operation(summary = "查询 Amazon Vendor 交易状态") @PreAuthorize("@ss.hasPermission('amazon:vendor-transaction-status:query')")
    public CommonResult<Map<String, Object>> getTransaction(@Valid @RequestBody VendorRetailProcurementRequestVO request) { return CommonResult.success(vendorRetailProcurementService.getTransaction(request)); }
}
