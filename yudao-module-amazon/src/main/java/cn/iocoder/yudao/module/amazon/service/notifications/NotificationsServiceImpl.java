package cn.iocoder.yudao.module.amazon.service.notifications;

import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiServiceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Amazon Notifications v1 API 服务实现。 */
@Service
public class NotificationsServiceImpl extends AmazonSpApiServiceSupport implements NotificationsService {

    private static final Map<String, OperationDefinition> OPERATIONS = Map.ofEntries(
            entry("getSubscriptions", HttpMethod.GET, "/notifications/v1/subscriptions"),
            entry("getSubscription", HttpMethod.GET, "/notifications/v1/subscriptions/{notificationType}"),
            entry("createSubscription", HttpMethod.POST, "/notifications/v1/subscriptions/{notificationType}"),
            entry("getSubscriptionById", HttpMethod.GET, "/notifications/v1/subscriptions/{notificationType}/{subscriptionId}"),
            entry("deleteSubscriptionById", HttpMethod.DELETE, "/notifications/v1/subscriptions/{notificationType}/{subscriptionId}"),
            entry("sendTestNotification", HttpMethod.POST, "/notifications/v1/subscriptions/{notificationType}/testNotification"),
            entry("getDestinations", HttpMethod.GET, "/notifications/v1/destinations"),
            entry("createDestination", HttpMethod.POST, "/notifications/v1/destinations"),
            entry("getDestination", HttpMethod.GET, "/notifications/v1/destinations/{destinationId}"),
            entry("deleteDestination", HttpMethod.DELETE, "/notifications/v1/destinations/{destinationId}")
    );

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(String operation, AmazonSpApiReqVO request) {
        OperationDefinition definition = OPERATIONS.get(operation);
        if (definition == null) {
            throw new IllegalArgumentException("不支持的 Notifications operation: " + operation);
        }
        return invoke(request, operation, definition, AmazonApiCategory.NOTIFICATIONS, "notifications-" + operation);
    }

    /**
     * 创建不可变的 operation 路由定义。
     *
     * @param operation Amazon operationId
     * @param method 对应 HTTP 方法
     * @param path 模型定义的资源路径
     * @return operation 到路由定义的映射项
     */
    private static Map.Entry<String, OperationDefinition> entry(String operation, HttpMethod method, String path) {
        return Map.entry(operation, new OperationDefinition(method, path));
    }
}
