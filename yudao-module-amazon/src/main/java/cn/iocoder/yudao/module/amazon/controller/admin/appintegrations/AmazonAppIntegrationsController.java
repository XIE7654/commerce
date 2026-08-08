package cn.iocoder.yudao.module.amazon.controller.admin.appintegrations;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonCreateNotificationReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonDeleteNotificationsReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.appintegrations.vo.AmazonRecordActionFeedbackReqVO;
import cn.iocoder.yudao.module.amazon.service.appintegrations.AmazonAppIntegrationsService;
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

import java.util.Map;

/** Amazon Application Integrations 管理接口。 */
@Tag(name = "管理后台 - Amazon Application Integrations")
@RestController
@RequestMapping("/amazon/app-integrations")
@Validated
public class AmazonAppIntegrationsController {

    @Resource
    private AmazonAppIntegrationsService amazonAppIntegrationsService;

    /** 创建 Seller Central 通知。 */
    @PostMapping("/notification/create")
    @Operation(summary = "创建 Amazon Seller Central 通知")
    @PreAuthorize("@ss.hasPermission('amazon:app-integrations:create')")
    public CommonResult<Map<String, Object>> createNotification(@Valid @RequestBody AmazonCreateNotificationReqVO request) {
        return CommonResult.success(amazonAppIntegrationsService.createNotification(request));
    }

    /** 删除应用已发送的通知。 */
    @PostMapping("/notification/delete")
    @Operation(summary = "删除 Amazon Seller Central 通知")
    @PreAuthorize("@ss.hasPermission('amazon:app-integrations:delete')")
    public CommonResult<Boolean> deleteNotifications(@Valid @RequestBody AmazonDeleteNotificationsReqVO request) {
        amazonAppIntegrationsService.deleteNotifications(request);
        return CommonResult.success(true);
    }

    /** 记录卖家已完成通知关联操作。 */
    @PostMapping("/notification/feedback")
    @Operation(summary = "记录 Amazon 通知操作反馈")
    @PreAuthorize("@ss.hasPermission('amazon:app-integrations:update')")
    public CommonResult<Boolean> recordActionFeedback(@Valid @RequestBody AmazonRecordActionFeedbackReqVO request) {
        amazonAppIntegrationsService.recordActionFeedback(request);
        return CommonResult.success(true);
    }
}
