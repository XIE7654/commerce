package cn.iocoder.yudao.module.amazon.controller.admin.applicationmanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.applicationmanagement.vo.AmazonApplicationManagementReqVO;
import cn.iocoder.yudao.module.amazon.service.applicationmanagement.AmazonApplicationManagementService;
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

/** Amazon Application Management 管理接口。 */
@Tag(name = "管理后台 - Amazon Application Management")
@RestController
@RequestMapping("/amazon/application-management")
@Validated
public class AmazonApplicationManagementController {

    @Resource
    private AmazonApplicationManagementService amazonApplicationManagementService;

    /** 轮换开发者应用的 Client Secret。 */
    @PostMapping("/client-secret/rotate")
    @Operation(summary = "轮换 Amazon 应用 Client Secret")
    @PreAuthorize("@ss.hasPermission('amazon:application-management:update')")
    public CommonResult<Boolean> rotateApplicationClientSecret(@Valid @RequestBody AmazonApplicationManagementReqVO request) {
        amazonApplicationManagementService.rotateApplicationClientSecret(request);
        return CommonResult.success(true);
    }
}
