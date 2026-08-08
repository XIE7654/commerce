package cn.iocoder.yudao.module.amazon.service.vendorretailprocurement;

import cn.iocoder.yudao.module.amazon.controller.admin.vendorretailprocurement.vo.VendorRetailProcurementRequestVO;

import java.util.Map;

/** Amazon Vendor Retail Procurement API 服务。 */
public interface VendorRetailProcurementService {

    /** 查询采购订单列表。@param request 店铺、站点及 OpenAPI 查询参数 @return Amazon 采购订单列表 */
    Map<String, Object> getPurchaseOrders(VendorRetailProcurementRequestVO request);

    /** 查询单个采购订单。@param request 店铺、站点及采购订单编号 @return Amazon 采购订单详情 */
    Map<String, Object> getPurchaseOrder(VendorRetailProcurementRequestVO request);

    /** 提交采购订单确认。@param request 店铺、站点及确认请求体 @return Amazon 异步交易信息 */
    Map<String, Object> submitAcknowledgement(VendorRetailProcurementRequestVO request);

    /** 查询采购订单状态。@param request 店铺、站点及 OpenAPI 查询参数 @return Amazon 采购订单状态列表 */
    Map<String, Object> getPurchaseOrdersStatus(VendorRetailProcurementRequestVO request);

    /** 提交 Vendor 发票。@param request 店铺、站点及发票请求体 @return Amazon 异步交易信息 */
    Map<String, Object> submitInvoices(VendorRetailProcurementRequestVO request);

    /** 提交货件确认。@param request 店铺、站点及货件确认请求体 @return Amazon 异步交易信息 */
    Map<String, Object> submitShipmentConfirmations(VendorRetailProcurementRequestVO request);

    /** 提交 Vendor 货件。@param request 店铺、站点及货件请求体 @return Amazon 异步交易信息 */
    Map<String, Object> submitShipments(VendorRetailProcurementRequestVO request);

    /** 查询货件详情。@param request 店铺、站点及 OpenAPI 查询参数 @return Amazon 货件列表 */
    Map<String, Object> getShipmentDetails(VendorRetailProcurementRequestVO request);

    /** 查询运输标签。@param request 店铺、站点及 OpenAPI 查询参数 @return Amazon 运输标签列表 */
    Map<String, Object> getShipmentLabels(VendorRetailProcurementRequestVO request);

    /** 查询异步交易状态。@param request 店铺、站点及交易编号 @return Amazon 交易处理状态 */
    Map<String, Object> getTransaction(VendorRetailProcurementRequestVO request);
}
