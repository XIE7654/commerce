package cn.iocoder.yudao.module.amazon.controller.admin.datakiosk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Data Kiosk 创建 GraphQL 查询参数。 */
@Data
public class DataKioskCreateQueryReqVO extends DataKioskBaseReqVO {
    @Schema(description = "待提交的 GraphQL 查询，移除无用空白后最长 8000 个字符", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "GraphQL 查询不能为空") @Size(max = 8000, message = "GraphQL 查询不能超过 8000 个字符") private String query;
    @Schema(description = "查询结果分页令牌；获取下一页时需与原 query 一起传入") private String paginationToken;
}
