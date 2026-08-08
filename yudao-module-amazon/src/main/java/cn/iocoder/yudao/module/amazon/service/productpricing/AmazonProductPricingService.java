package cn.iocoder.yudao.module.amazon.service.productpricing;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import java.util.Map;
/** Product Pricing API 服务。 */
public interface AmazonProductPricingService { Map<String,Object> featured(AmazonProductsReqVO r); Map<String,Object> competitive(AmazonProductsReqVO r); }
