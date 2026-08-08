package cn.iocoder.yudao.module.amazon.controller.admin.datakiosk;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo.*;
import cn.iocoder.yudao.module.amazon.service.datakiosk.DataKioskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Data Kiosk 管理接口。 */
@Tag(name = "管理后台 - Amazon Data Kiosk") @RestController @RequestMapping("/amazon/data-kiosk") @Validated
public class DataKioskController {
    @Resource private DataKioskService dataKioskService;
    /** 创建 Data Kiosk GraphQL 查询任务。 */ @PostMapping("/queries") @Operation(summary = "创建 Data Kiosk 查询") @PreAuthorize("@ss.hasPermission('amazon:data-kiosk:query')") public CommonResult<Map<String, Object>> createQuery(@Valid @RequestBody DataKioskCreateQueryReqVO request) { return CommonResult.success(dataKioskService.createQuery(request)); }
    /** 查询 Data Kiosk 任务列表。 */ @PostMapping("/queries/list") @Operation(summary = "查询 Data Kiosk 任务列表") @PreAuthorize("@ss.hasPermission('amazon:data-kiosk:query')") public CommonResult<Map<String, Object>> getQueries(@Valid @RequestBody DataKioskQueriesReqVO request) { return CommonResult.success(dataKioskService.getQueries(request)); }
    /** 查询一个 Data Kiosk 任务。 */ @PostMapping("/queries/get") @Operation(summary = "查询 Data Kiosk 任务详情") @PreAuthorize("@ss.hasPermission('amazon:data-kiosk:query')") public CommonResult<Map<String, Object>> getQuery(@Valid @RequestBody DataKioskQueryIdReqVO request) { return CommonResult.success(dataKioskService.getQuery(request)); }
    /** 取消未完成的 Data Kiosk 任务。 */ @PostMapping("/queries/cancel") @Operation(summary = "取消 Data Kiosk 查询任务") @PreAuthorize("@ss.hasPermission('amazon:data-kiosk:query')") public CommonResult<Map<String, Object>> cancelQuery(@Valid @RequestBody DataKioskQueryIdReqVO request) { return CommonResult.success(dataKioskService.cancelQuery(request)); }
    /** 获取 Data Kiosk 文档下载信息。 */ @PostMapping("/documents/get") @Operation(summary = "查询 Data Kiosk 文档") @PreAuthorize("@ss.hasPermission('amazon:data-kiosk:query')") public CommonResult<Map<String, Object>> getDocument(@Valid @RequestBody DataKioskDocumentIdReqVO request) { return CommonResult.success(dataKioskService.getDocument(request)); }
}
