package cn.iocoder.yudao.module.amazon.service.externalfulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.externalfulfillment.vo.ExternalFulfillmentRequestVO;

import java.util.Map;

/** Amazon External Fulfillment API 服务。 */
public interface ExternalFulfillmentService {

    /** 批量更新或查询 External Fulfillment 库存。 */
    Map<String, Object> batchInventory(ExternalFulfillmentRequestVO request);
    /** 分页查询 External Fulfillment 退货。 */
    Map<String, Object> listReturns(ExternalFulfillmentRequestVO request);
    /** 查询指定 External Fulfillment 退货详情。 */
    Map<String, Object> getReturn(ExternalFulfillmentRequestVO request);
    /** 按状态查询 External Fulfillment 货件。 */
    Map<String, Object> getShipments(ExternalFulfillmentRequestVO request);
    /** 查询指定 External Fulfillment 货件详情。 */
    Map<String, Object> getShipment(ExternalFulfillmentRequestVO request);
    /** 确认或拒绝 External Fulfillment 货件。 */
    Map<String, Object> processShipment(ExternalFulfillmentRequestVO request);
    /** 为货件创建包裹。 */
    Map<String, Object> createPackages(ExternalFulfillmentRequestVO request);
    /** 更新货件包裹信息。 */
    Map<String, Object> updatePackage(ExternalFulfillmentRequestVO request);
    /** 更新货件包裹状态。 */
    Map<String, Object> updatePackageStatus(ExternalFulfillmentRequestVO request);
    /** 查询包裹可用配送选项。 */
    Map<String, Object> retrieveShippingOptions(ExternalFulfillmentRequestVO request);
    /** 为货件生成发票。 */
    Map<String, Object> generateInvoice(ExternalFulfillmentRequestVO request);
    /** 获取货件发票。 */
    Map<String, Object> retrieveInvoice(ExternalFulfillmentRequestVO request);
    /** 生成或重新生成货件面单。 */
    Map<String, Object> generateShipLabels(ExternalFulfillmentRequestVO request);
}
