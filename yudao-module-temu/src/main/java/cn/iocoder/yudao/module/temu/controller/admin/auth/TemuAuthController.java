package cn.iocoder.yudao.module.temu.controller.admin.auth;

import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAuthInfoReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
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

import java.util.Locale;
import java.util.Map;

/**
 * 管理后台 Temu 认证接口。
 */
@Tag(name = "管理后台 - Temu 认证")
@RestController
@RequestMapping("/temu/auth")
@Validated
public class TemuAuthController {

    @Resource
    private TemuProperties temuProperties;

    /**
     * 查询当前 access_token 的权限信息。
     *
     * <p>Controller 只接收站点和 Token，appKey/appSecret 从服务端区域配置读取，避免前端传递应用密钥。
     * 返回值直接使用 Temu 官方 JSON 结构，不额外包裹 {@code CommonResult}。</p>
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应，包含 success、errorCode、errorMsg、result
     */
    @PostMapping("/access-token-info")
    @Operation(summary = "查询 Temu access_token 权限")
    @PreAuthorize("@ss.hasPermission('temu:auth:query')")
    public JsonNode getAccessTokenInfo(@Valid @RequestBody TemuAuthInfoReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(),
                site.getEndpoint()).getAuth().getAccessTokenInfo();
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
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(),
                site.getEndpoint()).getAuth().getLocalMallTags();
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
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(),
                site.getEndpoint()).getAuth().createAccessTokenInfo(Map.<String, Object>of("code", request.getCode()));
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断字符串
     * @return 为空或只包含空白字符时返回 true
     */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
