package cn.iocoder.yudao.module.amazon.controller.admin.reports.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Amazon 报表任务或报表文件标识请求参数。
 */
@Data
public class AmazonReportIdReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "报表所属站点的国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "Amazon 报表任务或报表文件编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234567890")
    @NotBlank(message = "Amazon 标识不能为空")
    private String id;
}
