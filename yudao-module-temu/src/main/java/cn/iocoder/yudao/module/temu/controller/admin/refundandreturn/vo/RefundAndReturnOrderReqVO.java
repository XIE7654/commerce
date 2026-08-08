package cn.iocoder.yudao.module.temu.controller.admin.refundandreturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 退货单查询请求参数。
 */
@Schema(description = "管理后台 - Refund And Return 退货单查询 Request VO")
@Data
public class RefundAndReturnOrderReqVO extends RefundAndReturnBaseReqVO {

    /** 父售后单号。 */
    @Schema(description = "父售后单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PO-211-06792196915752890-D01")
    @NotBlank(message = "父售后单号不能为空")
    private String parentAfterSalesSn;

    /** 售后单号。 */
    @Schema(description = "售后单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PO-211-06792196915752890")
    @NotBlank(message = "售后单号不能为空")
    private String afterSalesSn;
}
