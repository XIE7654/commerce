package cn.iocoder.yudao.module.temu.service.ads;

import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsCreateReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsGoodsListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsGoodsReportReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsLogQueryReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsMallReportReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsModifyReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.ads.vo.AdsRoasPredReqVO;
import tools.jackson.databind.JsonNode;

/** Temu Ads 业务 Service。 */
public interface AdsService {
    /** 查询商品推荐 ROAS；@param request ROAS 预测参数 @return Temu 官方响应 */
    JsonNode predictRoas(AdsRoasPredReqVO request);
    /** 查询店铺广告报表；@param request 店铺报表参数 @return Temu 官方响应 */
    JsonNode getMallReport(AdsMallReportReqVO request);
    /** 查询商品广告报表；@param request 商品报表参数 @return Temu 官方响应 */
    JsonNode getGoodsReport(AdsGoodsReportReqVO request);
    /** 创建商品广告；@param request 广告创建参数 @return Temu 官方响应 */
    JsonNode createAds(AdsCreateReqVO request);
    /** 查询广告详情；@param request 广告详情商品参数 @return Temu 官方响应 */
    JsonNode getAdDetails(AdsGoodsListReqVO request);
    /** 查询广告操作日志；@param request 广告日志参数 @return Temu 官方响应 */
    JsonNode getAdLogs(AdsLogQueryReqVO request);
    /** 查询商品广告创建资格；@param request 商品可创建广告参数 @return Temu 官方响应 */
    JsonNode getGoodsCreatability(AdsGoodsListReqVO request);
    /** 修改广告状态、预算或 ROAS；@param request 广告修改参数 @return Temu 官方响应 */
    JsonNode modifyAd(AdsModifyReqVO request);
}
