package cn.iocoder.yudao.module.amazon.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - Amazon店铺授权新增/修改 Request VO")
@Data
public class AmazonShopSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11541")
    private Long id;

    @Schema(description = "Amazon sellerId", example = "18348")
    private String sellerId;

    @Schema(description = "默认 marketplaceId", example = "29401")
    private String marketplaceId;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "店铺名称不能为空")
    private String shopName;

    @Schema(description = "Amazon 区域：NA、EU、FE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Amazon 区域：NA、EU、FE不能为空")
    private String region;

    @Schema(description = "状态：0-启用，1-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：0-启用，1-禁用不能为空")
    private Integer status;

}