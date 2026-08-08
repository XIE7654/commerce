package cn.iocoder.yudao.module.amazon.service.fulfillmentinbound;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;

import java.util.Map;

/** Amazon Fulfillment Inbound 服务。 */
public interface FulfillmentInboundService {

    /**
     * 调用 Fulfillment Inbound v0 或 v2024-03-20 的白名单 operation。
     *
     * @param operation Amazon 模型中定义的 operationId
     * @param request 店铺、路径参数、查询参数及请求体
     * @return Amazon 原始 JSON 响应；204 时为空 Map
     */
    Map<String, Object> invoke(String operation, AmazonFulfillmentApiReqVO request);
}
