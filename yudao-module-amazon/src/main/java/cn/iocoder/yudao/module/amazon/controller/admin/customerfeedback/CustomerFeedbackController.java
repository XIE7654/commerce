package cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo.CustomerFeedbackBrowseNodeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo.CustomerFeedbackItemReqVO;
import cn.iocoder.yudao.module.amazon.service.customerfeedback.CustomerFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Customer Feedback 管理接口。 */
@Tag(name = "管理后台 - Amazon Customer Feedback") @RestController @RequestMapping("/amazon/customer-feedback") @Validated
public class CustomerFeedbackController {
    @Resource private CustomerFeedbackService customerFeedbackService;
    /** 查询商品评论主题。 */ @PostMapping("/items/review-topics") @Operation(summary = "查询商品评论主题") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getItemReviewTopics(@Valid @RequestBody CustomerFeedbackItemReqVO request) { return CommonResult.success(customerFeedbackService.getItemReviewTopics(request)); }
    /** 查询商品所属浏览节点。 */ @PostMapping("/items/browse-node") @Operation(summary = "查询商品浏览节点") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getItemBrowseNode(@Valid @RequestBody CustomerFeedbackItemReqVO request) { return CommonResult.success(customerFeedbackService.getItemBrowseNode(request)); }
    /** 查询浏览节点评论主题。 */ @PostMapping("/browse-nodes/review-topics") @Operation(summary = "查询浏览节点评论主题") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getBrowseNodeReviewTopics(@Valid @RequestBody CustomerFeedbackBrowseNodeReqVO request) { return CommonResult.success(customerFeedbackService.getBrowseNodeReviewTopics(request)); }
    /** 查询商品评论趋势。 */ @PostMapping("/items/review-trends") @Operation(summary = "查询商品评论趋势") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getItemReviewTrends(@Valid @RequestBody CustomerFeedbackItemReqVO request) { return CommonResult.success(customerFeedbackService.getItemReviewTrends(request)); }
    /** 查询浏览节点评论趋势。 */ @PostMapping("/browse-nodes/review-trends") @Operation(summary = "查询浏览节点评论趋势") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getBrowseNodeReviewTrends(@Valid @RequestBody CustomerFeedbackBrowseNodeReqVO request) { return CommonResult.success(customerFeedbackService.getBrowseNodeReviewTrends(request)); }
    /** 查询浏览节点退货主题。 */ @PostMapping("/browse-nodes/return-topics") @Operation(summary = "查询浏览节点退货主题") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getBrowseNodeReturnTopics(@Valid @RequestBody CustomerFeedbackBrowseNodeReqVO request) { return CommonResult.success(customerFeedbackService.getBrowseNodeReturnTopics(request)); }
    /** 查询浏览节点退货趋势。 */ @PostMapping("/browse-nodes/return-trends") @Operation(summary = "查询浏览节点退货趋势") @PreAuthorize("@ss.hasPermission('amazon:customer-feedback:query')") public CommonResult<Map<String, Object>> getBrowseNodeReturnTrends(@Valid @RequestBody CustomerFeedbackBrowseNodeReqVO request) { return CommonResult.success(customerFeedbackService.getBrowseNodeReturnTrends(request)); }
}
