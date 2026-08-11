package cn.iocoder.yudao.module.amazon.service.listingsitems;

import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemGetReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPatchReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsItemPutReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.listings.vo.AmazonListingsSearchReqVO;
import cn.iocoder.yudao.module.amazon.sdk.AmazonApiResponse;
import cn.iocoder.yudao.module.amazon.service.listings.AmazonListingsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/** Listings Items API 服务实现。 */
@Service
public class AmazonListingsItemsServiceImpl implements AmazonListingsItemsService {

    @Resource
    private AmazonListingsService delegate;

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> search(AmazonListingsSearchReqVO request) {
        return delegate.searchListingsItems(request);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> get(AmazonListingsItemGetReqVO request) {
        return delegate.getListingsItem(request);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> put(AmazonListingsItemPutReqVO request) {
        return delegate.putListingsItem(request);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> patch(AmazonListingsItemPatchReqVO request) {
        return delegate.patchListingsItem(request);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonApiResponse<Map<String, Object>> delete(AmazonListingsItemGetReqVO request) {
        return delegate.deleteListingsItem(request);
    }
}
