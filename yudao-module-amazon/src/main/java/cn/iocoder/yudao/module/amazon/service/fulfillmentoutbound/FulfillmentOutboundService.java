package cn.iocoder.yudao.module.amazon.service.fulfillmentoutbound;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;

import java.util.Map;

/** Amazon Fulfillment Outbound 服务。 */
public interface FulfillmentOutboundService {

    /**
     * 调用 Fulfillment Outbound v2020-07-01 或 v2026-07-04 的白名单 operation。
     *
     * @param operation Amazon 模型中定义的 operationId
     * @param request 店铺、路径参数、查询参数及请求体
     * @return Amazon 原始 JSON 响应；204 时为空 Map
     */
    Map<String, Object> invoke(String operation, AmazonFulfillmentApiReqVO request);
}
