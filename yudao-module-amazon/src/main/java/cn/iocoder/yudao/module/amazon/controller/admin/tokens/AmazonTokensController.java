package cn.iocoder.yudao.module.amazon.controller.admin.tokens;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo.AmazonRestrictedDataTokenCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo.AmazonRestrictedDataTokenRespVO;
import cn.iocoder.yudao.module.amazon.service.tokens.AmazonTokensService;
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

/** Amazon Tokens 管理接口。 */
@Tag(name = "管理后台 - Amazon Tokens")
@RestController
@RequestMapping("/amazon/tokens")
@Validated
public class AmazonTokensController {

    @Resource
    private AmazonTokensService amazonTokensService;

    /**
     * 创建限定受限资源范围的 Restricted Data Token。
     *
     * @param request 店铺、站点和受限资源范围
     * @return 短期 Restricted Data Token
     */
    @PostMapping("/restricted-data-token")
    @Operation(summary = "创建 Amazon Restricted Data Token")
    @PreAuthorize("@ss.hasPermission('amazon:tokens:create')")
    public CommonResult<AmazonRestrictedDataTokenRespVO> createRestrictedDataToken(
            @Valid @RequestBody AmazonRestrictedDataTokenCreateReqVO request) {
        return CommonResult.success(amazonTokensService.createRestrictedDataToken(request));
    }

}
