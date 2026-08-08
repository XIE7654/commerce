package cn.iocoder.yudao.module.amazon.service.catalogitems;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import java.util.Map;
/** Catalog Items API 服务。 */
public interface AmazonCatalogItemsService { Map<String,Object> search(AmazonProductsReqVO request); Map<String,Object> get(AmazonProductsReqVO request); }
