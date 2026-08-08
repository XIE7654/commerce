package cn.iocoder.yudao.module.amazon.controller.admin.customerfeedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Customer Feedback 商品维度查询参数。 */
@Data
public class CustomerFeedbackItemReqVO {
    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空") private Long shopId;
    @Schema(description = "查询站点国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空") private String countryCode;
    @Schema(description = "ASIN", requiredMode = Schema.RequiredMode.REQUIRED, example = "B08N5WRWNW")
    @NotBlank(message = "ASIN 不能为空") private String asin;
    @Schema(description = "评论主题排序方式；查询评论主题时必填", example = "MENTIONS")
    private String sortBy;
}
