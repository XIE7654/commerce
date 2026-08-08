package cn.iocoder.yudao.module.amazon.controller.admin.reports.vo;

import cn.iocoder.yudao.module.amazon.enums.AmazonReportTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Amazon 报表计划列表查询请求参数。
 */
@Data
public class AmazonReportSchedulesListReqVO {

    @Schema(description = "已完成 Seller 授权的店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "查询站点所属国家代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "US")
    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @Schema(description = "用于筛选报表计划的报表类型，最多 10 个", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "GET_MERCHANT_LISTINGS_ALL_DATA")
    @NotNull(message = "报表类型不能为空")
    @Size(min = 1, max = 10, message = "报表类型数量必须在 1 到 10 之间")
    private List<AmazonReportTypeEnum> reportTypes;
}
