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
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Products Listing 商品刊登业务 Service 实现。
 */
@Service
@Validated
public class ProductsListingServiceImpl implements ProductsListingService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;

    /**
     * 查询商品分类。
     *
     * @param request 分类查询参数
     * @return Temu 官方分类响应
     */
    @Override
    public JsonNode getGoodsCategories(ProductsListingCategoryReqVO request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("parentCatId", request.getParentCatId());
        return createClient(request).getProduct().catsGet(params);
    }

    /**
     * 查询分类商品模板。
     *
     * @param request 分类模板查询参数
     * @return Temu 官方模板响应
     */
    @Override
    public JsonNode getGoodsTemplate(ProductsListingCatIdReqVO request) {
        return createClient(request).getProduct().templateGet(Map.of("catId", request.getCatId()));
    }

    /**
     * 查询可用品牌与商标。
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方品牌与商标响应
     */
    @Override
    public JsonNode getAvailableBrandAndTrademark(ProductsListingBrandTrademarkReqVO request) {
        return getBrandTrademark(request);
    }

    /**
     * 查询签名信息。
     *
     * <p>Postman 集合中的 getSignature 请求与品牌商标查询使用相同 type 和参数，
     * 因此按集合定义转发，确保调用结果可复现。</p>
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方响应
     */
    @Override
    public JsonNode getSignature(ProductsListingBrandTrademarkReqVO request) {
        return getBrandTrademark(request);
    }

    /**
     * 查询运费模板。
     *
     * @param request 认证参数
     * @return Temu 官方运费模板响应
     */
    @Override
    public JsonNode getShippingTemplates(ProductsListingBaseReqVO request) {
        return createClient(request).getProduct().freightTemplateListQuery(Map.of());
    }

    /**
     * 查询尺码元素。
     *
     * @param request 分类参数
     * @return Temu 官方尺码元素响应
     */
    @Override
    public JsonNode getSizeElements(ProductsListingCatIdReqVO request) {
        return createClient(request).getProduct().sizeElementGet(Map.of("catId", request.getCatId()));
    }

    /**
     * 上传并自动处理商品图片。
     *
     * @param request 图片地址和处理参数
     * @return Temu 官方上传响应
     */
    @Override
    public JsonNode uploadImageWithAutoTransformer(ProductsListingImageUploadReqVO request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("fileUrl", request.getFileUrl());
        params.put("formatConversionType", request.getFormatConversionType());
        params.put("scalingType", request.getScalingType());
        params.put("compressionType", request.getCompressionType());
        return createClient(request).getProduct().imageUpload(params);
    }

    /**
     * 创建规格 ID。
     *
     * @param request 分类、父规格与子规格名称
     * @return Temu 官方规格响应
     */
    @Override
    public JsonNode createSpecId(ProductsListingSpecIdReqVO request) {
        return createClient(request).getProduct().specIdGet(Map.of(
                "catId", request.getCatId(), "parentSpecId", request.getParentSpecId(),
                "childSpecName", request.getChildSpecName()));
    }

    /**
     * 创建商品。
     *
     * <p>商品属性和 SKU 结构随分类模板变化，使用 JSON 节点原样转发，避免本地字段模型
     * 截断平台扩展字段。</p>
     *
     * @param request 商品创建参数
     * @return Temu 官方商品创建响应
     */
    @Override
    public JsonNode createGoods(ProductsListingGoodsCreateReqVO request) {
        return createClient(request).getProduct().goodsAdd(Map.of(
                "goodsBasic", request.getGoodsBasic(), "goodsDesc", request.getGoodsDesc(),
                "goodsProperty", request.getGoodsProperty(), "goodsServicePromise", request.getGoodsServicePromise(),
                "goodsTrademark", request.getGoodsTrademark(), "skuList", request.getSkuList()));
    }

    /**
     * 推荐商品分类。
     *
     * @param request 商品名称
     * @return Temu 官方分类推荐响应
     */
    @Override
    public JsonNode recommendCategories(ProductsListingCategoryRecommendReqVO request) {
        return createClient(request).getProduct().categoryRecommend(Map.of("goodsName", request.getGoodsName()));
    }

    /**
     * 推荐商品属性。
     *
     * @param request 分类、商品名称和已有属性
     * @return Temu 官方属性推荐响应
     */
    @Override
    public JsonNode recommendProperties(ProductsListingPropertyRecommendReqVO request) {
        return createClient(request).getProduct().propertyGet(Map.of(
                "catId", request.getCatId(), "goodsName", request.getGoodsName(), "goodsPropList", request.getGoodsPropList()));
    }

    /**
     * 调用集合定义的品牌与商标接口，供两个同参请求复用。
     *
     * @param request 品牌与商标查询参数
     * @return Temu 官方响应
     */
    private JsonNode getBrandTrademark(ProductsListingBrandTrademarkReqVO request) {
        return createClient(request).getProduct().brandTrademarkGet(Map.of(
                "brandId", request.getBrandId(), "page", request.getPage(), "size", request.getSize()));
    }

    /**
     * 根据站点区域配置创建 Temu 客户端，应用密钥始终由服务端管理。
     *
     * @param request 包含站点和授权 Token 的请求参数
     * @return 已初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(ProductsListingBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(),
                site.getEndpoint(), temuJsonStorageService);
    }

    /**
     * 判断字符串是否为空白。
     *
     * @param value 待判断字符串
     * @return 字符串为空或仅包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
