package cn.iocoder.yudao.module.amazon.service.tokens;

import cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo.AmazonRestrictedDataTokenCreateReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.tokens.vo.AmazonRestrictedDataTokenRespVO;

/** Amazon Tokens API 服务。 */
public interface AmazonTokensService {

    /**
     * 为指定受限资源创建短期 Restricted Data Token。
     *
     * @param request 店铺、站点和受限资源范围
     * @return 可用于请求已授权受限资源的 RDT
     */
    AmazonRestrictedDataTokenRespVO createRestrictedDataToken(AmazonRestrictedDataTokenCreateReqVO request);

}
