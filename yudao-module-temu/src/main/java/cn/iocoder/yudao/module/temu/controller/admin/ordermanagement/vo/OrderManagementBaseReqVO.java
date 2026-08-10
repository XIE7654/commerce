package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Order Management 接口公共认证请求参数。
 */
@Data
public class OrderManagementBaseReqVO {

    /** 本地 Temu 店铺编号，用于确定卖家归属及订单租户隔离。 */
    @Schema(description = "本地 Temu 店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    /** Temu 站点代码，决定服务端读取的区域应用配置。 */
    @Schema(description = "Temu 站点代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "站点代码不能为空")
    private String site;

    /** 仅用于本次 Temu OpenAPI 调用的店铺授权 Token。 */
    @Schema(description = "Temu access_token", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken 不能为空")
    private String accessToken;
}
