package cn.iocoder.yudao.module.amazon.service.products;

import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;

import java.util.Map;

/** Amazon Products 相关 API 服务。 */
public interface AmazonProductsService {

    /**
     * 查询 Catalog Items。
     *
     * @param request 店铺、站点和 Catalog 搜索条件
     * @return Amazon 返回的 Catalog Items 数据
     */
    Map<String, Object> searchCatalogItems(AmazonProductsReqVO request);

    /**
     * 按 ASIN 查询 Catalog Item。
     *
     * @param request 店铺、站点、ASIN 和返回数据集
     * @return Amazon 返回的 Catalog Item 数据
     */
    Map<String, Object> getCatalogItem(AmazonProductsReqVO request);

    /**
     * 查询商品所属的 Catalog 分类层级。
     *
     * @param request 店铺、站点，以及二选一的 ASIN 或卖家 SKU
     * @return Amazon 返回的商品分类列表
     */
    Map<String, Object> listCatalogCategories(AmazonProductsReqVO request);

    /**
     * 批量查询 Featured Offer 预期价格。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 返回的预期价格数据
     */
    Map<String, Object> getFeaturedOfferExpectedPriceBatch(AmazonProductsReqVO request);

    /**
     * 批量查询商品竞争摘要。
     *
     * @param request 店铺、站点和官方批量请求体
     * @return Amazon 返回的竞争摘要数据
     */
    Map<String, Object> getCompetitiveSummary(AmazonProductsReqVO request);

    /**
     * 按卖家 SKU 预估费用。
     *
     * @param request 店铺、站点、卖家 SKU 和费用请求体
     * @return Amazon 返回的费用预估数据
     */
    Map<String, Object> getMyFeesEstimateForSku(AmazonProductsReqVO request);

    /**
     * 按 ASIN 预估费用。
     *
     * @param request 店铺、站点、ASIN 和费用请求体
     * @return Amazon 返回的费用预估数据
     */
    Map<String, Object> getMyFeesEstimateForAsin(AmazonProductsReqVO request);

    /**
     * 批量预估费用。
     *
     * @param request 店铺、站点和官方费用请求数组
     * @return Amazon 返回的批量费用预估数据
     */
    Map<String, Object> getMyFeesEstimates(AmazonProductsReqVO request);

    /**
     * 搜索商品类型定义。
     *
     * @param request 店铺、站点和商品类型搜索条件
     * @return Amazon 返回的商品类型定义列表
     */
    Map<String, Object> searchDefinitionsProductTypes(AmazonProductsReqVO request);

    /**
     * 查询指定商品类型定义。
     *
     * @param request 店铺、站点、商品类型和定义筛选条件
     * @return Amazon 返回的商品类型定义
     */
    Map<String, Object> getDefinitionsProductType(AmazonProductsReqVO request);
}
