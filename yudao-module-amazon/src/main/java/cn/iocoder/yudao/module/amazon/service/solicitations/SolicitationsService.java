package cn.iocoder.yudao.module.amazon.service.solicitations;

import cn.iocoder.yudao.module.amazon.controller.admin.solicitations.vo.SolicitationsReqVO;
import java.util.Map;

/** Amazon Solicitations 服务。 */
public interface SolicitationsService {
    /** 查询订单可发起的征集动作。 @param request 店铺、站点和订单参数 @return Amazon 原始响应 */
    Map<String, Object> getSolicitationActionsForOrder(SolicitationsReqVO request);
    /** 发起商品评论和卖家反馈征集。 @param request 店铺、站点和订单参数 @return Amazon 原始响应 */
    Map<String, Object> createProductReviewAndSellerFeedbackSolicitation(SolicitationsReqVO request);
}
