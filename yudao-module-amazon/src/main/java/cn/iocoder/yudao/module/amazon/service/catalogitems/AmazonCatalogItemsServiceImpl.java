package cn.iocoder.yudao.module.amazon.service.catalogitems;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Map;
/** Catalog Items API 服务实现。 */
@Service
public class AmazonCatalogItemsServiceImpl implements AmazonCatalogItemsService {

    @Resource
    private AmazonProductsService delegate;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> search(AmazonProductsReqVO request) {
        return delegate.searchCatalogItems(request);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> get(AmazonProductsReqVO request) {
        return delegate.getCatalogItem(request);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> listCategories(AmazonProductsReqVO request) {
        return delegate.listCatalogCategories(request);
    }
}
