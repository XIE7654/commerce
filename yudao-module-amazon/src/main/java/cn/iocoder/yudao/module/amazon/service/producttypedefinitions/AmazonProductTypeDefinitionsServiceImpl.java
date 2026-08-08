package cn.iocoder.yudao.module.amazon.service.producttypedefinitions;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO; import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService; import jakarta.annotation.Resource; import org.springframework.stereotype.Service; import java.util.Map;
/** Product Type Definitions API 服务实现。 */
@Service public class AmazonProductTypeDefinitionsServiceImpl implements AmazonProductTypeDefinitionsService { @Resource private AmazonProductsService delegate; public Map<String,Object> search(AmazonProductsReqVO r){return delegate.searchDefinitionsProductTypes(r);} public Map<String,Object> get(AmazonProductsReqVO r){return delegate.getDefinitionsProductType(r);} }
