package cn.iocoder.yudao.module.amazon.service.customerfeedback;

import cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo.CustomerFeedbackBrowseNodeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo.CustomerFeedbackItemReqVO;

import java.util.Map;

/** Customer Feedback API 服务。 */
public interface CustomerFeedbackService {
    /**
     * 查询商品评论主题。
     * @param request 店铺、站点、ASIN 与排序条件
     * @return Amazon 原始响应
     */ Map<String, Object> getItemReviewTopics(CustomerFeedbackItemReqVO request);
    /**
     * 查询商品所属浏览节点。
     * @param request 店铺、站点与 ASIN
     * @return Amazon 原始响应
     */ Map<String, Object> getItemBrowseNode(CustomerFeedbackItemReqVO request);
    /**
     * 查询浏览节点评论主题。
     * @param request 店铺、站点、浏览节点与排序条件
     * @return Amazon 原始响应
     */ Map<String, Object> getBrowseNodeReviewTopics(CustomerFeedbackBrowseNodeReqVO request);
    /**
     * 查询商品评论趋势。
     * @param request 店铺、站点与 ASIN
     * @return Amazon 原始响应
     */ Map<String, Object> getItemReviewTrends(CustomerFeedbackItemReqVO request);
    /**
     * 查询浏览节点评论趋势。
     * @param request 店铺、站点与浏览节点
     * @return Amazon 原始响应
     */ Map<String, Object> getBrowseNodeReviewTrends(CustomerFeedbackBrowseNodeReqVO request);
    /**
     * 查询浏览节点退货主题。
     * @param request 店铺、站点与浏览节点
     * @return Amazon 原始响应
     */ Map<String, Object> getBrowseNodeReturnTopics(CustomerFeedbackBrowseNodeReqVO request);
    /**
     * 查询浏览节点退货趋势。
     * @param request 店铺、站点与浏览节点
     * @return Amazon 原始响应
     */ Map<String, Object> getBrowseNodeReturnTrends(CustomerFeedbackBrowseNodeReqVO request);
}
