package cn.iocoder.yudao.module.amazon.service.auth;

import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonAuthorizeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonCallbackReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonTokenReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonTokenRespVO;

/**
 * Amazon OAuth 授权与店铺 Token 服务。
 */
public interface AmazonOAuthService {

    /**
     * 创建绑定当前租户的授权地址。
     *
     * @param request 授权类型和店铺名称
     * @return Amazon 授权地址
     */
    String buildAuthorizeUrl(AmazonAuthorizeReqVO request);

    /**
     * 处理 Amazon OAuth 回调并保存店铺授权信息。
     *
     * @param request Amazon 回调参数
     * @return 新建或更新的店铺编号
     */
    Long handleCallback(AmazonCallbackReqVO request);

    /**
     * 获取指定店铺的有效 Seller access token，必要时自动刷新。
     *
     * @param shopId 店铺编号
     * @return 有效 access token
     */
    String getSellerAccessToken(Long shopId);

    /**
     * 获取指定店铺的有效 Ads access token，必要时自动刷新。
     *
     * @param shopId 店铺编号
     * @return 有效 Ads access token
     */
    String getAdAccessToken(Long shopId);

    /**
     * 根据国家所属 SP-API 端点，使用授权码换取 Token；仅用于测试，不保存授权信息。
     *
     * @param request 授权码和国家代码
     * @return Amazon 返回的 Token 信息
     */
    AmazonTokenRespVO exchangeAuthorizationCode(AmazonTokenReqVO request);

    /**
     * 根据国家所属 SP-API 端点刷新 access token；仅用于测试，不保存授权信息。
     *
     * @param request refresh token 和国家代码
     * @return Amazon 返回的 Token 信息
     */
    AmazonTokenRespVO refreshAccessToken(AmazonTokenReqVO request);
}
