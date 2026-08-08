package cn.iocoder.yudao.module.amazon.service.invoices;

import cn.iocoder.yudao.module.amazon.controller.admin.invoices.vo.InvoicesRequestVO;

import java.util.Map;

/** Amazon Invoices API 服务。 */
public interface InvoicesService {

    /** @param request 店铺、站点和 Marketplace 信息 @return 可用的发票属性 */
    Map<String, Object> getInvoicesAttributes(InvoicesRequestVO request);

    /** @param request 店铺、站点和导出文档编号 @return 发票导出文档下载信息 */
    Map<String, Object> getInvoicesDocument(InvoicesRequestVO request);

    /** @param request 店铺、站点和导出条件 @return 新建的发票导出任务 */
    Map<String, Object> createInvoicesExport(InvoicesRequestVO request);

    /** @param request 店铺、站点和导出筛选条件 @return 发票导出任务列表 */
    Map<String, Object> getInvoicesExports(InvoicesRequestVO request);

    /** @param request 店铺、站点和导出任务编号 @return 指定发票导出任务 */
    Map<String, Object> getInvoicesExport(InvoicesRequestVO request);

    /** @param request 店铺、站点和政府发票创建信息 @return 政府发票创建结果 */
    Map<String, Object> createGovernmentInvoice(InvoicesRequestVO request);

    /** @param request 店铺、站点和政府发票筛选条件 @return 政府发票处理状态 */
    Map<String, Object> getGovernmentInvoiceStatus(InvoicesRequestVO request);

    /** @param request 店铺、站点、货件和政府发票筛选条件 @return 政府发票文档信息 */
    Map<String, Object> getGovernmentInvoiceDocument(InvoicesRequestVO request);

    /** @param request 店铺、站点和发票筛选条件 @return 发票列表 */
    Map<String, Object> getInvoices(InvoicesRequestVO request);

    /** @param request 店铺、站点和发票编号 @return 发票详情 */
    Map<String, Object> getInvoice(InvoicesRequestVO request);
}
