package cn.iocoder.yudao.module.temu.controller.admin.pricing;

import cn.iocoder.yudao.module.temu.controller.admin.pricing.vo.*;
import cn.iocoder.yudao.module.temu.service.pricing.PricingService;
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

/** 管理后台 Temu Pricing 接口。 */
@Tag(name = "管理后台 - Temu Pricing")
@RestController
@RequestMapping("/temu/pricing")
@Validated
public class PricingController {
    @Resource private PricingService pricingService;

    /** 查询商品 SKU 供货价。 @param request 查询参数 @return Temu 官方响应 */
    @PostMapping("/goods/prices") @Operation(summary = "查询 Temu 商品 SKU 供货价") @PreAuthorize("@ss.hasPermission('temu:pricing:query')")
    public JsonNode getGoodsPriceList(@Valid @RequestBody PricingGoodsPriceListReqVO request) { return pricingService.getGoodsPriceList(request); }
    /** 修改商品 SKU 供货价。 @param request 调价参数 @return Temu 官方响应 */
    @PostMapping("/goods/prices/update") @Operation(summary = "修改 Temu 商品 SKU 供货价") @PreAuthorize("@ss.hasPermission('temu:pricing:update')")
    public JsonNode updateGoodsPrice(@Valid @RequestBody PricingUpdateGoodsPriceReqVO request) { return pricingService.updateGoodsPrice(request); }
    /** 查询调价单。 @param request 查询参数 @return Temu 官方响应 */
    @PostMapping("/price-orders/page") @Operation(summary = "查询 Temu 调价单") @PreAuthorize("@ss.hasPermission('temu:pricing:query')")
    public JsonNode getGoodsPriceOrder(@Valid @RequestBody PricingPriceOrderQueryReqVO request) { return pricingService.getGoodsPriceOrder(request); }
    /** 接受调价单。 @param request 接受参数 @return Temu 官方响应 */
    @PostMapping("/price-orders/accept") @Operation(summary = "接受 Temu 调价单") @PreAuthorize("@ss.hasPermission('temu:pricing:update')")
    public JsonNode acceptGoodsPriceOrder(@Valid @RequestBody PricingPriceOrderAcceptReqVO request) { return pricingService.acceptGoodsPriceOrder(request); }
    /** 议价调价单。 @param request 议价参数 @return Temu 官方响应 */
    @PostMapping("/price-orders/negotiate") @Operation(summary = "议价 Temu 调价单") @PreAuthorize("@ss.hasPermission('temu:pricing:update')")
    public JsonNode negotiateGoodsPriceOrder(@Valid @RequestBody PricingPriceOrderNegotiateReqVO request) { return pricingService.negotiateGoodsPriceOrder(request); }
    /** 拒绝调价单。 @param request 拒绝参数 @return Temu 官方响应 */
    @PostMapping("/price-orders/reject") @Operation(summary = "拒绝 Temu 调价单") @PreAuthorize("@ss.hasPermission('temu:pricing:update')")
    public JsonNode rejectGoodsPriceOrder(@Valid @RequestBody PricingPriceOrderRejectReqVO request) { return pricingService.rejectGoodsPriceOrder(request); }
    /** 创建价格申诉单。 @param request 申诉参数 @return Temu 官方响应 */
    @PostMapping("/appeal-orders/create") @Operation(summary = "创建 Temu 价格申诉单") @PreAuthorize("@ss.hasPermission('temu:pricing:create')")
    public JsonNode createGoodsAppealPriceOrder(@Valid @RequestBody PricingAppealOrderCreateReqVO request) { return pricingService.createGoodsAppealPriceOrder(request); }
    /** 查询价格申诉单。 @param request 查询参数 @return Temu 官方响应 */
    @PostMapping("/appeal-orders/query") @Operation(summary = "查询 Temu 价格申诉单") @PreAuthorize("@ss.hasPermission('temu:pricing:query')")
    public JsonNode getGoodsAppealPriceOrder(@Valid @RequestBody PricingAppealOrderQueryReqVO request) { return pricingService.getGoodsAppealPriceOrder(request); }
    /** 查询价格申诉记录。 @param request 查询参数 @return Temu 官方响应 */
    @PostMapping("/appeal-orders/records") @Operation(summary = "查询 Temu 价格申诉记录") @PreAuthorize("@ss.hasPermission('temu:pricing:query')")
    public JsonNode getGoodsAppealPriceOrderRecord(@Valid @RequestBody PricingAppealOrderRecordQueryReqVO request) { return pricingService.getGoodsAppealPriceOrderRecord(request); }
    /** 查询商品推荐价。 @param request 查询参数 @return Temu 官方响应 */
    @PostMapping("/recommended-prices/query") @Operation(summary = "查询 Temu 商品推荐价") @PreAuthorize("@ss.hasPermission('temu:pricing:query')")
    public JsonNode getGoodsRecommendedPrice(@Valid @RequestBody PricingRecommendedPriceQueryReqVO request) { return pricingService.getGoodsRecommendedPrice(request); }
    /** 查询订单金额。 @param request 查询参数 @return Temu 官方响应 */
    @PostMapping("/orders/amount") @Operation(summary = "查询 Temu 订单金额") @PreAuthorize("@ss.hasPermission('temu:pricing:query')")
    public JsonNode getOrderAmount(@Valid @RequestBody PricingOrderAmountQueryReqVO request) { return pricingService.getOrderAmount(request); }
}
