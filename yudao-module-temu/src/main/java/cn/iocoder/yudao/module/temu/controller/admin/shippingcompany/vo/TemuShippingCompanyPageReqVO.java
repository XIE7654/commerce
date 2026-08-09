package cn.iocoder.yudao.module.temu.controller.admin.shippingcompany.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Temu 区域承运商目录分页 Request VO")
@Data
public class TemuShippingCompanyPageReqVO extends PageParam {

    @Schema(description = "Temu 站点代码，例如 US、DE、JP")
    private String site;

    @Schema(description = "Temu 区域编号", example = "1004")
    private Long regionId;

    @Schema(description = "Temu 物流服务商编号", example = "6863")
    private Long logisticsServiceProviderId;

    @Schema(description = "物流服务商名称", example = "李四")
    private String logisticsServiceProviderName;

    @Schema(description = "物流品牌名称", example = "赵六")
    private String logisticsBrandName;

}