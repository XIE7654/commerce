package cn.iocoder.yudao.module.temu.controller.admin.auth;

import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAuthInfoReqVO;
import cn.iocoder.yudao.module.temu.service.auth.TemuAuthService;
import com.fasterxml.jackson.databind.JsonNode;
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
 * 管理后台 Temu 认证接口。
 */
@Tag(name = "管理后台 - Temu 认证")
@RestController
@RequestMapping("/temu/auth")
@Validated
public class TemuAuthController {

    @Resource
    private TemuAuthService temuAuthService;

    /**
     * 查询当前 access_token 的权限信息。
     *
     * <p>Controller 只接收站点和 Token，具体的区域配置读取和 Temu API 调用由 Service 负责。
     * 返回值直接使用 Temu 官方 JSON 结构，不额外包裹 {@code CommonResult}。</p>
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应，包含 success、errorCode、errorMsg、result
     */
    @PostMapping("/access-token-info")
    @Operation(summary = "查询 Temu access_token 权限")
    @PreAuthorize("@ss.hasPermission('temu:auth:query')")
    public JsonNode getAccessTokenInfo(@Valid @RequestBody TemuAuthInfoReqVO request) {
        return temuAuthService.getAccessTokenInfo(request);
    }

    /**
     * 查询当前店铺已绑定的本地店铺标签。
     *
     * <p>Temu 接口只需要授权 Token，不需要额外业务参数；站点和应用密钥仍由服务端配置决定。</p>
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应，标签列表位于 {@code result.tags}
     */
    @PostMapping("/local-mall-tags")
    @Operation(summary = "查询 Temu 本地店铺标签")
    @PreAuthorize("@ss.hasPermission('temu:auth:query')")
    public JsonNode getLocalMallTags(@Valid @RequestBody TemuAuthInfoReqVO request) {
        return temuAuthService.getLocalMallTags(request);
    }

    /**
     * 使用 Temu 授权回调返回的临时授权码创建 access_token。
     *
     * <p>授权码只能使用一次且有效期为十分钟，接口原始响应直接返回 Temu 的授权结果。</p>
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方响应，包含 accessToken、mallId 和授权范围等信息
     */
    @PostMapping("/access-token/create")
    @Operation(summary = "使用 Temu 授权码创建 access_token")
    @PreAuthorize("@ss.hasPermission('temu:auth:query')")
    public JsonNode createAccessToken(@Valid @RequestBody TemuAccessTokenCreateReqVO request) {
        return temuAuthService.createAccessToken(request);
    }
}
