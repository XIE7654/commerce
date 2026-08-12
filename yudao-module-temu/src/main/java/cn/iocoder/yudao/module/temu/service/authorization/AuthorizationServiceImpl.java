package cn.iocoder.yudao.module.temu.service.authorization;

import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenInfoReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateRequest;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenCreateResult;
import cn.iocoder.yudao.module.temu.framework.client.auth.AccessTokenInfoResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;

/**
 * Temu Authorization 授权业务 Service 实现。
 */
@Service
@Validated
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource
    private TemuProperties temuProperties;

    /**
     * 调用 {@code bg.open.accesstoken.info.get} 查询当前 Token 的授权信息。
     *
     * @param request 查询参数，包含站点和 access_token
     * @return Temu 官方授权信息响应
     */
    @Override
    public AccessTokenInfoResult getAccessTokenInfo(AuthorizationAccessTokenInfoReqVO request) {
        return createClient(request.getSite(), request.getAccessToken()).getAuth().getAccessTokenInfo().getResult();
    }

    /**
     * 调用 {@code bg.open.accesstoken.create} 以临时授权码创建 Token。
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方 access_token 创建响应
     */
    @Override
    public AccessTokenCreateResult createAccessToken(AuthorizationAccessTokenCreateReqVO request) {
        AccessTokenCreateRequest createRequest = new AccessTokenCreateRequest();
        createRequest.setCode(request.getCode());
        return createClient(request.getSite(), request.getAccessToken()).getAuth()
                .createAccessToken(createRequest).getResult();
    }

    /**
     * 根据站点配置创建 Temu SDK 客户端。
     *
     * <p>应用密钥只能从服务端区域配置读取，避免管理端请求传入或暴露敏感配置。</p>
     *
     * @param siteCode Temu 站点代码
     * @param accessToken 本次 Router 调用使用的授权 Token
     * @return 已配置区域信息的新版 Temu 客户端
     */
    private TemuClient createClient(String siteCode, String accessToken) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(siteCode.trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), accessToken, site.name());
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
