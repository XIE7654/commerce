package cn.iocoder.yudao.module.amazon.service.listingsitems;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPatchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPutReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;

import java.util.Map;

/** Listings Items API 服务。 */
public interface AmazonListingsItemsService {

    /** 查询 Listings Items。 */
    AmazonApiResponse<Map<String, Object>> search(AmazonListingsSearchReqVO request);

    /** 查询单个 Listings Item。 */
    AmazonApiResponse<Map<String, Object>> get(AmazonListingsItemGetReqVO request);

    /** 创建或全量更新 Listings Item。 */
    AmazonApiResponse<Map<String, Object>> put(AmazonListingsItemPutReqVO request);

    /** 局部更新 Listings Item。 */
    AmazonApiResponse<Map<String, Object>> patch(AmazonListingsItemPatchReqVO request);

    /** 删除 Listings Item。 */
    AmazonApiResponse<Map<String, Object>> delete(AmazonListingsItemGetReqVO request);
}
