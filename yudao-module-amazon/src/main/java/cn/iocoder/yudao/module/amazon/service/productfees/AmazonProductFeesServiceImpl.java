package cn.iocoder.yudao.module.amazon.service.productfees;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO; import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService; import jakarta.annotation.Resource; import org.springframework.stereotype.Service; import java.util.Map;
/** Product Fees API 服务实现。 */
@Service public class AmazonProductFeesServiceImpl implements AmazonProductFeesService { @Resource private AmazonProductsService delegate; public Map<String,Object> sku(AmazonProductsReqVO r){return delegate.getMyFeesEstimateForSku(r);} public Map<String,Object> asin(AmazonProductsReqVO r){return delegate.getMyFeesEstimateForAsin(r);} public Map<String,Object> batch(AmazonProductsReqVO r){return delegate.getMyFeesEstimates(r);} }
