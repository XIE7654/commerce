package cn.iocoder.yudao.module.amazon.controller.admin.easyship;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.easyship.vo.EasyShipRequestVO;
import cn.iocoder.yudao.module.amazon.service.easyship.EasyShipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Amazon Easy Ship 管理接口。 */
@Tag(name = "管理后台 - Amazon Easy Ship") @RestController @RequestMapping("/amazon/easy-ship") @Validated
public class EasyShipController {
    @Resource private EasyShipService easyShipService;
    /** 查询可用交接时间段。 */
    @PostMapping("/handover-slots") @Operation(summary = "查询 Easy Ship 交接时间段") @PreAuthorize("@ss.hasPermission('amazon:easy-ship:query')")
    public CommonResult<Map<String, Object>> listHandoverSlots(@Valid @RequestBody EasyShipRequestVO request) { return call(request, "listHandoverSlots", "POST", "/timeSlot"); }
    /** 查询已安排的包裹。 */
    @PostMapping("/package/detail") @Operation(summary = "查询 Easy Ship 包裹") @PreAuthorize("@ss.hasPermission('amazon:easy-ship:query')")
    public CommonResult<Map<String, Object>> getScheduledPackage(@Valid @RequestBody EasyShipRequestVO request) { return call(request, "getScheduledPackage", "GET", "/package"); }
    /** 创建单个已安排包裹。 */
    @PostMapping("/package") @Operation(summary = "创建 Easy Ship 包裹") @PreAuthorize("@ss.hasPermission('amazon:easy-ship:create')")
    public CommonResult<Map<String, Object>> createScheduledPackage(@Valid @RequestBody EasyShipRequestVO request) { return call(request, "createScheduledPackage", "POST", "/package"); }
    /** 更新已安排包裹。 */
    @PostMapping("/package/update") @Operation(summary = "更新 Easy Ship 包裹") @PreAuthorize("@ss.hasPermission('amazon:easy-ship:update')")
    public CommonResult<Map<String, Object>> updateScheduledPackages(@Valid @RequestBody EasyShipRequestVO request) { return call(request, "updateScheduledPackages", "PATCH", "/package"); }
    /** 批量创建已安排包裹。 */
    @PostMapping("/packages/bulk") @Operation(summary = "批量创建 Easy Ship 包裹") @PreAuthorize("@ss.hasPermission('amazon:easy-ship:create')")
    public CommonResult<Map<String, Object>> createScheduledPackageBulk(@Valid @RequestBody EasyShipRequestVO request) { return call(request, "createScheduledPackageBulk", "POST", "/packages/bulk"); }
    /** 将受校验的请求转交 Service。 */
    private CommonResult<Map<String, Object>> call(EasyShipRequestVO request, String operation, String method, String path) { return CommonResult.success(easyShipService.invoke(request, operation, method, path)); }
}
