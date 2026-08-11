package cn.iocoder.yudao.module.amazon.service.sellers;

import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.AccountDto;
import cn.iocoder.yudao.module.amazon.sdk.sellers.dto.MarketplaceParticipationDto;
import java.util.List;

/** Amazon Sellers 服务。 */
public interface AmazonSellersService {
    /**
     * 查询卖家可参与销售的站点及参与状态。
     *
     * @param request 店铺参数
     * @return 包含强类型 Marketplace 数据的统一响应
     */
    AmazonApiResponse<List<MarketplaceParticipationDto>> getMarketplaceParticipations(AmazonSellersReqVO request);

    /**
     * 同步卖家可参与销售的站点及参与状态到店铺站点表。
     *
     * @param request 店铺参数
     * @return Amazon 返回的站点参与状态
     */
    AmazonApiResponse<List<MarketplaceParticipationDto>> syncMarketplaceParticipations(AmazonSellersReqVO request);

    /**
     * 查询当前卖家的账户信息。
     *
     * @param request 店铺参数
     * @return 包含强类型账户数据的统一响应
     */
    AmazonApiResponse<AccountDto> getAccount(AmazonSellersReqVO request);

    /**
     * 同步指定店铺的卖家账户档案及 Marketplace 参与状态。
     *
     * @param request 店铺参数
     * @return 已同步的强类型账户数据
     */
    AmazonApiResponse<AccountDto> syncAccount(AmazonSellersReqVO request);
}
