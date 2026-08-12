package cn.iocoder.yudao.module.temu.service.addproducts;

import cn.iocoder.yudao.module.temu.controller.admin.addproducts.vo.AddProductsCatsReqVO;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;

import java.util.List;

/**
 * Add Products 商品发布相关业务 Service。
 */
public interface AddProductsService {

    /**
     * 查询 Temu 商品分类。
     *
     * @param request 查询参数，包含站点、授权 Token、语言和可选父分类 ID
     * @return Temu 官方分类查询响应
     */
    TemuApiResponse<List<CatsGetCategoryResult>> getCategories(AddProductsCatsReqVO request);
}
