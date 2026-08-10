package cn.iocoder.yudao.module.temu.service.authorization;

import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenInfoReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;

/**
 * Temu Authorization 授权业务 Service 实现。
 */
@Service
@Validated
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;

    /**
     * 调用 {@code bg.open.accesstoken.info.get} 查询当前 Token 的授权信息。
     *
     * @param request 查询参数，包含站点和 access_token
     * @return Temu 官方授权信息响应
     */
    @Override
    public JsonNode getAccessTokenInfo(AuthorizationAccessTokenInfoReqVO request) {
        return createClient(request.getSite(), request.getAccessToken()).getAuth().getAccessTokenInfo();
    }

    /**
     * 调用 {@code bg.open.accesstoken.create} 以临时授权码创建 Token。
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方 access_token 创建响应
     */
    @Override
    public JsonNode createAccessToken(AuthorizationAccessTokenCreateReqVO request) {
        return createClient(request.getSite(), request.getAccessToken()).getAuth()
                .createAccessTokenInfo(Map.of("code", request.getCode()));
    }

    /**
     * 根据站点配置创建 Temu SDK 客户端。
     *
     * <p>应用密钥只能从服务端区域配置读取，避免管理端请求传入或暴露敏感配置。</p>
     *
     * @param siteCode Temu 站点代码
     * @param accessToken 本次 Router 调用使用的授权 Token
     * @return 已配置区域信息的 Temu SDK 客户端
     */
    private TemuClient createClient(String siteCode, String accessToken) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(siteCode.trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), accessToken, site.getEndpoint(),
                temuJsonStorageService, site.name(), temuApiRequestLogService, null);
    }

    /**
     * 判断配置值是否为空白。
     *
     * @param value 待判断的配置值
     * @return 值为空或仅包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
