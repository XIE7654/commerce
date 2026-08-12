package cn.iocoder.yudao.module.amazon.service.listings;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import com.amazon.SellingPartnerAPIAA.LWAException;
import software.amazon.spapi.ApiException;
import software.amazon.spapi.models.listings.items.v2021_08_01.Item;
import software.amazon.spapi.models.listings.items.v2021_08_01.ItemSearchResults;

/** Amazon Listings Items 只读服务。 */
public interface AmazonListingsService {

    /**
     * 查询店铺在指定站点的 Listings Items。
     *
     * @param request 店铺、国家代码及查询筛选条件
     * @return 官方 SDK Listings Items 查询结果
     */
    ItemSearchResults searchListingsItems(AmazonListingsSearchReqVO request) throws ApiException, LWAException;

    /**
     * 查询店铺在指定站点的单个 Listings Item。
     *
     * @param request 店铺、国家代码、SKU 及返回数据集
     * @return 官方 SDK Listings Item
     */
    Item getListingsItem(AmazonListingsItemGetReqVO request) throws ApiException, LWAException;
}
