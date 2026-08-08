package cn.iocoder.yudao.module.temu.service.productsmanagement;

import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementFullUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementGoodsIdReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementGoodsListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementPartialUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSaleStatusUpdateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSkuListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productsmanagement.vo.ProductsManagementSpecDetailReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import cn.iocoder.yudao.module.temu.service.apirequestlog.TemuApiRequestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Products Management 商品管理业务 Service 实现。
 */
@Service
@Validated
public class ProductsManagementServiceImpl implements ProductsManagementService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;
    @Resource
    private TemuApiRequestLogService temuApiRequestLogService;

    /**
     * 查询 Temu 商品列表。
     *
     * @param request 商品列表查询参数
     * @return Temu 官方商品列表响应
     */
    @Override
    public JsonNode getGoodsList(ProductsManagementGoodsListReqVO request) {
        return createClient(request).getProduct().goodsListQuery(Map.of(
                "goodsSearchType", request.getGoodsSearchType(), "orderType", request.getOrderType(),
                "pageNo", request.getPageNo(), "pageSize", request.getPageSize()));
    }

    /**
     * 查询 Temu SKU 列表。
     *
     * @param request SKU 列表查询参数
     * @return Temu 官方 SKU 列表响应
     */
    @Override
    public JsonNode getGoodsSkuList(ProductsManagementSkuListReqVO request) {
        return createClient(request).getProduct().skuListQuery(Map.of(
                "skuStatusFilterType", request.getSkuStatusFilterType(),
                "pageNo", request.getPageNo(), "pageSize", request.getPageSize()));
    }

    /**
     * 查询 Temu 商品详情。
     *
     * @param request 商品详情查询参数
     * @return Temu 官方商品详情响应
     */
    @Override
    public JsonNode getGoodsDetail(ProductsManagementGoodsIdReqVO request) {
        return createClient(request).getProduct().goodsDetailQuery(Map.of("goodsId", request.getGoodsId()));
    }

    /**
     * 查询 Temu 商品规格详情。
     *
     * @param request 商品规格详情查询参数
     * @return Temu 官方商品规格详情响应
     */
    @Override
    public JsonNode getGoodsSpecDetail(ProductsManagementSpecDetailReqVO request) {
        return createClient(request).getProduct().specInfoGet(Map.of("specIdList", request.getSpecIdList()));
    }

    /**
     * 完整更新 Temu 商品。
     *
     * <p>商品属性和 SKU 的嵌套结构由 Temu 分类模板决定，使用 JSON 节点原样转发，
     * 防止平台扩展字段被本地 VO 截断。</p>
     *
     * @param request 商品完整更新参数
     * @return Temu 官方更新响应
     */
    @Override
    public JsonNode updateGoodsInfo(ProductsManagementFullUpdateReqVO request) {
        return createClient(request).getProduct().goodsUpdate(Map.of(
                "goodsId", request.getGoodsId(), "goodsProperty", request.getGoodsProperty(),
                "goodsBasic", request.getGoodsBasic(), "goodsServicePromise", request.getGoodsServicePromise(),
                "skuList", request.getSkuList()));
    }

    /**
     * 部分更新 Temu 商品。
     *
     * <p>仅将客户端提供的非空字段提交给 Temu，使未提交字段维持平台原有值。</p>
     *
     * @param request 商品部分更新参数
     * @return Temu 官方更新响应
     */
    @Override
    public JsonNode partialUpdateGoodsInfo(ProductsManagementPartialUpdateReqVO request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("goodsId", request.getGoodsId());
        params.put("bulletPoints", request.getBulletPoints());
        params.put("catId", request.getCatId());
        params.put("goodsDesc", request.getGoodsDesc());
        params.put("goodsName", request.getGoodsName());
        params.put("goodsProperties", request.getGoodsProperties());
        params.put("goodsServicePromise", request.getGoodsServicePromise());
        return createClient(request).getProduct().goodsPartialUpdate(params);
    }

    /**
     * 查询 Temu 商品上架状态。
     *
     * @param request 商品上架状态查询参数
     * @return Temu 官方上架状态响应
     */
    @Override
    public JsonNode getGoodsSaleStatus(ProductsManagementGoodsIdReqVO request) {
        return createClient(request).getProduct().publishStatusGet(Map.of("goodsIdList", List.of(request.getGoodsId())));
    }

    /**
     * 更新 Temu 商品上架状态。
     *
     * @param request 商品上架状态更新参数
     * @return Temu 官方更新响应
     */
    @Override
    public JsonNode updateGoodsSaleStatus(ProductsManagementSaleStatusUpdateReqVO request) {
        return createClient(request).getProduct().saleStatusSet(Map.of(
                "goodsId", request.getGoodsId(), "onsale", request.getOnsale()));
    }

    /**
     * 按请求站点创建 Temu SDK 客户端。
     *
     * <p>应用密钥仅从服务端区域配置读取，避免管理端请求暴露敏感应用配置。</p>
     *
     * @param request 包含站点与授权 Token 的请求参数
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(ProductsManagementBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(),
                temuJsonStorageService, site.name(), temuApiRequestLogService);
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断配置值
     * @return 值为空或只包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
