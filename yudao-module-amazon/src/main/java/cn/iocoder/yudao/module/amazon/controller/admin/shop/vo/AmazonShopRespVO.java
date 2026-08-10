package cn.iocoder.yudao.module.amazon.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - Amazon店铺授权 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AmazonShopRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11541")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "Amazon sellerId", example = "18348")
    @ExcelProperty("Amazon sellerId")
    private String sellerId;

    @Schema(description = "默认 marketplaceId", example = "29401")
    @ExcelProperty("默认 marketplaceId")
    private String marketplaceId;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("店铺名称")
    private String shopName;

    @Schema(description = "Amazon 区域：NA、EU、FE", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Amazon 区域：NA、EU、FE")
    private String region;

    @Schema(description = "授权时间")
    @ExcelProperty("授权时间")
    private LocalDateTime authorizeTime;

    @Schema(description = "授权过期时间")
    @ExcelProperty("授权过期时间")
    private LocalDateTime authorizeExpireTime;

    @Schema(description = "广告 access token 过期时间")
    @ExcelProperty("广告 access token 过期时间")
    private LocalDateTime adAccessTokenExpiresAt;

    @Schema(description = "广告授权时间")
    @ExcelProperty("广告授权时间")
    private LocalDateTime adAuthorizeTime;

    @Schema(description = "状态：0-启用，1-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：0-启用，1-禁用")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}