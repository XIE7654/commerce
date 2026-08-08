package cn.iocoder.yudao.module.amazon.service.merchantfulfillment;

import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;

import java.util.Map;

/** Amazon Merchant Fulfillment API 服务。 */
public interface MerchantFulfillmentService {

    /**
     * 调用 Merchant Fulfillment v0 白名单 operation。
     *
     * @param operation Amazon 模型中定义的 operationId
     * @param request 店铺、路径参数、查询参数及请求体
     * @return Amazon 原始 JSON 响应；204 时为空 Map
     */
    Map<String, Object> invoke(String operation, AmazonSpApiReqVO request);
}
