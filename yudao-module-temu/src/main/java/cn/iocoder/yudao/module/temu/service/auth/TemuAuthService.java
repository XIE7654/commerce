package cn.iocoder.yudao.module.temu.service.auth;

import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAccessTokenCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.auth.vo.TemuAuthInfoReqVO;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Temu 认证相关业务 Service。
 */
public interface TemuAuthService {

    /**
     * 查询当前 access_token 的权限信息。
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应
     */
    JsonNode getAccessTokenInfo(TemuAuthInfoReqVO request);

    /**
     * 查询当前店铺已绑定的本地店铺标签。
     *
     * @param request 查询参数，包含站点和授权 Token
     * @return Temu 官方响应
     */
    JsonNode getLocalMallTags(TemuAuthInfoReqVO request);

    /**
     * 使用 Temu 授权回调返回的临时授权码创建 access_token。
     *
     * @param request 创建参数，包含站点、Router access_token 和授权码
     * @return Temu 官方响应
     */
    JsonNode createAccessToken(TemuAccessTokenCreateReqVO request);

}
