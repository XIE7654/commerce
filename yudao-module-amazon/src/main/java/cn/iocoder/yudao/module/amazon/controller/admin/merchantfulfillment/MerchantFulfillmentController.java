package cn.iocoder.yudao.module.amazon.controller.admin.merchantfulfillment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo.AmazonSpApiReqVO;
import cn.iocoder.yudao.module.amazon.service.merchantfulfillment.MerchantFulfillmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Amazon Merchant Fulfillment 管理接口。 */
@Tag(name = "管理后台 - Amazon Merchant Fulfillment")
@RestController
@RequestMapping("/amazon/merchant-fulfillment")
@Validated
public class MerchantFulfillmentController {

    @Resource
    private MerchantFulfillmentService merchantFulfillmentService;

    /**
     * 调用参考模型白名单内的 Merchant Fulfillment v0 操作。
     *
     * @param operation Amazon 模型定义的 operationId
     * @param request 店铺、路径参数、查询参数及请求体
     * @return Amazon 原始 JSON 响应
     */
    @PostMapping("/operations/{operation}")
    @Operation(summary = "调用 Amazon Merchant Fulfillment 操作")
    @PreAuthorize("@ss.hasPermission('amazon:merchant-fulfillment:manage')")
    public CommonResult<Map<String, Object>> invoke(@PathVariable String operation,
                                                     @Valid @RequestBody AmazonSpApiReqVO request) {
        return CommonResult.success(merchantFulfillmentService.invoke(operation, request));
    }
}
