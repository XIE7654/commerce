package cn.iocoder.yudao.module.temu.controller.admin.buyshipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Buy Shipping 面单查询请求参数。
 */
@Schema(description = "管理后台 - Buy Shipping 面单查询 Request VO")
@Data
public class BuyShippingLabelReqVO extends BuyShippingBaseReqVO {

    /** 待获取面单的包裹编号。 */
    @Schema(description = "包裹编号列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "包裹编号不能为空")
    private List<String> packageSnList;
}
