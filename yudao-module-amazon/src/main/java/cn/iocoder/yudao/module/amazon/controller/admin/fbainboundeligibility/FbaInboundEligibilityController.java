package cn.iocoder.yudao.module.amazon.controller.admin.fbainboundeligibility;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.service.fbainboundeligibility.FbaInboundEligibilityService;
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

/** Amazon FBA Inbound Eligibility 管理接口。 */
@Tag(name = "管理后台 - Amazon FBA Inbound Eligibility")
@RestController
@RequestMapping("/amazon/fba-inbound-eligibility")
@Validated
public class FbaInboundEligibilityController {

    @Resource
    private FbaInboundEligibilityService fbaInboundEligibilityService;

    /** 查询商品在指定 FBA 入库计划中的资格预览。 */
    @PostMapping("/item-preview")
    @Operation(summary = "查询 Amazon FBA 入库资格预览")
    @PreAuthorize("@ss.hasPermission('amazon:fba-inbound-eligibility:query')")
    public CommonResult<Map<String, Object>> getItemEligibilityPreview(@Valid @RequestBody AmazonFulfillmentApiReqVO request) {
        return CommonResult.success(fbaInboundEligibilityService.getItemEligibilityPreview(request));
    }
}
