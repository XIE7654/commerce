package cn.iocoder.yudao.module.temu.service.productsmanagement;

import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementFullUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementGoodsIdReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementGoodsListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementPartialUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSaleStatusUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSkuListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSpecDetailReqVO;
import tools.jackson.databind.JsonNode;

/**
 * Products Management 商品管理业务 Service。
 */
public interface ProductsManagementService {

    /**
     * 查询 Temu 商品列表。
     * @param request 商品列表查询参数
     * @return Temu 官方商品列表响应
     */
    JsonNode getGoodsList(ProductsManagementGoodsListReqVO request);

    /**
     * 查询 Temu SKU 列表。
     * @param request SKU 列表查询参数
     * @return Temu 官方 SKU 列表响应
     */
    JsonNode getGoodsSkuList(ProductsManagementSkuListReqVO request);

    /**
     * 查询 Temu 商品详情。
     * @param request 商品详情查询参数
     * @return Temu 官方商品详情响应
     */
    JsonNode getGoodsDetail(ProductsManagementGoodsIdReqVO request);

    /**
     * 查询 Temu 商品规格详情。
     * @param request 商品规格详情查询参数
     * @return Temu 官方商品规格详情响应
     */
    JsonNode getGoodsSpecDetail(ProductsManagementSpecDetailReqVO request);

    /**
     * 完整更新 Temu 商品。
     * @param request 商品完整更新参数
     * @return Temu 官方更新响应
     */
    JsonNode updateGoodsInfo(ProductsManagementFullUpdateReqVO request);

    /**
     * 部分更新 Temu 商品。
     * @param request 商品部分更新参数
     * @return Temu 官方更新响应
     */
    JsonNode partialUpdateGoodsInfo(ProductsManagementPartialUpdateReqVO request);

    /**
     * 查询 Temu 商品上架状态。
     * @param request 商品上架状态查询参数
     * @return Temu 官方上架状态响应
     */
    JsonNode getGoodsSaleStatus(ProductsManagementGoodsIdReqVO request);

    /**
     * 更新 Temu 商品上架状态。
     * @param request 商品上架状态更新参数
     * @return Temu 官方更新响应
     */
    JsonNode updateGoodsSaleStatus(ProductsManagementSaleStatusUpdateReqVO request);
}
