package cn.iocoder.yudao.module.temu.service.auth;

import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAuthInfoReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import tools.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.Map;

/**
 * Temu 认证相关业务 Service 实现。
 */
@Service
@Validated
public class TemuAuthServiceImpl implements TemuAuthService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;

    /**
     * 查询当前 access_token 的权限信息。
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应
     */
    @Override
    public JsonNode getAccessTokenInfo(TemuAuthInfoReqVO request) {
        return createClient(request.getSite(), request.getAccessToken()).getAuth().getAccessTokenInfo();
    }

    /**
     * 查询当前店铺已绑定的本地店铺标签。
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应
     */
    @Override
    public JsonNode getLocalMallTags(TemuAuthInfoReqVO request) {
        return createClient(request.getSite(), request.getAccessToken()).getAuth().getLocalMallTags();
    }

    /**
     * 使用 Temu 授权回调返回的临时授权码创建 access_token。
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方响应
     */
    @Override
    public JsonNode createAccessToken(TemuAccessTokenCreateReqVO request) {
        return createClient(request.getSite(), request.getAccessToken()).getAuth()
                .createAccessTokenInfo(Map.<String, Object>of("code", request.getCode()));
    }

    /**
     * 根据站点和授权 Token 创建 Temu SDK 客户端。
     *
     * <p>应用密钥只从服务端区域配置读取，避免由请求方传入敏感配置。</p>
     *
     * @param siteCode Temu 站点代码
     * @param accessToken Temu 授权 Token
     * @return 已配置的 Temu SDK 客户端
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
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断字符串
     * @return 为空或只包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
