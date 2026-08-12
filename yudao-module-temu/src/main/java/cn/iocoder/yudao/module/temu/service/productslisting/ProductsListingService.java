package cn.iocoder.yudao.module.temu.service.productslisting;

import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryReqVO;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;

import java.util.List;

/**
 * Products Listing 商品刊登业务 Service。
 */
public interface ProductsListingService {

    /**
     * 查询商品分类。
     *
     * @param request 分类查询参数
     * @return Temu 官方分类响应
     */
    TemuApiResponse<List<CatsGetCategoryResult>> getGoodsCategories(ProductsListingCategoryReqVO request);

}
