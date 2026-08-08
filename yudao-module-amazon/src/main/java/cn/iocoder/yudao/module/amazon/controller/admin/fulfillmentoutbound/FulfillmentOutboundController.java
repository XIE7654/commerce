package cn.iocoder.yudao.module.amazon.controller.admin.fulfillmentoutbound;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.fulfillment.vo.AmazonFulfillmentApiReqVO;
import cn.iocoder.yudao.module.amazon.service.fulfillmentoutbound.FulfillmentOutboundService;
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

/** Amazon Fulfillment Outbound 管理接口。 */
@Tag(name = "管理后台 - Amazon Fulfillment Outbound")
@RestController
@RequestMapping("/amazon/fulfillment-outbound")
@Validated
public class FulfillmentOutboundController {

    @Resource
    private FulfillmentOutboundService fulfillmentOutboundService;

    /** 调用文档白名单内的 Fulfillment Outbound v2020-07-01 或 v2026-07-04 operation。 */
    @PostMapping("/operations/{operation}")
    @Operation(summary = "调用 Amazon Fulfillment Outbound 操作")
    @PreAuthorize("@ss.hasPermission('amazon:fulfillment-outbound:manage')")
    public CommonResult<Map<String, Object>> invoke(@PathVariable String operation,
                                                     @Valid @RequestBody AmazonFulfillmentApiReqVO request) {
        return CommonResult.success(fulfillmentOutboundService.invoke(operation, request));
    }
}
