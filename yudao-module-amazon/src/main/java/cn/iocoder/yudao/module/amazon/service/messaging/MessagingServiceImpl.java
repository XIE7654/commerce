package cn.iocoder.yudao.module.amazon.service.messaging;

import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiServiceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Amazon Messaging v1 API 服务实现。 */
@Service
public class MessagingServiceImpl extends AmazonSpApiServiceSupport implements MessagingService {

    private static final Map<String, OperationDefinition> OPERATIONS = Map.ofEntries(
            entry("getMessagingActionsForOrder", HttpMethod.GET, "/messaging/v1/orders/{amazonOrderId}"),
            entry("confirmCustomizationDetails", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/confirmCustomizationDetails"),
            entry("createConfirmDeliveryDetails", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/confirmDeliveryDetails"),
            entry("createLegalDisclosure", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/legalDisclosure"),
            entry("createConfirmOrderDetails", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/confirmOrderDetails"),
            entry("createConfirmServiceDetails", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/confirmServiceDetails"),
            entry("CreateWarranty", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/warranty"),
            entry("GetAttributes", HttpMethod.GET, "/messaging/v1/orders/{amazonOrderId}/attributes"),
            entry("createDigitalAccessKey", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/digitalAccessKey"),
            entry("createUnexpectedProblem", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/unexpectedProblem"),
            entry("sendInvoice", HttpMethod.POST, "/messaging/v1/orders/{amazonOrderId}/messages/invoice")
    );

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(String operation, AmazonSpApiReqVO request) {
        OperationDefinition definition = OPERATIONS.get(operation);
        if (definition == null) {
            throw new IllegalArgumentException("不支持的 Messaging operation: " + operation);
        }
        return invoke(request, operation, definition, AmazonApiCategory.MESSAGING, "messaging-" + operation);
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
