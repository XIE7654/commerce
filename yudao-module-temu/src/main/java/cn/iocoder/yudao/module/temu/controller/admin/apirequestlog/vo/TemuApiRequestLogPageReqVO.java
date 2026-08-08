package cn.iocoder.yudao.module.temu.controller.admin.apirequestlog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Temu OpenAPI 请求调用日志分页 Request VO")
@Data
public class TemuApiRequestLogPageReqVO extends PageParam {

    @Schema(description = "Temu 店铺编号", example = "29212")
    private Long shopId;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP")
    private String site;

    @Schema(description = "API 分类，例如 product、order")
    private String apiCategory;

    @Schema(description = "Temu OpenAPI 接口 type", example = "李四")
    private String operationName;

    @Schema(description = "HTTP 请求方式")
    private String requestMethod;

    @Schema(description = "脱敏后的完整请求 URL", example = "https://www.iocoder.cn")
    private String requestUrl;

    @Schema(description = "请求路径")
    private String requestPath;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}