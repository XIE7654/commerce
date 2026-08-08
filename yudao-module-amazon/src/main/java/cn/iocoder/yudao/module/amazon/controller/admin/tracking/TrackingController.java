package cn.iocoder.yudao.module.amazon.controller.admin.tracking;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.tracking.vo.TrackingShipmentReqVO;
import cn.iocoder.yudao.module.amazon.service.tracking.TrackingService;
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

/** Amazon Tracking 管理接口。 */
@Tag(name = "管理后台 - Amazon Tracking")
@RestController
@RequestMapping("/amazon/tracking")
@Validated
public class TrackingController {

    @Resource
    private TrackingService trackingService;

    /**
     * 查询货件物流轨迹。
     *
     * @param request 店铺、站点和至少一个货件标识
     * @return Amazon 返回的物流轨迹数据
     */
    @PostMapping("/shipments/track")
    @Operation(summary = "查询 Amazon 货件物流轨迹")
    @PreAuthorize("@ss.hasPermission('amazon:tracking:query')")
    public CommonResult<Map<String, Object>> getShipmentTracking(@Valid @RequestBody TrackingShipmentReqVO request) {
        return CommonResult.success(trackingService.getShipmentTracking(request));
    }
}
