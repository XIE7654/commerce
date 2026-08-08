package cn.iocoder.yudao.module.amazon.service.producttypedefinitions;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO; import java.util.Map;
/** Product Type Definitions API 服务。 */
public interface AmazonProductTypeDefinitionsService { Map<String,Object> search(AmazonProductsReqVO r); Map<String,Object> get(AmazonProductsReqVO r); }
