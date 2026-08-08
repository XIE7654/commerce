package cn.iocoder.yudao.module.amazon.controller.admin.supplysources;
import cn.iocoder.yudao.framework.common.pojo.CommonResult; import cn.iocoder.yudao.module.amazon.controller.admin.supplysources.vo.SupplySourcesReqVO; import cn.iocoder.yudao.module.amazon.service.supplysources.SupplySourcesService; import jakarta.annotation.Resource; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.Map;
/** Amazon Supply Sources 管理接口。 */
@RestController @RequestMapping("/amazon/supply-sources") public class SupplySourcesController {
 @Resource private SupplySourcesService service;
 /** 查询供货源。 */ @PostMapping("/list") @PreAuthorize("@ss.hasPermission('amazon:supply-sources:query')") public CommonResult<Map<String,Object>> list(@Valid @RequestBody SupplySourcesReqVO r){return CommonResult.success(service.getSupplySources(r));}
 /** 创建供货源。 */ @PostMapping("/create") @PreAuthorize("@ss.hasPermission('amazon:supply-sources:update')") public CommonResult<Map<String,Object>> create(@Valid @RequestBody SupplySourcesReqVO r){return CommonResult.success(service.createSupplySource(r));}
 /** 查询供货源详情。 */ @PostMapping("/get") @PreAuthorize("@ss.hasPermission('amazon:supply-sources:query')") public CommonResult<Map<String,Object>> get(@Valid @RequestBody SupplySourcesReqVO r){return CommonResult.success(service.getSupplySource(r));}
 /** 更新供货源。 */ @PostMapping("/update") @PreAuthorize("@ss.hasPermission('amazon:supply-sources:update')") public CommonResult<Map<String,Object>> update(@Valid @RequestBody SupplySourcesReqVO r){return CommonResult.success(service.updateSupplySource(r));}
 /** 归档供货源。 */ @PostMapping("/archive") @PreAuthorize("@ss.hasPermission('amazon:supply-sources:update')") public CommonResult<Map<String,Object>> archive(@Valid @RequestBody SupplySourcesReqVO r){return CommonResult.success(service.archiveSupplySource(r));}
 /** 更新供货源状态。 */ @PostMapping("/status/update") @PreAuthorize("@ss.hasPermission('amazon:supply-sources:update')") public CommonResult<Map<String,Object>> status(@Valid @RequestBody SupplySourcesReqVO r){return CommonResult.success(service.updateSupplySourceStatus(r));}
}
