package cn.iocoder.yudao.module.amazon.service.finances;

import cn.iocoder.yudao.module.amazon.controller.admin.finances.vo.AmazonFinancesReqVO;

import java.util.Map;

/** Amazon Finances 服务。 */
public interface AmazonFinancesService {

    /** @param request 财务交易查询参数 @return 财务交易列表 */
    Map<String, Object> listTransactions(AmazonFinancesReqVO request);
    /** @param request 余额查询参数 @return 余额列表 */
    Map<String, Object> listBalances(AmazonFinancesReqVO request);
    /** @param request 财务汇总查询参数 @return 财务汇总列表 */
    Map<String, Object> listSummary(AmazonFinancesReqVO request);
    /** @param request 财务事件组查询参数 @return 财务事件组列表 */
    Map<String, Object> listFinancialEventGroups(AmazonFinancesReqVO request);
    /** @param request 财务事件组及分页参数 @return 指定组的财务事件 */
    Map<String, Object> listFinancialEventsByGroupId(AmazonFinancesReqVO request);
    /** @param request 订单及分页参数 @return 指定订单的财务事件 */
    Map<String, Object> listFinancialEventsByOrderId(AmazonFinancesReqVO request);
    /** @param request 财务事件筛选参数 @return 财务事件列表 */
    Map<String, Object> listFinancialEvents(AmazonFinancesReqVO request);
    /** @param request 发起付款请求参数 @return 付款引用编号 */
    Map<String, Object> initiatePayout(AmazonFinancesReqVO request);
    /** @param request 付款查询参数 @return 付款列表 */
    Map<String, Object> listPayouts(AmazonFinancesReqVO request);
    /** @param request 付款方式查询参数 @return 付款方式列表 */
    Map<String, Object> getPaymentMethods(AmazonFinancesReqVO request);
    /** @param request 预计付款查询参数 @return 预计付款列表 */
    Map<String, Object> listExpectedPayouts(AmazonFinancesReqVO request);
    /** @param request 发票头查询参数 @return 发票头列表 */
    Map<String, Object> getInvoiceHeaders(AmazonFinancesReqVO request);
    /** @param request 发票明细查询参数 @return 发票及行项目 */
    Map<String, Object> getInvoice(AmazonFinancesReqVO request);

}
