package cn.iocoder.yudao.module.amazon.service.productpricing;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import cn.iocoder.yudao.module.amazon.service.products.AmazonProductsService;
import jakarta.annotation.Resource; import org.springframework.stereotype.Service; import java.util.Map;
/** Product Pricing API 服务实现。 */
@Service public class AmazonProductPricingServiceImpl implements AmazonProductPricingService { @Resource private AmazonProductsService delegate; public Map<String,Object> featured(AmazonProductsReqVO r){return delegate.getFeaturedOfferExpectedPriceBatch(r);} public Map<String,Object> competitive(AmazonProductsReqVO r){return delegate.getCompetitiveSummary(r);} }
