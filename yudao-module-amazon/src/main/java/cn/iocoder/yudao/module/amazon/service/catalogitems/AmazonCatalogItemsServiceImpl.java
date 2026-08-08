package cn.iocoder.yudao.module.amazon.service.catalogitems;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Map;
/** Catalog Items API 服务实现。 */
@Service public class AmazonCatalogItemsServiceImpl implements AmazonCatalogItemsService { @Resource private AmazonProductsService delegate; public Map<String,Object> search(AmazonProductsReqVO r){return delegate.searchCatalogItems(r);} public Map<String,Object> get(AmazonProductsReqVO r){return delegate.getCatalogItem(r);} }
