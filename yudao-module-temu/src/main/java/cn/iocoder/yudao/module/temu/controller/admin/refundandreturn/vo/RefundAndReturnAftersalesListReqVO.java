package cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 售后单列表查询请求参数。
 */
@Schema(description = "管理后台 - Refund And Return 售后单列表查询 Request VO")
@Data
public class RefundAndReturnAftersalesListReqVO extends RefundAndReturnBaseReqVO {

    /** 页码，从 1 开始。 */
    @Schema(description = "页码，从 1 开始", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于等于 1")
    private Integer pageNo;

    /** 每页记录数。 */
    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "每页记录数不能为空")
    @Min(value = 1, message = "每页记录数必须大于等于 1")
    private Integer pageSize;

    /** 用于筛选的父售后单号列表；为空时按 Temu 平台默认规则查询。 */
    @Schema(description = "父售后单号列表", example = "[\"PO-211-06792196915752890-D01\"]")
    private List<String> parentAfterSalesSnList;
}
