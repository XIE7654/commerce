package cn.iocoder.yudao.module.amazon.service.listingsitems;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.*; import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService; import jakarta.annotation.Resource; import org.springframework.stereotype.Service; import java.util.Map;
/** Listings Items API 服务。 */
public interface AmazonListingsItemsService { Map<String,Object> search(AmazonListingsSearchReqVO r); Map<String,Object> get(AmazonListingsItemGetReqVO r); Map<String,Object> put(AmazonListingsItemPutReqVO r); Map<String,Object> patch(AmazonListingsItemPatchReqVO r); Map<String,Object> delete(AmazonListingsItemGetReqVO r); }
