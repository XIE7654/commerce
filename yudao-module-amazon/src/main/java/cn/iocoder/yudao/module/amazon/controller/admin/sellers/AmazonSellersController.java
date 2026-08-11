package cn.iocoder.yudao.module.amazon.controller.admin.sellers;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import cn.iocoder.yudao.module.amazon.service.sellers.AmazonSellersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.AccountDto;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.MarketplaceParticipationDto;
import java.util.List;

/**
 * Amazon Sellers 管理接口。
 */
@Tag(name = "管理后台 - Amazon Sellers")
@RestController
@RequestMapping("/amazon/sellers")
@Validated
public class AmazonSellersController {
    @Resource
    private AmazonSellersService amazonSellersService;

    /**
     * 查询卖家在各 Amazon 站点的参与状态。
     */
    @PostMapping("/marketplace-participations")
    @Operation(summary = "查询卖家站点参与状态")
    @PreAuthorize("@ss.hasPermission('amazon:sellers:query')")
    public CommonResult<AmazonApiResponse<List<MarketplaceParticipationDto>>> getMarketplaceParticipations(@Valid @RequestBody AmazonSellersReqVO request) {
        return CommonResult.success(amazonSellersService.getMarketplaceParticipations(request));
    }

    /**
     * 查询 Amazon 卖家账户信息。
     */
    @PostMapping("/account")
    @Operation(summary = "查询 Amazon 卖家账户")
    @PreAuthorize("@ss.hasPermission('amazon:sellers:query')")
    public CommonResult<AmazonApiResponse<AccountDto>> getAccount(@Valid @RequestBody AmazonSellersReqVO request) {
        return CommonResult.success(amazonSellersService.getAccount(request));
    }

    /**
     * 同步 Amazon 卖家账户及店铺 Marketplace 参与状态。
     */
    @PostMapping("/account/sync")
    @Operation(summary = "同步 Amazon 卖家账户及站点参与状态")
    @PreAuthorize("@ss.hasPermission('amazon:sellers:update')")
    public CommonResult<AmazonApiResponse<AccountDto>> syncAccount(@Valid @RequestBody AmazonSellersReqVO request) {
        return CommonResult.success(amazonSellersService.syncAccount(request));
    }
}
