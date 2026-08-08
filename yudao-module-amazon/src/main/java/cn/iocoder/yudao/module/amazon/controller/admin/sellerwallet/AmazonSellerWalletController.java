package cn.iocoder.yudao.module.amazon.controller.admin.sellerwallet;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.sellerwallet.vo.AmazonSellerWalletReqVO;
import cn.iocoder.yudao.module.amazon.service.sellerwallet.AmazonSellerWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Amazon Seller Wallet 管理接口。 */
@Tag(name = "管理后台 - Amazon Seller Wallet") @RestController @RequestMapping("/amazon/seller-wallet") @Validated
public class AmazonSellerWalletController {
    @Resource private AmazonSellerWalletService amazonSellerWalletService;
    /** 查询 Seller Wallet 账户列表。 */ @PostMapping("/accounts/list") @Operation(summary = "查询 Seller Wallet 账户列表") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> listAccounts(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.listAccounts(request)); }
    /** 查询 Seller Wallet 单个账户。 */ @PostMapping("/accounts/get") @Operation(summary = "查询 Seller Wallet 账户") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> getAccount(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.getAccount(request)); }
    /** 查询 Seller Wallet 账户余额。 */ @PostMapping("/accounts/balances") @Operation(summary = "查询 Seller Wallet 账户余额") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> listAccountBalances(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.listAccountBalances(request)); }
    /** 查询跨币种转账预览。 */ @PostMapping("/transfer-preview") @Operation(summary = "查询 Seller Wallet 转账预览") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> getTransferPreview(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.getTransferPreview(request)); }
    /** 查询 Seller Wallet 交易记录。 */ @PostMapping("/transactions/list") @Operation(summary = "查询 Seller Wallet 交易记录") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> listAccountTransactions(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.listAccountTransactions(request)); }
    /** 创建 Seller Wallet 转账交易。 */ @PostMapping("/transactions/create") @Operation(summary = "创建 Seller Wallet 转账交易") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:update')") public CommonResult<Map<String, Object>> createTransaction(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.createTransaction(request)); }
    /** 查询 Seller Wallet 交易详情。 */ @PostMapping("/transactions/get") @Operation(summary = "查询 Seller Wallet 交易") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> getTransaction(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.getTransaction(request)); }
    /** 查询定时转账配置列表。 */ @PostMapping("/transfer-schedules/list") @Operation(summary = "查询 Seller Wallet 定时转账") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> listTransferSchedules(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.listTransferSchedules(request)); }
    /** 创建定时转账配置。 */ @PostMapping("/transfer-schedules/create") @Operation(summary = "创建 Seller Wallet 定时转账") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:update')") public CommonResult<Map<String, Object>> createTransferSchedule(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.createTransferSchedule(request)); }
    /** 更新定时转账配置。 */ @PostMapping("/transfer-schedules/update") @Operation(summary = "更新 Seller Wallet 定时转账") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:update')") public CommonResult<Map<String, Object>> updateTransferSchedule(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.updateTransferSchedule(request)); }
    /** 查询定时转账配置详情。 */ @PostMapping("/transfer-schedules/get") @Operation(summary = "查询 Seller Wallet 定时转账详情") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:query')") public CommonResult<Map<String, Object>> getTransferSchedule(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.getTransferSchedule(request)); }
    /** 删除定时转账配置。 */ @PostMapping("/transfer-schedules/delete") @Operation(summary = "删除 Seller Wallet 定时转账") @PreAuthorize("@ss.hasPermission('amazon:seller-wallet:update')") public CommonResult<Map<String, Object>> deleteTransferSchedule(@Valid @RequestBody AmazonSellerWalletReqVO request) { return CommonResult.success(amazonSellerWalletService.deleteTransferSchedule(request)); }
}
