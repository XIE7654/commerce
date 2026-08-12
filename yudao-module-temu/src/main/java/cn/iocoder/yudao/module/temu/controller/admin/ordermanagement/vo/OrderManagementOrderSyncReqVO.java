package cn.iocoder.yudao.module.temu.controller.admin.ordermanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Order Management 订单同步请求参数。
 *
 * <p>服务端根据订单主键取得店铺、父订单号和授权信息，避免由调用方传入这些关联数据。</p>
 */
@Schema(description = "管理后台 - Order Management 订单同步 Request VO")
@Data
public class OrderManagementOrderSyncReqVO {

    /** 本地 temu_order 主键编号。 */
    @Schema(description = "本地 Temu 订单主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "订单编号不能为空")
    private Long orderId;
}
