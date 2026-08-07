package cn.iocoder.yudao.module.temu.service.ads;

import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsBaseReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsGoodsListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsGoodsReportReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsLogQueryReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsMallReportReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsModifyReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsRoasPredReqVO;
import cn.iocoder.yudao.module.temu.enums.TemuSiteRegionEnum;
import cn.iocoder.yudao.module.temu.framework.config.TemuProperties;
import cn.iocoder.yudao.module.temu.sdk.TemuClient;
import cn.iocoder.yudao.module.temu.sdk.TemuJsonStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.JsonNode;

import java.util.Locale;
import java.util.Map;

/** Temu Ads 业务 Service 实现。 */
@Service
@Validated
public class AdsServiceImpl implements AdsService {

    @Resource
    private TemuProperties temuProperties;
    @Resource
    private TemuJsonStorageService temuJsonStorageService;

    /**
     * 查询指定商品的 Temu 推荐 ROAS。
     *
     * @param request 包含站点、授权 Token 和商品列表的请求参数
     * @return Temu 官方 ROAS 预测响应
     */
    @Override
    public JsonNode predictRoas(AdsRoasPredReqVO request) {
        return createClient(request).getAds().roasPred(Map.of("goodsInfoList", request.getGoodsInfoList()));
    }

    /**
     * 查询店铺指定时间范围内的广告报表。
     *
     * @param request 包含站点、授权 Token 和本地时区时间范围的请求参数
     * @return Temu 官方店铺报表响应
     */
    @Override
    public JsonNode getMallReport(AdsMallReportReqVO request) {
        return createClient(request).getAds().reportsMallQuery(Map.of(
                "startTs", request.getStartTs(), "endTs", request.getEndTs()));
    }

    /**
     * 查询指定商品在时间范围内的广告报表。
     *
     * @param request 包含站点、授权 Token、商品和时间范围的请求参数
     * @return Temu 官方商品报表响应
     */
    @Override
    public JsonNode getGoodsReport(AdsGoodsReportReqVO request) {
        return createClient(request).getAds().reportsGoodsQuery(Map.of(
                "goodsId", request.getGoodsId(), "startTs", request.getStartTs(), "endTs", request.getEndTs()));
    }

    /**
     * 为商品创建 Temu 广告活动。
     *
     * @param request 包含站点、授权 Token 和广告配置的请求参数
     * @return Temu 官方广告创建响应
     */
    @Override
    public JsonNode createAds(AdsCreateReqVO request) {
        return createClient(request).getAds().adCreate(Map.of("createAdReqs", request.getCreateAdReqs()));
    }

    /**
     * 查询一组商品的广告详情。
     *
     * @param request 包含站点、授权 Token 和商品列表的请求参数
     * @return Temu 官方广告详情响应
     */
    @Override
    public JsonNode getAdDetails(AdsGoodsListReqVO request) {
        return createClient(request).getAds().adDetailQuery(Map.of("goodsList", request.getGoodsIds()));
    }

    /**
     * 查询指定商品在时间范围内的广告操作日志。
     *
     * @param request 包含站点、授权 Token、商品和时间范围的请求参数
     * @return Temu 官方广告日志响应
     */
    @Override
    public JsonNode getAdLogs(AdsLogQueryReqVO request) {
        return createClient(request).getAds().adLogQuery(Map.of(
                "goodsId", request.getGoodsId(), "startTime", request.getStartTime(), "endTime", request.getEndTime()));
    }

    /**
     * 查询商品是否满足 Temu 广告创建条件。
     *
     * @param request 包含站点、授权 Token 和商品列表的请求参数
     * @return Temu 官方商品可创建性响应
     */
    @Override
    public JsonNode getGoodsCreatability(AdsGoodsListReqVO request) {
        return createClient(request).getAds().adGoodsCreateQuery(Map.of("goodsIdList", request.getGoodsIds()));
    }

    /**
     * 按指定类型删除、暂停、开启或调整 Temu 广告。
     *
     * @param request 包含站点、授权 Token、修改类型和广告配置的请求参数
     * @return Temu 官方广告修改响应
     */
    @Override
    public JsonNode modifyAd(AdsModifyReqVO request) {
        return createClient(request).getAds().adModify(Map.of(
                "status", request.getStatus(), "modifyAdDTO", request.getModifyAdDTO()));
    }

    /**
     * 依据请求站点创建 SDK 客户端。
     *
     * <p>应用密钥只能从服务端区域配置读取，避免管理端请求泄露敏感应用配置。</p>
     *
     * @param request 包含站点与授权 Token 的请求参数
     * @return 已按区域配置初始化的 Temu SDK 客户端
     */
    private TemuClient createClient(AdsBaseReqVO request) {
        TemuSiteRegionEnum site = TemuSiteRegionEnum.valueOf(request.getSite().trim().toUpperCase(Locale.ROOT));
        TemuProperties.RegionProperties region = temuProperties.getRegion(site);
        if (region == null || isBlank(region.getAppKey()) || isBlank(region.getAppSecret())) {
            throw new IllegalArgumentException("Temu 站点未配置 appKey 或 appSecret: " + site.name());
        }
        return new TemuClient(region.getAppKey(), region.getAppSecret(), request.getAccessToken(), site.getEndpoint(), temuJsonStorageService);
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
