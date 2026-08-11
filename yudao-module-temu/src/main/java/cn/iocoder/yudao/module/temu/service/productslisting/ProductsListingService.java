package cn.iocoder.yudao.module.temu.service.productslisting;

import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingBrandTrademarkReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCatIdReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryRecommendReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingGoodsCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingImageUploadReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingPropertyRecommendReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingSpecIdReqVO;
import cn.iocoder.yudao.module.temu.sdk.TemuApiResponse;
import cn.iocoder.yudao.module.temu.sdk.product.dto.CatsGetCategoryDto;
import tools.jackson.databind.JsonNode;

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
    TemuApiResponse<List<CatsGetCategoryDto>> getGoodsCategories(ProductsListingCategoryReqVO request);

    /**
     * 查询分类商品模板。
     *
     * @param request 分类模板查询参数
     * @return Temu 官方模板响应
     */
    JsonNode getGoodsTemplate(ProductsListingCatIdReqVO request);

    /**
     * 查询可用品牌与商标。
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方品牌与商标响应
     */
    JsonNode getAvailableBrandAndTrademark(ProductsListingBrandTrademarkReqVO request);

    /**
     * 查询品牌与商标签名信息。
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方响应
     */
    JsonNode getSignature(ProductsListingBrandTrademarkReqVO request);

    /**
     * 查询运费模板。
     *
     * @param request 认证参数
     * @return Temu 官方运费模板响应
     */
    JsonNode getShippingTemplates(ProductsListingBaseReqVO request);

    /**
     * 查询尺码元素。
     *
     * @param request 分类参数
     * @return Temu 官方尺码元素响应
     */
    JsonNode getSizeElements(ProductsListingCatIdReqVO request);

    /**
     * 上传并自动处理商品图片。
     *
     * @param request 图片地址和处理参数
     * @return Temu 官方上传响应
     */
    JsonNode uploadImageWithAutoTransformer(ProductsListingImageUploadReqVO request);

    /**
     * 创建规格 ID。
     *
     * @param request 分类、父规格与子规格名称
     * @return Temu 官方规格响应
     */
    JsonNode createSpecId(ProductsListingSpecIdReqVO request);

    /**
     * 创建商品。
     *
     * @param request 商品创建参数
     * @return Temu 官方商品创建响应
     */
    JsonNode createGoods(ProductsListingGoodsCreateReqVO request);

    /**
     * 推荐商品分类。
     *
     * @param request 商品名称
     * @return Temu 官方分类推荐响应
     */
    JsonNode recommendCategories(ProductsListingCategoryRecommendReqVO request);

    /**
     * 推荐商品属性。
     *
     * @param request 分类、商品名称和已有属性
     * @return Temu 官方属性推荐响应
     */
    JsonNode recommendProperties(ProductsListingPropertyRecommendReqVO request);
}
