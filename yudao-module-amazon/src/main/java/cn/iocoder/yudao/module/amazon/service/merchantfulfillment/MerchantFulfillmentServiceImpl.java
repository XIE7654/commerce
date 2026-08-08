package cn.iocoder.yudao.module.amazon.service.merchantfulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiCategory;
import cn.iocoder.yudao.module.amazon.service.spapi.AmazonSpApiServiceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Amazon Merchant Fulfillment v0 API 服务实现。 */
@Service
public class MerchantFulfillmentServiceImpl extends AmazonSpApiServiceSupport implements MerchantFulfillmentService {

    private static final Map<String, OperationDefinition> OPERATIONS = Map.ofEntries(
            entry("getEligibleShipmentServices", HttpMethod.POST, "/mfn/v0/eligibleShippingServices"),
            entry("getShipment", HttpMethod.GET, "/mfn/v0/shipments/{shipmentId}"),
            entry("cancelShipment", HttpMethod.DELETE, "/mfn/v0/shipments/{shipmentId}"),
            entry("createShipment", HttpMethod.POST, "/mfn/v0/shipments"),
            entry("getAdditionalSellerInputs", HttpMethod.POST, "/mfn/v0/additionalSellerInputs")
    );

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> invoke(String operation, AmazonSpApiReqVO request) {
        OperationDefinition definition = OPERATIONS.get(operation);
        if (definition == null) {
            throw new IllegalArgumentException("不支持的 Merchant Fulfillment operation: " + operation);
        }
        return invoke(request, operation, definition, AmazonApiCategory.MERCHANT_FULFILLMENT,
                "merchant-fulfillment-" + operation);
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
