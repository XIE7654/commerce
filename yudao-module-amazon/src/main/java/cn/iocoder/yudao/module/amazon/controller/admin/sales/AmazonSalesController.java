package cn.iocoder.yudao.module.amazon.controller.admin.sales;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.amazon.controller.admin.sales.vo.AmazonSalesOrderMetricsReqVO;
import cn.iocoder.yudao.module.amazon.service.sales.AmazonSalesService;
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

/** Amazon Sales 管理接口。 */
@Tag(name = "管理后台 - Amazon Sales") @RestController @RequestMapping("/amazon/sales") @Validated
public class AmazonSalesController {
    @Resource private AmazonSalesService amazonSalesService;
    /** 查询指定条件下的 Amazon 聚合订单指标。 */
    @PostMapping("/order-metrics") @Operation(summary = "查询 Amazon 销售订单指标")
    @PreAuthorize("@ss.hasPermission('amazon:sales:query')")
    public CommonResult<Map<String, Object>> getOrderMetrics(@Valid @RequestBody AmazonSalesOrderMetricsReqVO request) {
        return CommonResult.success(amazonSalesService.getOrderMetrics(request));
    }
}
