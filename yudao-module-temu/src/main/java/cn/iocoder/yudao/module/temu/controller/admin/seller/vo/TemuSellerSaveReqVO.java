package cn.iocoder.yudao.module.temu.controller.admin.seller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Temu 卖家商城授权信息新增/修改 Request VO")
@Data
public class TemuSellerSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16844")
    private Long id;

    @Schema(description = "API 权限范围列表")
    private String apiScopeList;

    @Schema(description = "接口完整响应快照，便于兼容后续字段")
    private String responseJson;

    @Schema(description = "最近一次同步授权信息时间")
    private LocalDateTime lastSyncTime;

}