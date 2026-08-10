package cn.iocoder.yudao.module.temu.controller.admin.order.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - Temu 订单分页 Request VO")
@Data
public class TemuOrderPageReqVO extends PageParam {

    @Schema(description = "关联 temu_shop.id", example = "4092")
    private Long shopId;

    @Schema(description = "关联 temu_seller.id，由店铺授权关系确定", example = "1515")
    private Long sellerId;

    @Schema(description = "Temu 父订单号")
    private String parentOrderSn;

    @Schema(description = "Temu 子订单号")
    private String orderSn;

    @Schema(description = "Temu 站点编号", example = "23343")
    private Integer siteId;

    @Schema(description = "Temu 区域编号", example = "21232")
    private Long regionId;

    @Schema(description = "父订单状态", example = "1")
    private Integer parentOrderStatus;

    @Schema(description = "子订单状态", example = "2")
    private Integer orderStatus;

    @Schema(description = "商品名称", example = "芋艿")
    private String goodsName;

    @Schema(description = "最近同步时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastSyncTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}