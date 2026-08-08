package cn.iocoder.yudao.module.amazon.controller.admin.vehicles;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.vehicles.vo.VehiclesListReqVO;
import cn.iocoder.yudao.module.amazon.service.vehicles.VehiclesService;
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

/** Amazon Vehicles 管理接口。 */
@Tag(name = "管理后台 - Amazon Vehicles")
@RestController
@RequestMapping("/amazon/vehicles")
@Validated
public class VehiclesController {

    @Resource
    private VehiclesService vehiclesService;

    /**
     * 分页查询 Amazon 车型目录。
     *
     * @param request 店铺、Marketplace、车型类型和可选分页条件
     * @return Amazon 返回的车型目录数据
     */
    @PostMapping("/list")
    @Operation(summary = "查询 Amazon 车型目录")
    @PreAuthorize("@ss.hasPermission('amazon:vehicles:query')")
    public CommonResult<Map<String, Object>> getVehicles(@Valid @RequestBody VehiclesListReqVO request) {
        return CommonResult.success(vehiclesService.getVehicles(request));
    }
}
