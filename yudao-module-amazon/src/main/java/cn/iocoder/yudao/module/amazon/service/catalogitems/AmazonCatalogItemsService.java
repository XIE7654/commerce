package cn.iocoder.yudao.module.amazon.service.catalogitems;
import cn.iocoder.yudao.module.amazon.controller.admin.products.vo.AmazonProductsReqVO;
import java.util.Map;
/** Catalog Items API 服务。 */
public interface AmazonCatalogItemsService {

    /**
     * 搜索 Catalog Items。
     *
     * @param request 店铺、站点和搜索条件
     * @return Amazon 返回的 Catalog Items 数据
     */
    Map<String, Object> search(AmazonProductsReqVO request);

    /**
     * 按 ASIN 查询 Catalog Item。
     *
     * @param request 店铺、站点、ASIN 和返回数据集
     * @return Amazon 返回的 Catalog Item 数据
     */
    Map<String, Object> get(AmazonProductsReqVO request);

    /**
     * 查询指定 ASIN 或卖家 SKU 所属的 Catalog 分类层级。
     *
     * @param request 店铺、站点，以及二选一的 ASIN 或卖家 SKU
     * @return Amazon 返回的商品分类列表
     */
    Map<String, Object> listCategories(AmazonProductsReqVO request);
}
