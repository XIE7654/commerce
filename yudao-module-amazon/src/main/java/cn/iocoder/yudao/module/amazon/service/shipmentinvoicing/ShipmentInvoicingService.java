package cn.iocoder.yudao.module.amazon.service.shipmentinvoicing;

import cn.iocoder.yudao.module.amazon.controller.admin.shipmentinvoicing.vo.ShipmentInvoicingRequestVO;

import java.util.Map;

/** Amazon Shipment Invoicing API 服务。 */
public interface ShipmentInvoicingService {

    /** @param request 店铺、站点和 FBA 出库货件编号 @return 开具发票所需的货件信息 */
    Map<String, Object> getShipmentDetails(ShipmentInvoicingRequestVO request);

    /** @param request 店铺、站点、货件编号和发票内容 @return 发票提交结果 */
    Map<String, Object> submitInvoice(ShipmentInvoicingRequestVO request);

    /** @param request 店铺、站点和 FBA 出库货件编号 @return 发票处理状态 */
    Map<String, Object> getInvoiceStatus(ShipmentInvoicingRequestVO request);
}
