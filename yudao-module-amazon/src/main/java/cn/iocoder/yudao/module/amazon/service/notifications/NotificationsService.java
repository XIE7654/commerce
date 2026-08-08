package cn.iocoder.yudao.module.amazon.service.notifications;

import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;

import java.util.Map;

/** Amazon Notifications API 服务。 */
public interface NotificationsService {

    /**
     * 调用 Notifications v1 白名单 operation。
     *
     * @param operation Amazon 模型中定义的 operationId
     * @param request 店铺、路径参数、查询参数及请求体
     * @return Amazon 原始 JSON 响应；204 时为空 Map
     */
    Map<String, Object> invoke(String operation, AmazonSpApiReqVO request);
}
