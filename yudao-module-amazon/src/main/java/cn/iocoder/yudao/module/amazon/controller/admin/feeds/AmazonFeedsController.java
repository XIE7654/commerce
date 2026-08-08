package cn.iocoder.yudao.module.amazon.controller.admin.feeds;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.feeds.vo.*;
import cn.iocoder.yudao.module.amazon.service.feeds.AmazonFeedsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Amazon Feeds 管理接口。 */
@Tag(name = "管理后台 - Amazon Feeds")
@RestController
@RequestMapping("/amazon/feeds")
@Validated
public class AmazonFeedsController {
    @Resource private AmazonFeedsService amazonFeedsService;
    /** 查询 Feed 列表。 */
    @PostMapping("/list") @Operation(summary = "查询 Amazon Feed 列表") @PreAuthorize("@ss.hasPermission('amazon:feeds:query')")
    public CommonResult<Map<String,Object>> getFeeds(@Valid @RequestBody AmazonFeedsListReqVO request){return CommonResult.success(amazonFeedsService.getFeeds(request));}
    /** 创建 Feed。 */
    @PostMapping("/create") @Operation(summary = "创建 Amazon Feed") @PreAuthorize("@ss.hasPermission('amazon:feeds:create')")
    public CommonResult<Map<String,Object>> createFeed(@Valid @RequestBody AmazonFeedCreateReqVO request){return CommonResult.success(amazonFeedsService.createFeed(request));}
    /** 查询 Feed 详情。 */
    @PostMapping("/detail") @Operation(summary = "查询 Amazon Feed 详情") @PreAuthorize("@ss.hasPermission('amazon:feeds:query')")
    public CommonResult<Map<String,Object>> getFeed(@Valid @RequestBody AmazonFeedIdReqVO request){return CommonResult.success(amazonFeedsService.getFeed(request));}
    /** 取消 Feed。 */
    @PostMapping("/cancel") @Operation(summary = "取消 Amazon Feed") @PreAuthorize("@ss.hasPermission('amazon:feeds:update')")
    public CommonResult<Boolean> cancelFeed(@Valid @RequestBody AmazonFeedIdReqVO request){amazonFeedsService.cancelFeed(request);return CommonResult.success(true);}
    /** 创建 Feed Document 上传凭证。 */
    @PostMapping("/document/create") @Operation(summary = "创建 Amazon Feed Document") @PreAuthorize("@ss.hasPermission('amazon:feeds:create')")
    public CommonResult<Map<String,Object>> createFeedDocument(@Valid @RequestBody AmazonFeedDocumentCreateReqVO request){return CommonResult.success(amazonFeedsService.createFeedDocument(request));}
    /** 查询 Feed Document 元数据。 */
    @PostMapping("/document/detail") @Operation(summary = "查询 Amazon Feed Document") @PreAuthorize("@ss.hasPermission('amazon:feeds:query')")
    public CommonResult<Map<String,Object>> getFeedDocument(@Valid @RequestBody AmazonFeedIdReqVO request){return CommonResult.success(amazonFeedsService.getFeedDocument(request));}
}
