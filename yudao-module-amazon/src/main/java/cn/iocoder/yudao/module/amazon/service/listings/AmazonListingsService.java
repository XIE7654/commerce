package cn.iocoder.yudao.module.amazon.service.listings;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;

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
}
