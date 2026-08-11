package cn.iocoder.yudao.module.amazon.service.listingsrestrictions;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsRestrictionsReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;

import java.util.Map;

/** Listings Restrictions API 服务。 */
public interface AmazonListingsRestrictionsService {

    /** 查询商品上架限制。 */
    AmazonApiResponse<Map<String, Object>> get(AmazonListingsRestrictionsReqVO request);
}
