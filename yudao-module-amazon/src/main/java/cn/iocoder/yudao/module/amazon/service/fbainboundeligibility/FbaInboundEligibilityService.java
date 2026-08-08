package cn.iocoder.yudao.module.amazon.service.fbainboundeligibility;

import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;

import java.util.Map;

/** Amazon FBA Inbound Eligibility 服务。 */
public interface FbaInboundEligibilityService {

    /**
     * 查询 ASIN 在 FBA INBOUND 或 COMMINGLING 计划下的资格预览。
     *
     * @param request 店铺及查询参数；query 必须包含 asin 和 program
     * @return Amazon 原始资格预览响应
     */
    Map<String, Object> getItemEligibilityPreview(AmazonFulfillmentApiReqVO request);
}
