package cn.iocoder.yudao.module.temu.controller.admin.authorization;

import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenInfoReqVO;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenInfoResult;
import cn.iocoder.yudao.module.temu.service.authorization.AuthorizationService;
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

/**
 * 管理后台 Temu Authorization 接口。
 */
@Tag(name = "管理后台 - Temu Authorization")
@RestController
@RequestMapping("/temu/authorization")
@Validated
public class AuthorizationController {

    @Resource
    private AuthorizationService authorizationService;

    /**
     * 查询当前 Temu access_token 的授权信息。
     *
     * @param request 查询参数，包含站点和 access_token
     * @return Temu 官方授权信息响应
     */
    @PostMapping("/access-token/info")
    @Operation(summary = "查询 Temu access_token 授权信息")
    @PreAuthorize("@ss.hasPermission('temu:authorization:query')")
    public AccessTokenInfoResult getAccessTokenInfo(@Valid @RequestBody AuthorizationAccessTokenInfoReqVO request) {
        return authorizationService.getAccessTokenInfo(request);
    }

    /**
     * 使用 Temu 回调的临时授权码创建 access_token。
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方 access_token 创建响应
     */
    @PostMapping("/access-token/create")
    @Operation(summary = "创建 Temu access_token")
    @PreAuthorize("@ss.hasPermission('temu:authorization:create')")
    public AccessTokenCreateResult createAccessToken(@Valid @RequestBody AuthorizationAccessTokenCreateReqVO request) {
        return authorizationService.createAccessToken(request);
    }

}
