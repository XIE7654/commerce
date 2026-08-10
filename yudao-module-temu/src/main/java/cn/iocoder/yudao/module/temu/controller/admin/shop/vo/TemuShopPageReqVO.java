package cn.iocoder.yudao.module.temu.controller.admin.shop.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Temu 店铺分页 Request VO")
@Data
public class TemuShopPageReqVO extends PageParam {

    @Schema(description = "店铺类型：1-全托管，2-半托管，3-本土店铺", example = "2")
    private Integer shopType;

    @Schema(description = "Temu 站点代码，例如 US、DE、JP")
    private String site;

    @Schema(description = "店铺名称", example = "赵六")
    private String shopName;

    @Schema(description = "Temu 授权 Token")
    private String authToken;

    @Schema(description = "状态：0-开启，1-关闭", example = "0")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
