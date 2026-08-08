package cn.iocoder.yudao.module.amazon.service.listings;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPatchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPutReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsRestrictionsReqVO;

import java.util.Map;

/**
 * Amazon Listings Items 服务。
 */
public interface AmazonListingsService {

    /**
     * 查询店铺在指定站点的 Listings Items。
     *
     * @param request 店铺、国家代码及 Listings 筛选条件
     * @return Amazon Listings Items 原始响应
     */
    Map<String, Object> searchListingsItems(AmazonListingsSearchReqVO request);

    /**
     * 查询店铺在指定站点的单个 Listings Item。
     *
     * @param request 店铺、国家代码、SKU 及返回数据集
     * @return Amazon Listings Item 原始响应
     */
    Map<String, Object> getListingsItem(AmazonListingsItemGetReqVO request);

    /**
     * 创建或全量更新指定 SKU 的 Listings Item。
     *
     * @param request 店铺、站点、SKU 和完整商品属性
     * @return Amazon 提交结果
     */
    Map<String, Object> putListingsItem(AmazonListingsItemPutReqVO request);

    /**
     * 局部更新指定 SKU 的 Listings Item。
     *
     * @param request 店铺、站点、SKU 和 JSON Patch 操作
     * @return Amazon 提交结果
     */
    Map<String, Object> patchListingsItem(AmazonListingsItemPatchReqVO request);

    /**
     * 删除指定 SKU 的 Listings Item。
     *
     * @param request 店铺、站点和 SKU
     * @return Amazon 删除结果
     */
    Map<String, Object> deleteListingsItem(AmazonListingsItemGetReqVO request);

    /**
     * 查询指定 ASIN 的商品上架限制。
     *
     * @param request 店铺、站点和 ASIN
     * @return Amazon 限制信息
     */
    Map<String, Object> getListingsRestrictions(AmazonListingsRestrictionsReqVO request);
}
