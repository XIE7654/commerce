package cn.iocoder.yudao.module.amazon.service.auth;

import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonAuthorizeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonCallbackReqVO;

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
}
