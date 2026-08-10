package cn.iocoder.yudao.module.amazon.controller.admin.shop.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Amazon店铺授权分页 Request VO")
@Data
public class AmazonShopPageReqVO extends PageParam {

    @Schema(description = "店铺名称", example = "张三")
    private String shopName;

    @Schema(description = "Amazon 区域：NA、EU、FE")
    private String region;

    @Schema(description = "状态：0-启用，1-禁用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}