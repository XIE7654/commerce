package cn.iocoder.yudao.module.amazon.controller.admin.uploads;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.uploads.vo.UploadsCreateDestinationReqVO;
import cn.iocoder.yudao.module.amazon.service.uploads.UploadsService;
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

/** Amazon Uploads 管理接口。 */
@Tag(name = "管理后台 - Amazon Uploads")
@RestController
@RequestMapping("/amazon/uploads")
@Validated
public class UploadsController {

    @Resource
    private UploadsService uploadsService;

    /**
     * 创建一次性文件上传目的地。
     *
     * @param request 店铺、资源路径、内容摘要及 Marketplace 参数
     * @return Amazon 返回的预签名上传地址和请求头
     */
    @PostMapping("/destinations/create")
    @Operation(summary = "创建 Amazon 文件上传目的地")
    @PreAuthorize("@ss.hasPermission('amazon:uploads:create')")
    public CommonResult<Map<String, Object>> createUploadDestination(@Valid @RequestBody UploadsCreateDestinationReqVO request) {
        return CommonResult.success(uploadsService.createUploadDestination(request));
    }
}
