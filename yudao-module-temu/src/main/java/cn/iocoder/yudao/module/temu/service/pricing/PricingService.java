package cn.iocoder.yudao.module.temu.service.pricing;

import cn.iocoder.yudao.module.temu.controller.admin.pricing.vo.*;
import tools.jackson.databind.JsonNode;

/** Temu Pricing 定价业务 Service。 */
public interface PricingService {
    /** 查询商品 SKU 供货价。 @param request 查询参数 @return Temu 官方响应 */ JsonNode getGoodsPriceList(PricingGoodsPriceListReqVO request);
    /** 修改商品 SKU 供货价。 @param request 调价参数 @return Temu 官方响应 */ JsonNode updateGoodsPrice(PricingUpdateGoodsPriceReqVO request);
    /** 查询调价单。 @param request 查询参数 @return Temu 官方响应 */ JsonNode getGoodsPriceOrder(PricingPriceOrderQueryReqVO request);
    /** 接受调价单。 @param request 接受参数 @return Temu 官方响应 */ JsonNode acceptGoodsPriceOrder(PricingPriceOrderAcceptReqVO request);
    /** 发起调价单议价。 @param request 议价参数 @return Temu 官方响应 */ JsonNode negotiateGoodsPriceOrder(PricingPriceOrderNegotiateReqVO request);
    /** 拒绝调价单。 @param request 拒绝参数 @return Temu 官方响应 */ JsonNode rejectGoodsPriceOrder(PricingPriceOrderRejectReqVO request);
    /** 创建价格申诉单。 @param request 申诉参数 @return Temu 官方响应 */ JsonNode createGoodsAppealPriceOrder(PricingAppealOrderCreateReqVO request);
    /** 查询价格申诉单。 @param request 查询参数 @return Temu 官方响应 */ JsonNode getGoodsAppealPriceOrder(PricingAppealOrderQueryReqVO request);
    /** 查询价格申诉记录。 @param request 查询参数 @return Temu 官方响应 */ JsonNode getGoodsAppealPriceOrderRecord(PricingAppealOrderRecordQueryReqVO request);
    /** 查询商品推荐价。 @param request 查询参数 @return Temu 官方响应 */ JsonNode getGoodsRecommendedPrice(PricingRecommendedPriceQueryReqVO request);
    /** 查询订单金额。 @param request 查询参数 @return Temu 官方响应 */ JsonNode getOrderAmount(PricingOrderAmountQueryReqVO request);
}
