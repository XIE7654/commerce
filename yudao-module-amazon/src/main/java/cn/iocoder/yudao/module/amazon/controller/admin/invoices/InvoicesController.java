package cn.iocoder.yudao.module.amazon.controller.admin.invoices;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.invoices.vo.InvoicesRequestVO;
import cn.iocoder.yudao.module.amazon.service.invoices.InvoicesService;
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

/** Amazon Invoices 管理接口。 */
@Tag(name = "管理后台 - Amazon Invoices")
@RestController
@RequestMapping("/amazon/invoices")
@Validated
public class InvoicesController {

    @Resource
    private InvoicesService invoicesService;

    /**
     * 查询站点支持的发票属性。
     *
     * @param request 店铺、站点和 Marketplace 信息
     * @return Amazon 返回的发票属性选项
     */
    @PostMapping("/attributes")
    @Operation(summary = "查询 Amazon 发票属性")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getInvoicesAttributes(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getInvoicesAttributes(request));
    }

    /**
     * 查询发票导出文档的下载信息。
     *
     * @param request 店铺、站点和导出文档编号
     * @return Amazon 返回的文档下载地址和信息
     */
    @PostMapping("/documents/get")
    @Operation(summary = "查询 Amazon 发票导出文档")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getInvoicesDocument(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getInvoicesDocument(request));
    }

    /**
     * 创建异步发票导出任务。
     *
     * @param request 店铺、站点和导出筛选条件
     * @return Amazon 返回的导出任务信息
     */
    @PostMapping("/exports/create")
    @Operation(summary = "创建 Amazon 发票导出")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:create')")
    public CommonResult<Map<String, Object>> createInvoicesExport(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.createInvoicesExport(request));
    }

    /**
     * 查询发票导出任务列表。
     *
     * @param request 店铺、站点和导出筛选条件
     * @return Amazon 返回的导出任务列表
     */
    @PostMapping("/exports/list")
    @Operation(summary = "查询 Amazon 发票导出列表")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getInvoicesExports(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getInvoicesExports(request));
    }

    /**
     * 查询指定发票导出任务。
     *
     * @param request 店铺、站点和导出任务编号
     * @return Amazon 返回的导出任务详情
     */
    @PostMapping("/exports/get")
    @Operation(summary = "查询 Amazon 发票导出")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getInvoicesExport(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getInvoicesExport(request));
    }

    /**
     * 创建政府发票。
     *
     * @param request 店铺、站点和政府发票创建信息
     * @return Amazon 返回的政府发票创建结果
     */
    @PostMapping("/government-invoices/create")
    @Operation(summary = "创建 Amazon 政府发票")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:create')")
    public CommonResult<Map<String, Object>> createGovernmentInvoice(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.createGovernmentInvoice(request));
    }

    /**
     * 查询政府发票的处理状态。
     *
     * @param request 店铺、站点和政府发票筛选条件
     * @return Amazon 返回的政府发票状态
     */
    @PostMapping("/government-invoices/status")
    @Operation(summary = "查询 Amazon 政府发票状态")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getGovernmentInvoiceStatus(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getGovernmentInvoiceStatus(request));
    }

    /**
     * 查询政府发票文档。
     *
     * @param request 店铺、站点、货件和政府发票筛选条件
     * @return Amazon 返回的政府发票文档信息
     */
    @PostMapping("/government-invoices/document")
    @Operation(summary = "查询 Amazon 政府发票文档")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getGovernmentInvoiceDocument(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getGovernmentInvoiceDocument(request));
    }

    /**
     * 查询发票列表。
     *
     * @param request 店铺、站点和发票筛选条件
     * @return Amazon 返回的发票列表
     */
    @PostMapping("/list")
    @Operation(summary = "查询 Amazon 发票列表")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getInvoices(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getInvoices(request));
    }

    /**
     * 查询单张发票。
     *
     * @param request 店铺、站点和发票编号
     * @return Amazon 返回的发票详情
     */
    @PostMapping("/get")
    @Operation(summary = "查询 Amazon 发票详情")
    @PreAuthorize("@ss.hasPermission('amazon:invoices:query')")
    public CommonResult<Map<String, Object>> getInvoice(@Valid @RequestBody InvoicesRequestVO request) {
        return CommonResult.success(invoicesService.getInvoice(request));
    }
}
