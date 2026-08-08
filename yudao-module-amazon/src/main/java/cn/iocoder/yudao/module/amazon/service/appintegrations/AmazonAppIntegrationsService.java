package cn.iocoder.yudao.module.amazon.service.appintegrations;

import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonCreateNotificationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonDeleteNotificationsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonRecordActionFeedbackReqVO;

import java.util.Map;

/** Amazon Application Integrations 服务。 */
public interface AmazonAppIntegrationsService {

    /**
     * 向 Seller Central 创建应用通知。
     *
     * @param request 通知模板、动态参数和店铺授权信息
     * @return Amazon 返回的通知编号
     */
    Map<String, Object> createNotification(AmazonCreateNotificationReqVO request);

    /**
     * 从应用通知面板删除指定模板的通知。
     *
     * @param request 模板、删除原因和店铺授权信息
     */
    void deleteNotifications(AmazonDeleteNotificationsReqVO request);

    /**
     * 记录卖家对指定通知的操作结果。
     *
     * @param request 通知编号、反馈代码和店铺授权信息
     */
    void recordActionFeedback(AmazonRecordActionFeedbackReqVO request);
}
