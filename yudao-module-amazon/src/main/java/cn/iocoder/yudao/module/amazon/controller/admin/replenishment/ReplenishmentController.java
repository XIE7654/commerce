package cn.iocoder.yudao.module.amazon.controller.admin.replenishment;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.replenishment.vo.ReplenishmentReqVO;
import cn.iocoder.yudao.module.amazon.service.replenishment.ReplenishmentService;
import jakarta.annotation.Resource; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.Map;
/** Amazon Replenishment 管理接口。 */
@RestController @RequestMapping("/amazon/replenishment") public class ReplenishmentController {
 @Resource private ReplenishmentService service;
 /** 查询销售伙伴补货指标。 */ @PostMapping("/selling-partner-metrics/search") @PreAuthorize("@ss.hasPermission('amazon:replenishment:query')") public CommonResult<Map<String,Object>> sellingPartnerMetrics(@Valid @RequestBody ReplenishmentReqVO r){return CommonResult.success(service.getSellingPartnerMetrics(r));}
 /** 查询报价补货指标。 */ @PostMapping("/offer-metrics/search") @PreAuthorize("@ss.hasPermission('amazon:replenishment:query')") public CommonResult<Map<String,Object>> offerMetrics(@Valid @RequestBody ReplenishmentReqVO r){return CommonResult.success(service.listOfferMetrics(r));}
 /** 查询补货报价。 */ @PostMapping("/offers/search") @PreAuthorize("@ss.hasPermission('amazon:replenishment:query')") public CommonResult<Map<String,Object>> offers(@Valid @RequestBody ReplenishmentReqVO r){return CommonResult.success(service.listOffers(r));}
}
