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

import com.amazon.SellingPartnerAPIAA.LWAException;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.models.sellers.v1.GetAccountResponse;
import software.amazon.spapi.models.sellers.v1.GetMarketplaceParticipationsResponse;

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
    public CommonResult<GetMarketplaceParticipationsResponse> getMarketplaceParticipations(@Valid @RequestBody AmazonSellersReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonSellersService.getMarketplaceParticipations(request));
    }

    /**
     * 查询 Amazon 卖家账户信息。
     */
    @PostMapping("/account")
    @Operation(summary = "查询 Amazon 卖家账户")
    @PreAuthorize("@ss.hasPermission('amazon:sellers:query')")
    public CommonResult<GetAccountResponse> getAccount(@Valid @RequestBody AmazonSellersReqVO request) throws ApiException, LWAException {
        return CommonResult.success(amazonSellersService.getAccount(request));
    }

}
