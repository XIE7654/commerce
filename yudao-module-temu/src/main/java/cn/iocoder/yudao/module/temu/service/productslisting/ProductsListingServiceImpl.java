package cn.iocoder.yudao.module.temu.service.productslisting;

import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.productslisting.vo.ProductsListingCategoryReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetRequest;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Locale;

/**
 * Products Listing 商品刊登业务 Service 实现。
 */
@Service
@Validated
public class ProductsListingServiceImpl implements ProductsListingService {

    @Resource
    private TemuProperties temuProperties;

    /**
     * 查询商品分类。
     *
     * @param request 分类查询参数
     * @return Temu 官方分类响应
     */
    @Override
    public cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse<List<CatsGetCategoryResult>> getGoodsCategories(ProductsListingCategoryReqVO request) {
        CatsGetRequest catsRequest = new CatsGetRequest();
        catsRequest.setParentCatId(request.getParentCatId());
        return createFrameworkClient(request).getProduct().catsGet(catsRequest);
    }

    /**
     * 根据请求站点创建新版 Temu 客户端，供已迁移的分类接口使用。
     *
     * @param request 包含站点和授权 Token 的请求参数
     * @return 已配置的新版 Temu 客户端
     */
    private cn.iocoder.yudao.module.temu.framework.client.TemuClient createFrameworkClient(ProductsListingBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new cn.iocoder.yudao.module.temu.framework.client.TemuClient(
                region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.name());
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
