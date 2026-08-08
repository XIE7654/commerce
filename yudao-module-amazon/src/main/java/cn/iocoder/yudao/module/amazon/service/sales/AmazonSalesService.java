package cn.iocoder.yudao.module.amazon.service.sales;

import cn.iocoder.yudao.module.amazon.controller.admin.sales.vo.AmazonSalesOrderMetricsReqVO;

import java.util.Map;

/** Amazon Sales 服务。 */
public interface AmazonSalesService {
    /**
     * 查询指定站点和时间区间的聚合订单指标。
     *
     * @param request 店铺、站点及统计筛选条件
     * @return Amazon Sales 原始 JSON 响应
     */
    Map<String, Object> getOrderMetrics(AmazonSalesOrderMetricsReqVO request);
}
