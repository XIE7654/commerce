package cn.iocoder.yudao.module.temu.controller.admin.ads;

import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsGoodsListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsGoodsReportReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsLogQueryReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsMallReportReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsModifyReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsRoasPredReqVO;
import cn.iocoder.yudao.module.temu.service.ads.AdsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

/** 管理后台 Temu Ads 接口。 */
@Tag(name = "管理后台 - Temu Ads")
@RestController
@RequestMapping("/temu/ads")
@Validated
public class AdsController {

    @Resource
    private AdsService adsService;

    /** 查询商品推荐 ROAS。@param request ROAS 预测参数 @return Temu 官方响应 */
    @PostMapping("/roas/predict")
    @Operation(summary = "查询 Temu Ads 推荐 ROAS")
    @PreAuthorize("@ss.hasPermission('temu:ads:query')")
    public JsonNode predictRoas(@Valid @RequestBody AdsRoasPredReqVO request) { return adsService.predictRoas(request); }

    /** 查询店铺广告报表。@param request 店铺报表参数 @return Temu 官方响应 */
    @PostMapping("/reports/mall")
    @Operation(summary = "查询 Temu Ads 店铺报表")
    @PreAuthorize("@ss.hasPermission('temu:ads:query')")
    public JsonNode getMallReport(@Valid @RequestBody AdsMallReportReqVO request) { return adsService.getMallReport(request); }

    /** 查询商品广告报表。@param request 商品报表参数 @return Temu 官方响应 */
    @PostMapping("/reports/goods")
    @Operation(summary = "查询 Temu Ads 商品报表")
    @PreAuthorize("@ss.hasPermission('temu:ads:query')")
    public JsonNode getGoodsReport(@Valid @RequestBody AdsGoodsReportReqVO request) { return adsService.getGoodsReport(request); }

    /** 创建商品广告。@param request 广告创建参数 @return Temu 官方响应 */
    @PostMapping("/create")
    @Operation(summary = "创建 Temu Ads 广告")
    @PreAuthorize("@ss.hasPermission('temu:ads:create')")
    public JsonNode createAds(@Valid @RequestBody AdsCreateReqVO request) { return adsService.createAds(request); }

    /** 查询广告详情。@param request 广告详情商品参数 @return Temu 官方响应 */
    @PostMapping("/details")
    @Operation(summary = "查询 Temu Ads 广告详情")
    @PreAuthorize("@ss.hasPermission('temu:ads:query')")
    public JsonNode getAdDetails(@Valid @RequestBody AdsGoodsListReqVO request) { return adsService.getAdDetails(request); }

    /** 查询广告操作日志。@param request 广告日志参数 @return Temu 官方响应 */
    @PostMapping("/logs")
    @Operation(summary = "查询 Temu Ads 广告日志")
    @PreAuthorize("@ss.hasPermission('temu:ads:query')")
    public JsonNode getAdLogs(@Valid @RequestBody AdsLogQueryReqVO request) { return adsService.getAdLogs(request); }

    /** 查询商品广告创建资格。@param request 商品可创建广告参数 @return Temu 官方响应 */
    @PostMapping("/goods/creatable")
    @Operation(summary = "查询商品是否可创建 Temu Ads 广告")
    @PreAuthorize("@ss.hasPermission('temu:ads:query')")
    public JsonNode getGoodsCreatability(@Valid @RequestBody AdsGoodsListReqVO request) {
        return adsService.getGoodsCreatability(request);
    }

    /** 修改广告状态、预算或 ROAS。@param request 广告修改参数 @return Temu 官方响应 */
    @PostMapping("/modify")
    @Operation(summary = "修改 Temu Ads 广告")
    @PreAuthorize("@ss.hasPermission('temu:ads:update')")
    public JsonNode modifyAd(@Valid @RequestBody AdsModifyReqVO request) { return adsService.modifyAd(request); }
}
