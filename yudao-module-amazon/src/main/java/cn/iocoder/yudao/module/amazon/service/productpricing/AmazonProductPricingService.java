package cn.iocoder.yudao.module.amazon.service.productpricing;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.productpricing.vo.AmazonProductPricingReqVO;
import java.util.Map;
/** Product Pricing API 服务。 */
public interface AmazonProductPricingService {
    /**
     * 查询 Featured Offer 预期价格。
     *
     * @param request 店铺、站点和 2022 版本官方批量请求体
     * @return Amazon 原始响应
     */
    Map<String, Object> featured(AmazonProductsReqVO request);
    /**
     * 查询 2022 版本商品竞争摘要。
     *
     * @param request 店铺、站点和 2022 版本官方批量请求体
     * @return Amazon 原始响应
     */
    Map<String, Object> competitive(AmazonProductsReqVO request);
    /**
     * 查询卖家商品价格。
     *
     * @param request 店铺、站点、商品标识类型及对应标识列表
     * @return Amazon 原始响应
     */
    Map<String, Object> getPricing(AmazonProductPricingReqVO request);
    /**
     * 查询卖家商品竞争价格。
     *
     * @param request 店铺、站点、商品标识类型及对应标识列表
     * @return Amazon 原始响应
     */
    Map<String, Object> getCompetitivePricing(AmazonProductPricingReqVO request);
    /**
     * 按卖家 SKU 查询最低报价。
     *
     * @param request 店铺、站点、卖家 SKU 和商品成色
     * @return Amazon 原始响应
     */
    Map<String, Object> getListingOffers(AmazonProductPricingReqVO request);
    /**
     * 按 ASIN 查询最低报价。
     *
     * @param request 店铺、站点、ASIN 和商品成色
     * @return Amazon 原始响应
     */
    Map<String, Object> getItemOffers(AmazonProductPricingReqVO request);
    /**
     * 按 ASIN 批量查询最低报价。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 原始响应
     */
    Map<String, Object> getItemOffersBatch(AmazonProductPricingReqVO request);
    /**
     * 按卖家 SKU 批量查询最低报价。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 原始响应
     */
    Map<String, Object> getListingOffersBatch(AmazonProductPricingReqVO request);
}
