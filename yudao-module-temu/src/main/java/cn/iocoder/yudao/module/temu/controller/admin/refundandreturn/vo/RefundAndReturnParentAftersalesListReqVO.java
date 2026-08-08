package cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 父售后单列表查询请求参数。
 */
@Schema(description = "管理后台 - Refund And Return 父售后单列表查询 Request VO")
@Data
public class RefundAndReturnParentAftersalesListReqVO extends RefundAndReturnBaseReqVO {

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

    /** 售后状态组，取值以 Temu 平台定义为准。 */
    @Schema(description = "售后状态组", example = "7")
    private Integer afterSalesStatusGroup;

    /** 售后单创建时间下限，Unix 秒级时间戳。 */
    @Schema(description = "创建时间起始 Unix 秒级时间戳", example = "1747187481")
    @Min(value = 0, message = "创建时间起始时间戳不能小于 0")
    private Long createAtStart;

    /** 售后单创建时间上限，Unix 秒级时间戳。 */
    @Schema(description = "创建时间结束 Unix 秒级时间戳", example = "1747387481")
    @Min(value = 0, message = "创建时间结束时间戳不能小于 0")
    private Long createAtEnd;
}
