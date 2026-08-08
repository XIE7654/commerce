package cn.iocoder.yudao.module.amazon.service.productfees;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO; import java.util.Map;
/** Product Fees API 服务。 */
public interface AmazonProductFeesService { Map<String,Object> sku(AmazonProductsReqVO r); Map<String,Object> asin(AmazonProductsReqVO r); Map<String,Object> batch(AmazonProductsReqVO r); }
