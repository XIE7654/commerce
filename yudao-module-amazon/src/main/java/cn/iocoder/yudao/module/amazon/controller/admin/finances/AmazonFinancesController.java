package cn.iocoder.yudao.module.amazon.controller.admin.finances;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.finances.vo.AmazonFinancesReqVO;
import cn.iocoder.yudao.module.amazon.service.finances.AmazonFinancesService;
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

/** Amazon Finances 管理接口。 */
@Tag(name = "管理后台 - Amazon Finances")
@RestController
@RequestMapping("/amazon/finances")
@Validated
public class AmazonFinancesController {

    @Resource
    private AmazonFinancesService amazonFinancesService;

    /**
     * 查询 2024 版财务交易。
     *
     * @param request 店铺、站点和交易筛选参数
     * @return Amazon 返回的财务交易数据
     */
    @PostMapping("/transactions/list") @Operation(summary = "查询 Amazon 财务交易") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listTransactions(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listTransactions(request)); }
    /**
     * 查询 2024 版账户余额。
     *
     * @param request 店铺、站点和余额筛选参数
     * @return Amazon 返回的余额数据
     */
    @PostMapping("/balances/list") @Operation(summary = "查询 Amazon 账户余额") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listBalances(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listBalances(request)); }
    /**
     * 查询 2024 版财务汇总。
     *
     * @param request 店铺、站点和汇总筛选参数
     * @return Amazon 返回的财务汇总数据
     */
    @PostMapping("/summary/list") @Operation(summary = "查询 Amazon 财务汇总") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listSummary(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listSummary(request)); }
    /**
     * 查询旧版财务事件组。
     *
     * @param request 店铺、站点和事件组筛选参数
     * @return Amazon 返回的财务事件组数据
     */
    @PostMapping("/financial-event-groups/list") @Operation(summary = "查询 Amazon 财务事件组") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listFinancialEventGroups(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listFinancialEventGroups(request)); }
    /**
     * 查询指定财务事件组的事件。
     *
     * @param request 店铺、站点和事件组编号
     * @return Amazon 返回的财务事件数据
     */
    @PostMapping("/financial-events/by-group") @Operation(summary = "按事件组查询 Amazon 财务事件") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listFinancialEventsByGroupId(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listFinancialEventsByGroupId(request)); }
    /**
     * 查询指定订单的财务事件。
     *
     * @param request 店铺、站点和订单编号
     * @return Amazon 返回的财务事件数据
     */
    @PostMapping("/financial-events/by-order") @Operation(summary = "按订单查询 Amazon 财务事件") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listFinancialEventsByOrderId(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listFinancialEventsByOrderId(request)); }
    /**
     * 查询旧版财务事件。
     *
     * @param request 店铺、站点和事件筛选参数
     * @return Amazon 返回的财务事件数据
     */
    @PostMapping("/financial-events/list") @Operation(summary = "查询 Amazon 财务事件") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listFinancialEvents(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listFinancialEvents(request)); }
    /**
     * 发起卖家按需付款。
     *
     * @param request 店铺、站点和付款请求体
     * @return Amazon 返回的付款引用编号
     */
    @PostMapping("/payouts/initiate") @Operation(summary = "发起 Amazon 按需付款") @PreAuthorize("@ss.hasPermission('amazon:finances:update')")
    public CommonResult<Map<String, Object>> initiatePayout(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.initiatePayout(request)); }
    /**
     * 查询付款记录。
     *
     * @param request 店铺、站点和付款筛选参数
     * @return Amazon 返回的付款记录
     */
    @PostMapping("/payouts/list") @Operation(summary = "查询 Amazon 付款记录") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listPayouts(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listPayouts(request)); }
    /**
     * 查询付款方式。
     *
     * @param request 店铺、站点和付款方式筛选参数
     * @return Amazon 返回的付款方式数据
     */
    @PostMapping("/payment-methods/list") @Operation(summary = "查询 Amazon 付款方式") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> getPaymentMethods(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.getPaymentMethods(request)); }
    /**
     * 查询预计付款。
     *
     * @param request 店铺、站点和预计付款筛选参数
     * @return Amazon 返回的预计付款数据
     */
    @PostMapping("/payouts/expected") @Operation(summary = "查询 Amazon 预计付款") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> listExpectedPayouts(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.listExpectedPayouts(request)); }
    /**
     * 查询发票头。
     *
     * @param request 店铺、站点和发票筛选参数
     * @return Amazon 返回的发票头数据
     */
    @PostMapping("/invoices/list") @Operation(summary = "查询 Amazon 发票头") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> getInvoiceHeaders(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.getInvoiceHeaders(request)); }
    /**
     * 查询单张发票及其行项目。
     *
     * @param request 店铺、站点、Marketplace 和发票编号
     * @return Amazon 返回的发票明细数据
     */
    @PostMapping("/invoices/get") @Operation(summary = "查询 Amazon 发票详情") @PreAuthorize("@ss.hasPermission('amazon:finances:query')")
    public CommonResult<Map<String, Object>> getInvoice(@Valid @RequestBody AmazonFinancesReqVO request) { return CommonResult.success(amazonFinancesService.getInvoice(request)); }

}
