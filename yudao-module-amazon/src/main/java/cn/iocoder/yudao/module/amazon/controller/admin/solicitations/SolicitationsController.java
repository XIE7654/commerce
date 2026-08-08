package cn.iocoder.yudao.module.amazon.controller.admin.solicitations;
import cn.iocoder.yudao.framework.common.pojo.CommonResult; import cn.iocoder.yudao.module.amazon.controller.admin.solicitations.vo.SolicitationsReqVO; import cn.iocoder.yudao.module.amazon.service.solicitations.SolicitationsService; import jakarta.annotation.Resource; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.Map;
/** Amazon Solicitations 管理接口。 */
@RestController @RequestMapping("/amazon/solicitations") public class SolicitationsController {
 @Resource private SolicitationsService service;
 /** 查询订单可用征集动作。 */ @PostMapping("/actions/get") @PreAuthorize("@ss.hasPermission('amazon:solicitations:query')") public CommonResult<Map<String,Object>> actions(@Valid @RequestBody SolicitationsReqVO r){return CommonResult.success(service.getSolicitationActionsForOrder(r));}
 /** 发起商品评论与卖家反馈征集。 */ @PostMapping("/product-review-and-seller-feedback/create") @PreAuthorize("@ss.hasPermission('amazon:solicitations:update')") public CommonResult<Map<String,Object>> create(@Valid @RequestBody SolicitationsReqVO r){return CommonResult.success(service.createProductReviewAndSellerFeedbackSolicitation(r));}
}
