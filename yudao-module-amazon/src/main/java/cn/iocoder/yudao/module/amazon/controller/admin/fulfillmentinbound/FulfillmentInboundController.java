package cn.iocoder.yudao.module.amazon.controller.admin.fulfillmentinbound;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.service.fulfillmentinbound.FulfillmentInboundService;
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

/** Amazon Fulfillment Inbound 管理接口。 */
@Tag(name = "管理后台 - Amazon Fulfillment Inbound")
@RestController
@RequestMapping("/amazon/fulfillment-inbound")
@Validated
public class FulfillmentInboundController {

    @Resource
    private FulfillmentInboundService fulfillmentInboundService;

    /** 调用文档白名单内的 Fulfillment Inbound v0 或 v2024-03-20 operation。 */
    @PostMapping("/operations/{operation}")
    @Operation(summary = "调用 Amazon Fulfillment Inbound 操作")
    @PreAuthorize("@ss.hasPermission('amazon:fulfillment-inbound:manage')")
    public CommonResult<Map<String, Object>> invoke(@PathVariable String operation,
                                                     @Valid @RequestBody AmazonFulfillmentApiReqVO request) {
        return CommonResult.success(fulfillmentInboundService.invoke(operation, request));
    }
}
