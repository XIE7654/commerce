package cn.iocoder.yudao.module.temu.controller.admin.refundandreturn;

import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnAftersalesListReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnOrderReqVO;
import cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo.RefundAndReturnParentAftersalesListReqVO;
import cn.iocoder.yudao.module.temu.service.refundandreturn.RefundAndReturnService;
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
import tools.jackson.databind.JsonNode;

/**
 * 管理后台 Refund And Return 退款退货接口。
 */
@Tag(name = "管理后台 - Refund And Return")
@RestController
@RequestMapping("/temu/refund-and-return")
@Validated
public class RefundAndReturnController {

    @Resource
    private RefundAndReturnService refundAndReturnService;

    /**
     * 查询 Temu 父售后单列表。
     *
     * @param request 父售后单分页与筛选参数
     * @return Temu 官方父售后单列表响应
     */
    @PostMapping("/parent-aftersales-orders/list")
    @Operation(summary = "查询 Temu 父售后单列表")
    @PreAuthorize("@ss.hasPermission('temu:refund-and-return:query')")
    public JsonNode getParentAftersaleOrderList(@Valid @RequestBody RefundAndReturnParentAftersalesListReqVO request) {
        return refundAndReturnService.getParentAftersaleOrderList(request);
    }

    /**
     * 查询 Temu 售后单列表。
     *
     * @param request 售后单分页与父售后单筛选参数
     * @return Temu 官方售后单列表响应
     */
    @PostMapping("/aftersales-orders/list")
    @Operation(summary = "查询 Temu 售后单列表")
    @PreAuthorize("@ss.hasPermission('temu:refund-and-return:query')")
    public JsonNode getAftersaleOrderList(@Valid @RequestBody RefundAndReturnAftersalesListReqVO request) {
        return refundAndReturnService.getAftersaleOrderList(request);
    }

    /**
     * 查询 Temu 退货单信息。
     *
     * @param request 父售后单与售后单查询参数
     * @return Temu 官方退货单响应
     */
    @PostMapping("/return-orders/list")
    @Operation(summary = "查询 Temu 退货单信息")
    @PreAuthorize("@ss.hasPermission('temu:refund-and-return:query')")
    public JsonNode getReturnOrderList(@Valid @RequestBody RefundAndReturnOrderReqVO request) {
        return refundAndReturnService.getReturnOrderList(request);
    }
}
