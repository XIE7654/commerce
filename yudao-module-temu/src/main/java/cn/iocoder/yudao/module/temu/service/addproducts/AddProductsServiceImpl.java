package cn.iocoder.yudao.module.temu.service.addproducts;

import cn.iocoder.yudao.module.temu.controller.admin.addproducts.vo.AddProductsCatsReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.framework.client.TemuApiResponse;
import cn.iocoder.yudao.module.temu.framework.client.TemuClient;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetCategoryResult;
import cn.iocoder.yudao.module.temu.framework.client.product.CatsGetRequest;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.List;

/**
 * Add Products 商品发布相关业务 Service 实现。
 */
@Service
@Validated
public class AddProductsServiceImpl implements AddProductsService {

    @Resource
    private TemuProperties temuProperties;

    /**
     * 查询 Temu 商品分类。
     *
     * <p>父分类 ID 不传时保留空值，由 Temu 返回一级分类；请求参数名称按 Temu
     * OpenAPI 的 camelCase 约定传递，SDK 负责公共参数和签名处理。</p>
     *
     * @param request 查询参数，包含站点、授权 Token、语言和可选父分类 ID
     * @return Temu 官方分类查询响应
     */
    @Override
    public TemuApiResponse<List<CatsGetCategoryResult>> getCategories(AddProductsCatsReqVO request) {
        CatsGetRequest catsRequest = new CatsGetRequest();
        catsRequest.setLanguage(request.getLanguage());
        catsRequest.setParentCatId(request.getParentCatId());
        return createFrameworkClient(request.getSite(), request.getAccessToken()).getProduct().catsGet(catsRequest);
    }

    /**
     * 按站点区域配置创建新版 Temu 客户端，分类接口不再依赖旧 SDK。
     *
     * @param siteCode Temu 站点代码
     * @param accessToken Temu 授权 Token
     * @return 已配置的新版 Temu 客户端
     */
    private TemuClient createFrameworkClient(String siteCode, String accessToken) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(siteCode.trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), accessToken, site.name());
    }

    /**
     * 判断配置字符串是否为空白。
     *
     * @param value 待判断字符串
     * @return 为空或只包含空白字符时返回 true
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
