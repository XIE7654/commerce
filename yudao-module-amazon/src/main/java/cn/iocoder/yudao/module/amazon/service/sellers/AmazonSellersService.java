package cn.iocoder.yudao.module.amazon.service.sellers;

import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import software.amazon.spapi.ApiException;
import com.amazon.SellingPartnerAPIAA.LWAException;
import software.amazon.spapi.models.sellers.v1.GetAccountResponse;
import software.amazon.spapi.models.sellers.v1.GetMarketplaceParticipationsResponse;

/** Amazon Sellers 服务。 */
public interface AmazonSellersService {
    /**
     * 查询卖家可参与销售的站点及参与状态。
     *
     * @param request 店铺参数
     * @return 包含强类型 Marketplace 数据的统一响应
     */
    GetMarketplaceParticipationsResponse getMarketplaceParticipations(AmazonSellersReqVO request) throws ApiException, LWAException;

    /**
     * 查询当前卖家的账户信息。
     *
     * @param request 店铺参数
     * @return 包含强类型账户数据的统一响应
     */
    GetAccountResponse getAccount(AmazonSellersReqVO request) throws ApiException, LWAException;
}
