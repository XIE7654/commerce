package cn.iocoder.yudao.module.amazon.controller.admin.awd.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** AWD 接口通用请求参数；业务请求体按 Amazon 模型以 JSON Map 透传。 */
@Data
public class AwdRequestVO {
    @Schema(description = "Amazon 店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;
    @Schema(description = "站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;
    @Schema(description = "资源编号（订单或货件）")
    private String resourceId;
    @Schema(description = "查询参数，键值按 AWD 模型传入")
    private Map<String, String> query;
    @Schema(description = "Amazon 请求体，字段按 AWD 模型传入")
    private Map<String, Object> body;
}
