package cn.iocoder.yudao.module.amazon.service.replenishment;

import cn.iocoder.yudao.module.amazon.controller.admin.replenishment.vo.ReplenishmentReqVO;
import java.util.Map;

/** Amazon Replenishment 服务。 */
public interface ReplenishmentService {
    /** 查询销售伙伴补货指标。 @param request 店铺、站点和官方请求体 @return Amazon 原始响应 */
    Map<String, Object> getSellingPartnerMetrics(ReplenishmentReqVO request);
    /** 查询补货报价指标。 @param request 店铺、站点和官方请求体 @return Amazon 原始响应 */
    Map<String, Object> listOfferMetrics(ReplenishmentReqVO request);
    /** 查询补货报价。 @param request 店铺、站点和官方请求体 @return Amazon 原始响应 */
    Map<String, Object> listOffers(ReplenishmentReqVO request);
}
