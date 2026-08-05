package cn.iocoder.yudao.module.amazon.controller.admin.auth;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonAuthorizeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonCallbackReqVO;
import cn.iocoder.yudao.module.amazon.service.auth.AmazonOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Amazon OAuth 管理接口。
 */
@Tag(name = "管理后台 - Amazon 授权")
@RestController
@RequestMapping("/amazon/auth")
@Validated
public class AmazonAuthController {

    @Resource
    private AmazonOAuthService amazonOAuthService;

    /** 创建 Seller 或 Ads 授权地址。 */
    @PostMapping("/authorize-url")
    @Operation(summary = "创建 Amazon OAuth 授权地址")
    @PreAuthorize("@ss.hasPermission('amazon:auth:authorize')")
    public CommonResult<String> authorizeUrl(@Valid @RequestBody AmazonAuthorizeReqVO request) {
        return CommonResult.success(amazonOAuthService.buildAuthorizeUrl(request));
    }

    /** 接收 Amazon OAuth 回调并保存店铺授权。 */
    @GetMapping("/callback")
    @Operation(summary = "处理 Amazon OAuth 回调")
    public CommonResult<Long> callback(@Valid AmazonCallbackReqVO request) {
        return CommonResult.success(amazonOAuthService.handleCallback(request));
    }

    /** 主动获取有效 Seller access token，业务接口可复用该能力。 */
    @GetMapping("/seller-token")
    @Operation(summary = "获取 Amazon Seller access token")
    @PreAuthorize("@ss.hasPermission('amazon:auth:query')")
    public CommonResult<String> sellerToken(@RequestParam Long shopId) {
        return CommonResult.success(amazonOAuthService.getSellerAccessToken(shopId));
    }

    /** 获取有效 Ads access token。 */
    @GetMapping("/ad-token")
    @Operation(summary = "获取 Amazon Ads access token")
    @PreAuthorize("@ss.hasPermission('amazon:auth:query')")
    public CommonResult<String> adToken(@RequestParam Long shopId) {
        return CommonResult.success(amazonOAuthService.getAdAccessToken(shopId));
    }
}
