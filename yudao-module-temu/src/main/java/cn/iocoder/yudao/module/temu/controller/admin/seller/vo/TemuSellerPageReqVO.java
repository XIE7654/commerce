package cn.iocoder.yudao.module.temu.controller.admin.seller.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Temu 卖家商城授权信息分页 Request VO")
@Data
public class TemuSellerPageReqVO extends PageParam {

    @Schema(description = "关联 temu_shop.id", example = "572")
    private Long shopId;

    @Schema(description = "Temu 区域编号", example = "2860")
    private Integer regionId;

    @Schema(description = "Temu mallId", example = "27627")
    private Long mallId;

    @Schema(description = "应用订阅状态", example = "2")
    private Integer appSubscribeStatus;

    @Schema(description = "授权过期时间，Unix 时间戳（秒）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Long[] expiredTime;

    @Schema(description = "授权过期时间，便于数据库查询")
    private LocalDateTime expiredAt;

    @Schema(description = "API 权限范围列表")
    private String apiScopeList;

    @Schema(description = "接口完整响应快照，便于兼容后续字段")
    private String responseJson;

    @Schema(description = "最近一次同步授权信息时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastSyncTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}