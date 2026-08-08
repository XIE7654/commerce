package cn.iocoder.yudao.module.amazon.controller.admin.fbainventory;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.fbainventory.vo.FbaInventorySummariesReqVO;
import cn.iocoder.yudao.module.amazon.service.fbainventory.FbaInventoryService;
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

/**
 * Amazon FBA Inventory 管理接口。
 */
@Tag(name = "管理后台 - Amazon FBA Inventory")
@RestController
@RequestMapping("/amazon/fba-inventory")
@Validated
public class FbaInventoryController {

    @Resource
    private FbaInventoryService fbaInventoryService;

    /**
     * 查询指定店铺和站点的 FBA 库存摘要，不保存库存业务数据。
     *
     * @param request 店铺、国家代码及 FBA Inventory 查询条件
     * @return Amazon FBA Inventory 原始响应
     */
    @PostMapping("/summaries")
    @Operation(summary = "查询 Amazon FBA 库存摘要")
    @PreAuthorize("@ss.hasPermission('amazon:fba-inventory:query')")
    public CommonResult<Map<String, Object>> getInventorySummaries(
            @Valid @RequestBody FbaInventorySummariesReqVO request) {
        return CommonResult.success(fbaInventoryService.getInventorySummaries(request));
    }
}
