package cn.iocoder.yudao.module.amazon.controller.admin.spapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/** Amazon SP-API 通用操作请求参数，请求体字段按对应 Amazon 模型透传。 */
@Data
public class AmazonSpApiReqVO {

    @NotNull(message = "店铺编号不能为空")
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @NotBlank(message = "国家代码不能为空")
    @Schema(description = "用于选择 SP-API 区域端点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    private String countryCode;

    @Schema(description = "路径参数，键必须与对应 operation 路径占位符一致")
    private Map<String, String> pathParams;

    @Schema(description = "查询参数，字段按对应 Amazon operation 模型传入")
    private Map<String, String> query;

    @Schema(description = "JSON 请求体，字段按对应 Amazon operation 模型传入")
    private Map<String, Object> body;
}
