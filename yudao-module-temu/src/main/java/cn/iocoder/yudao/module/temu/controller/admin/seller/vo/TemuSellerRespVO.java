package cn.iocoder.yudao.module.temu.controller.admin.seller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Temu 卖家商城授权信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemuSellerRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6161")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "关联 temu_shop.id", requiredMode = Schema.RequiredMode.REQUIRED, example = "572")
    @ExcelProperty("关联 temu_shop.id")
    private Long shopId;

    @Schema(description = "店铺名称", example = "Temu 美国店")
    @ExcelProperty("店铺名称")
    private String shopName;

    @Schema(description = "Temu semiUniqueId", example = "15616")
    @ExcelProperty("Temu semiUniqueId")
    private String semiUniqueId;

    @Schema(description = "Temu 区域编号", example = "2860")
    @ExcelProperty("Temu 区域编号")
    private Integer regionId;

    @Schema(description = "Temu mallId", requiredMode = Schema.RequiredMode.REQUIRED, example = "27627")
    @ExcelProperty("Temu mallId")
    private Long mallId;

    @Schema(description = "Temu mallType", example = "2")
    @ExcelProperty("Temu mallType")
    private Integer mallType;

    @Schema(description = "店铺标签列表，Temu 标签枚举 JSON 数组", example = "[0]")
    @ExcelProperty("店铺标签列表")
    private String tags;

    @Schema(description = "应用订阅状态", example = "2")
    @ExcelProperty("应用订阅状态")
    private Integer appSubscribeStatus;

    @Schema(description = "授权过期时间，Unix 时间戳（秒）")
    @ExcelProperty("授权过期时间，Unix 时间戳（秒）")
    private Long expiredTime;

    @Schema(description = "授权过期时间，便于数据库查询")
    @ExcelProperty("授权过期时间，便于数据库查询")
    private LocalDateTime expiredAt;

    @Schema(description = "应用订阅事件编码列表")
    @ExcelProperty("应用订阅事件编码列表")
    private String appSubscribeEventCodeList;

    @Schema(description = "授权事件及权限状态列表")
    @ExcelProperty("授权事件及权限状态列表")
    private String authEventCodeList;

    @Schema(description = "API 权限范围列表")
    @ExcelProperty("API 权限范围列表")
    private String apiScopeList;

    @Schema(description = "接口完整响应快照，便于兼容后续字段")
    @ExcelProperty("接口完整响应快照，便于兼容后续字段")
    private String responseJson;

    @Schema(description = "最近一次同步授权信息时间")
    @ExcelProperty("最近一次同步授权信息时间")
    private LocalDateTime lastSyncTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
