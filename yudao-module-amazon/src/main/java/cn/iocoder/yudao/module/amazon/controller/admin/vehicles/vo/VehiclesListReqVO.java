package cn.iocoder.yudao.module.amazon.controller.admin.vehicles.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** Amazon Vehicles 分页查询参数。 */
@Data
public class VehiclesListReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "调用端点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "DE")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "查询的 Marketplace ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "A1PA6795UKMFR9")
    @NotBlank(message = "marketplaceId 不能为空")
    private String marketplaceId;

    @Schema(description = "车型类型", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"CAR", "MOTORBIKE"}, example = "CAR")
    @NotBlank(message = "vehicleType 不能为空")
    @Pattern(regexp = "CAR|MOTORBIKE", message = "vehicleType 必须为 CAR 或 MOTORBIKE")
    private String vehicleType;

    @Schema(description = "分页令牌", example = "sdlkj234lkj234lksjdflkjwdflkjsfdlkj234234234234")
    private String pageToken;

    @Schema(description = "只返回此 ISO 8601 时间之后更新的车型", example = "2024-01-05T18:00:03+00:00")
    private String updatedAfter;
}
