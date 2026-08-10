package cn.iocoder.yudao.module.amazon.service.sellers;

import cn.iocoder.yudao.module.amazon.controller.admin.sellers.vo.AmazonSellersReqVO;
import java.util.Map;

/** Amazon Sellers 服务。 */
public interface AmazonSellersService {
    /**
     * 查询卖家可参与销售的站点及参与状态。
     *
     * @param request 店铺参数
     * @return Amazon Sellers 原始 JSON 响应
     */
    Map<String, Object> getMarketplaceParticipations(AmazonSellersReqVO request);
    /**
     * 查询当前卖家的账户信息。
     *
     * @param request 店铺参数
     * @return Amazon Sellers 原始 JSON 响应
     */
    Map<String, Object> getAccount(AmazonSellersReqVO request);
}
