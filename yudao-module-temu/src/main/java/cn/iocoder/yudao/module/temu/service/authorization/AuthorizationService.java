package cn.iocoder.yudao.module.temu.service.authorization;

import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.authorization.vo.AuthorizationAccessTokenInfoReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Temu Authorization 授权业务 Service。
 */
public interface AuthorizationService {

    /**
     * 查询当前 access_token 的授权信息。
     *
     * @param request 查询参数，包含站点和 access_token
     * @return Temu 官方授权信息响应
     */
    JsonNode getAccessTokenInfo(AuthorizationAccessTokenInfoReqVO request);

    /**
     * 使用临时授权码创建 access_token。
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方 access_token 创建响应
     */
    JsonNode createAccessToken(AuthorizationAccessTokenCreateReqVO request);

}
