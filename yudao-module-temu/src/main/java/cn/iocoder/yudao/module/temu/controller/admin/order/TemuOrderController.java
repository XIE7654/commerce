package cn.iocoder.yudao.module.temu.controller.admin.order;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.temu.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo.OrderManagementOrderListReqVO;
import cn.iocoder.yudao.module.temu.dal.dataobject.order.TemuOrderDO;
import cn.iocoder.yudao.module.temu.service.ordermanagement.OrderManagementService;
import cn.iocoder.yudao.module.temu.service.order.TemuOrderService;
import tools.jackson.databind.JsonNode;

@Tag(name = "管理后台 - Temu 订单")
@RestController
@RequestMapping("/temu/order")
@Validated
public class TemuOrderController {

    @Resource
    private TemuOrderService orderService;

    @Resource
    private OrderManagementService orderManagementService;

    /**
     * 从 Temu 拉取订单并同步当前页数据到本地订单表。
     *
     * @param request 订单状态、区域和分页同步参数
     * @return Temu 官方订单列表响应
     */
    @PostMapping("/sync")
    @Operation(summary = "同步 Temu 订单")
    @PreAuthorize("@ss.hasPermission('temu:order:update')")
    public JsonNode syncOrder(@Valid @RequestBody OrderManagementOrderListReqVO request) {
        return orderManagementService.syncOrderList(request);
    }

    @GetMapping("/get")
    @Operation(summary = "获得Temu 订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('temu:order:query')")
    public CommonResult<TemuOrderRespVO> getOrder(@RequestParam("id") Long id) {
        TemuOrderDO order = orderService.getOrder(id);
        return success(BeanUtils.toBean(order, TemuOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Temu 订单分页")
    @PreAuthorize("@ss.hasPermission('temu:order:query')")
    public CommonResult<PageResult<TemuOrderRespVO>> getOrderPage(@Valid TemuOrderPageReqVO pageReqVO) {
        PageResult<TemuOrderDO> pageResult = orderService.getOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TemuOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出Temu 订单 Excel")
    @PreAuthorize("@ss.hasPermission('temu:order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderExcel(@Valid TemuOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemuOrderDO> list = orderService.getOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "Temu 订单.xls", "数据", TemuOrderRespVO.class,
                        BeanUtils.toBean(list, TemuOrderRespVO.class));
    }

}
