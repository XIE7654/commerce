package cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Customer Feedback 浏览节点维度查询参数。 */
@Data
public class CustomerFeedbackBrowseNodeReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "查询站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空") private String countryCode;
    @Schema(description = "浏览节点编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "283155")
    @NotBlank(message = "浏览节点编号不能为空") private String browseNodeId;
    @Schema(description = "主题排序方式；查询主题时必填", example = "MENTIONS")
    private String sortBy;
}
