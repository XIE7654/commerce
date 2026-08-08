package cn.iocoder.yudao.module.temu.service.pricing;

import cn.iocoder.yudao.module.temu.controller.admin.pricing.vo.*;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;

/** Temu Pricing 定价业务 Service 实现。 */
@Service
@Validated
public class PricingServiceImpl implements PricingService {

    @Resource private TemuProperties temuProperties;
    @Resource private TemuJsonStorageService temuJsonStorageService;

    /** 查询商品 SKU 当前供货价。 @param request 查询参数 @return Temu 官方响应 */
    @Override public JsonNode getGoodsPriceList(PricingGoodsPriceListReqVO request) {
        return createClient(request).getPrice().skuListPriceQuery(Map.of("querySupplierPriceBaseList", request.getQuerySupplierPriceBaseList()));
    }

    /** 提交商品 SKU 供货价变更。 @param request 调价参数 @return Temu 官方响应 */
    @Override public JsonNode updateGoodsPrice(PricingUpdateGoodsPriceReqVO request) {
        return createClient(request).getPrice().changeSkuPrice(Map.of("goodsId", request.getGoodsId(), "changeSkuPriceDTOList", request.getChangeSkuPriceDTOList()));
    }

    /** 按类型及分页条件查询调价单。 @param request 查询参数 @return Temu 官方响应 */
    @Override public JsonNode getGoodsPriceOrder(PricingPriceOrderQueryReqVO request) {
        Map<String, Object> params = new java.util.LinkedHashMap<>();
        params.put("page", request.getPage()); params.put("size", request.getSize());
        params.put("priceOrderType", request.getPriceOrderType()); params.put("goodsId", request.getGoodsId());
        return createClient(request).getPrice().priceorderQuery(params);
    }

    /** 接受指定版本的调价单。 @param request 接受参数 @return Temu 官方响应 */
    @Override public JsonNode acceptGoodsPriceOrder(PricingPriceOrderAcceptReqVO request) {
        return createClient(request).getPrice().priceorderAccept(Map.of("priceOrderInfoList", request.getPriceOrderInfoList()));
    }

    /** 为调价单提交新的 SKU 供货价。 @param request 议价参数 @return Temu 官方响应 */
    @Override public JsonNode negotiateGoodsPriceOrder(PricingPriceOrderNegotiateReqVO request) {
        return createClient(request).getPrice().priceorderNegotiate(Map.of("priceOrderId", request.getPriceOrderId(), "goodsId", request.getGoodsId(),
                "priceCommitVersion", request.getPriceCommitVersion(), "priceCommitId", request.getPriceCommitId(), "negotiatedPriceSkuList", request.getNegotiatedPriceSkuList()));
    }

    /** 拒绝指定调价单，并按请求决定是否下架。 @param request 拒绝参数 @return Temu 官方响应 */
    @Override public JsonNode rejectGoodsPriceOrder(PricingPriceOrderRejectReqVO request) {
        return createClient(request).getPrice().priceorderReject(Map.of("rejectDelist", request.isRejectDelist(), "priceOrderBaseList", request.getPriceOrderBaseList()));
    }

    /** 创建商品 SKU 的价格申诉单。 @param request 申诉参数 @return Temu 官方响应 */
    @Override public JsonNode createGoodsAppealPriceOrder(PricingAppealOrderCreateReqVO request) {
        return createClient(request).getPrice().appealorderCreate(Map.of("goodsId", request.getGoodsId(), "merchantAppealReasonCodeList", request.getMerchantAppealReasonCodeList(), "skuInfoList", request.getSkuInfoList()));
    }

    /** 查询商品当前的价格申诉单。 @param request 查询参数 @return Temu 官方响应 */
    @Override public JsonNode getGoodsAppealPriceOrder(PricingAppealOrderQueryReqVO request) {
        return createClient(request).getPrice().appealorderQuery(Map.of("goodsId", request.getGoodsId(), "tabCode", request.getTabCode()));
    }

    /** 查询指定 SKU 的价格申诉记录。 @param request 查询参数 @return Temu 官方响应 */
    @Override public JsonNode getGoodsAppealPriceOrderRecord(PricingAppealOrderRecordQueryReqVO request) {
        return createClient(request).getPrice().appealorderRecordQuery(Map.of("skuId", request.getSkuId()));
    }

    /** 查询一组商品的推荐供货价。 @param request 查询参数 @return Temu 官方响应 */
    @Override public JsonNode getGoodsRecommendedPrice(PricingRecommendedPriceQueryReqVO request) {
        return createClient(request).getPrice().recommendedPriceQuery(Map.of("recommendedPriceType", request.getRecommendedPriceType(), "goodsIdList", request.getGoodsIdList(), "language", request.getLanguage()));
    }

    /** 查询父订单金额。 @param request 查询参数 @return Temu 官方响应 */
    @Override public JsonNode getOrderAmount(PricingOrderAmountQueryReqVO request) {
        return createClient(request).getPrice().amountQuery(Map.of("parentOrderSn", request.getParentOrderSn()));
    }

    /** 按站点创建已携带店铺授权的 Temu 客户端。 @param request 包含站点和 Token 的请求参数 @return SDK 客户端 */
    private TemuClient createClient(PricingBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(), temuJsonStorageService);
    }

    /** 判断配置值是否为空白。 @param value 待判断值 @return 值为空或仅为空白时返回 true */
    private boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
}
